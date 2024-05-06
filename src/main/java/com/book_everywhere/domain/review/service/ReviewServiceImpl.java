package com.book_everywhere.domain.review.service;

import com.book_everywhere.common.exception.customs.CustomErrorCode;
import com.book_everywhere.common.exception.customs.EntityNotFoundException;
import com.book_everywhere.common.exception.customs.PropertyBadRequestException;
import com.book_everywhere.domain.auth.entity.User;
import com.book_everywhere.domain.auth.repository.UserRepository;
import com.book_everywhere.domain.book.entity.Book;
import com.book_everywhere.domain.book.repository.BookRepository;
import com.book_everywhere.domain.likes.repository.LikesRepository;
import com.book_everywhere.domain.likes.service.LikesCachingService;
import com.book_everywhere.domain.pin.entity.Pin;
import com.book_everywhere.domain.pin.repository.PinRepository;
import com.book_everywhere.domain.review.dto.ReviewDto;
import com.book_everywhere.domain.review.dto.ReviewRespDto;
import com.book_everywhere.domain.review.entity.Review;
import com.book_everywhere.domain.review.repository.ReviewRepository;
import com.book_everywhere.domain.tag.service.TaggedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final PinRepository pinRepository;
    private final UserRepository userRepository;
    private final TaggedService taggedService;
    private final LikesRepository likesRepository;
    private final LikesCachingService likesCachingService;


    //사용자 검증에 메소드
    //등록
    @Transactional
    public Long 독후감생성(ReviewRespDto reviewRespDto) {
        User user = userRepository.findBySocialId(reviewRespDto.getSocialId()).orElseThrow(
                () -> new EntityNotFoundException(CustomErrorCode.USER_NOT_FOUND));
        Book book = bookRepository.mFindBookIsbn(reviewRespDto.getBookRespDto().getIsbn());
        if (book == null) {
            throw new EntityNotFoundException(CustomErrorCode.BOOK_NOT_FOUND);
        }

        Pin pin = pinRepository.mFindPinByAddress(reviewRespDto.getPinRespDto().getAddress());
        if (pin == null) {
            throw new EntityNotFoundException(CustomErrorCode.PIN_NOT_FOUND);
        }

        Review review = Review.builder()
                .book(book)
                .pin(pin)
                .user(user)
                .title(reviewRespDto.getTitle())
                .writer(reviewRespDto.getWriter())
                .content(reviewRespDto.getContent())
                .isPrivate(reviewRespDto.isPrivate())
                .isBookComplete(reviewRespDto.getBookRespDto().isComplete())
                .build();
        reviewRepository.save(review);
        return review.getId();
    }

    //책에따른 모든 리뷰기능이 추가되었습니다.
    public List<ReviewDto> 책에따른모든리뷰(Long socialId, Long bookId) {
        List<Review> init = reviewRepository.mFindReviewsByBook(bookId);
        if (init.isEmpty()) {
            throw new EntityNotFoundException(CustomErrorCode.PIN_NOT_FOUND);
        }
        User user = userRepository.findBySocialId(socialId).orElseThrow();

        return init.stream().map(review -> {
            Long likeCount = likesCachingService.좋아요캐시업데이트(review.getId());
            boolean likeState = likesRepository.existsByUserIdAndReviewId(user.getId(), review.getId());
            return ReviewDto.toDto(review, likeCount, likeState);
        }).toList();
    }


    //단일 핀을 눌렀을때 독후감이 조회됩니다.
    public List<ReviewDto> 단일핀독후감조회(Long socialId, Long pinId) {
        List<Review> init = reviewRepository.mFindReviewUserMap(socialId, pinId);
        if (init.isEmpty()) {
            throw new EntityNotFoundException(CustomErrorCode.PIN_NOT_FOUND);
        }
        User user = userRepository.findBySocialId(socialId).orElseThrow();

        return init.stream().map(review -> {
            Long likeCount = likesCachingService.좋아요캐시업데이트(review.getId());
            boolean likeState = likesRepository.existsByUserIdAndReviewId(user.getId(), review.getId());
            return ReviewDto.toDto(review, likeCount, likeState);
        }).toList();
    }


    public List<ReviewDto> 유저모든독후감조회(Long socialId) {
        List<Review> init = reviewRepository.mFindReviewsByUser(socialId);
        User user = userRepository.findBySocialId(socialId).orElseThrow();

        return init.stream().map(review -> {
            Long likeCount = likesCachingService.좋아요캐시업데이트(review.getId());
            boolean likeState = likesRepository.existsByUserIdAndReviewId(user.getId(), review.getId());
            return ReviewDto.toDto(review, likeCount, likeState);
        }).toList();
    }


    //리뷰 하나만 조회
    public ReviewDto 단일독후감조회(Long socialId, Long review_id) {
        User user = userRepository.findBySocialId(socialId).orElseThrow();

        Review review = reviewRepository.findById(review_id).orElseThrow(
                () -> new EntityNotFoundException(CustomErrorCode.REVIEW_NOT_FOUND));
        Long likeCount = likesCachingService.좋아요캐시업데이트(review.getId());
        boolean likeState = likesRepository.existsByUserIdAndReviewId(user.getId(), review.getId());
        return ReviewDto.toDto(review, likeCount, likeState);
    }


    //등록된 모든 리뷰 조회
    public List<ReviewDto> 모든독후감조회(Long socialId) {
        List<Review> init = reviewRepository.findAll();

        Optional<User> optionalUser = userRepository.findBySocialId(socialId);

        return init.stream().map(review -> {
            Long likeCount = likesCachingService.좋아요캐시업데이트(review.getId());
            boolean likeState = optionalUser
                    .map(user -> likesRepository.existsByUserIdAndReviewId(user.getId(), review.getId()))
                    .orElse(false);
            return ReviewDto.toDto(review, likeCount, likeState);
        }).toList();
    }



    public List<ReviewDto> 모든공유독후감조회(Long socialId) {
        List<Review> init = reviewRepository.findByIsPrivateOrderByCreatedDateDesc(false);
        Optional<User> optionalUser = userRepository.findBySocialId(socialId);

        return init.stream().map(review -> {
            Long likeCount = likesCachingService.좋아요캐시업데이트(review.getId());
            boolean likeState = optionalUser
                    .map(user -> likesRepository.existsByUserIdAndReviewId(user.getId(), review.getId()))
                    .orElse(false);
            return ReviewDto.toDto(review, likeCount, likeState);
        }).toList();
    }

    @Transactional
    public void 독후감수정(Long reviewId, ReviewRespDto postRespDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException(CustomErrorCode.REVIEW_NOT_FOUND));
        Book existBook = bookRepository.mFindBookIsbn(postRespDto.getBookRespDto().getIsbn());
        Pin existPin = pinRepository.mFindPinByAddress(postRespDto.getPinRespDto().getAddress());
        User user = userRepository.findBySocialId(postRespDto.getSocialId()).orElseThrow(() -> new EntityNotFoundException(CustomErrorCode.USER_NOT_FOUND));
        review.changeReview(postRespDto.getTitle(), postRespDto.getContent(), postRespDto.isPrivate(), postRespDto.getBookRespDto().isComplete(), postRespDto.getWriter());
        review.setUser(user);
        review.setBook(existBook);
        review.setPin(existPin);
        reviewRepository.save(review);
    }

    @Transactional
    public void 유저독후감개수검증후책삭제(String isbn) {
        Book book = bookRepository.mFindBookIsbn(isbn);
        if (book == null) {
            throw new EntityNotFoundException(CustomErrorCode.BOOK_NOT_FOUND);
        }
        List<Review> reviews = reviewRepository.mFindReviewsByBook(book.getId());
        if (reviews.isEmpty()) {
            bookRepository.delete(book);
        }
    }
    @Transactional
    public void 독후감개수검증삭제(String prevBookTitle) {
        Book book = bookRepository.mFindBookTitle(prevBookTitle);
        if (book == null) {
            throw new EntityNotFoundException(CustomErrorCode.BOOK_NOT_FOUND);
        }
        List<Review> reviews = reviewRepository.mFindReviewsByBook(book.getId());
        if (reviews.isEmpty()) {
            bookRepository.delete(book);
        }
    }
    @Transactional
    public void 독후감개수검증후핀삭제(String address, Long socialId) {
        Pin pin = pinRepository.mFindPinByAddress(address);
        if (pin == null) {
            throw new EntityNotFoundException(CustomErrorCode.PIN_NOT_FOUND);
        }
        List<Review> reviews = reviewRepository.mFindReviewsByPin(pin.getId());
        if (reviews.isEmpty()) {
            if (!pin.getTags().isEmpty()) {
                taggedService.태그삭제(address, socialId);
            }
            pinRepository.delete(pin);
        }
    }
    @Transactional
    public void 독후감삭제(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException(CustomErrorCode.REVIEW_NOT_FOUND));
        reviewRepository.delete(review);
    }

    public void 등록또는수정전예외처리(ReviewRespDto postRespDto) {
        if (postRespDto.getTitle() == null || postRespDto.getTitle().isEmpty()) {
            throw new PropertyBadRequestException(CustomErrorCode.TITLE_IS_NOT_BLANK);
        }
        if (postRespDto.getTitle().length() > 20) {
            throw new PropertyBadRequestException(CustomErrorCode.TITLE_IS_NOT_OVER_20);
        }
        if (postRespDto.getContent() == null || postRespDto.getContent().isEmpty()) {
            throw new PropertyBadRequestException(CustomErrorCode.CONTENT_IS_NOT_BLANK);
        }
        if (postRespDto.getContent().length() > 1500) {
            throw new PropertyBadRequestException(CustomErrorCode.CONTENT_IS_NOT_OVER_1500);
        }
        if (postRespDto.getBookRespDto() == null) {
            throw new PropertyBadRequestException(CustomErrorCode.BOOK_IS_NOT_NULL);
        }
        if (postRespDto.getPinRespDto().getAddress() == null || postRespDto.getPinRespDto().getAddress().isEmpty()) {
            throw new PropertyBadRequestException(CustomErrorCode.ADDRESS_IS_NOT_NULL);
        }
    }

}