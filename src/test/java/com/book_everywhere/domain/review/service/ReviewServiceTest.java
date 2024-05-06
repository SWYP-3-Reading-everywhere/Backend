package com.book_everywhere.domain.review.service;

import com.book_everywhere.domain.auth.entity.User;
import com.book_everywhere.domain.auth.repository.UserRepository;
import com.book_everywhere.domain.book.dto.BookRespDto;
import com.book_everywhere.domain.book.repository.BookRepository;
import com.book_everywhere.domain.auth.UserTestBuilder;
import com.book_everywhere.domain.book.dto.BookRespDtoBuilder;
import com.book_everywhere.domain.pin.dto.PinRespDtoTestBuilder;
import com.book_everywhere.domain.review.dto.ReviewRespDtoTestBuilder;
import com.book_everywhere.domain.pin.dto.PinRespDto;
import com.book_everywhere.domain.pin.repository.PinRepository;
import com.book_everywhere.domain.review.dto.PostRespDto;
import com.book_everywhere.domain.review.entity.Review;
import com.book_everywhere.domain.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
public class ReviewServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PinRepository pinRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired // ReviewService에 모의 객체들을 주입
    private ReviewServiceImpl reviewServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화
    }

    @DisplayName("Service_독후감생성_테스트")
    @Test
    public void testCreateReview() throws Exception {
        // given: 테스트에 필요한 데이터 준비
        User fakeUser = UserTestBuilder.createDefault();
        BookRespDto bookRespDto = BookRespDtoBuilder.createDefault();
        PinRespDto pinRespDto = PinRespDtoTestBuilder.createDefault();
        PostRespDto postRespDto = ReviewRespDtoTestBuilder.createDefault(pinRespDto, bookRespDto);

        userRepository.save(fakeUser);
        bookRepository.save(bookRespDto.toEntity());
        pinRepository.save(pinRespDto.toEntity());

        // when: 독후감 생성 로직 실행
        reviewServiceImpl.독후감생성(postRespDto);

        // then: 데이터가 예상대로 저장되었는지 검증
        assertThat(reviewRepository.mFindReviewsByUser(fakeUser.getSocialId())).isNotEmpty();

        // 추가 검증: 저장된 리뷰의 내용, 사용자 ID 등을 검증할 수 있습니다
        Review savedReview = reviewRepository.mFindReviewsByUser(fakeUser.getSocialId()).get(0);
        assertThat(savedReview.getUser().getSocialId()).isEqualTo(fakeUser.getSocialId());
    }

    @DisplayName("Service_독후감삭제_테스트")
    @Test
    public void testDeleteReview() throws Exception {
        reviewRepository.deleteAll();
        pinRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();

        // given: 테스트에 필요한 리뷰 데이터 준비 및 저장
        User fakeUser = UserTestBuilder.createDefault();
        BookRespDto bookRespDto = BookRespDtoBuilder.createDefault();
        PinRespDto pinRespDto = PinRespDtoTestBuilder.createDefault();
        PostRespDto postRespDto = ReviewRespDtoTestBuilder.createDefault(pinRespDto, bookRespDto);
        
        // 없으면깨짐
        userRepository.save(fakeUser);
        bookRepository.save(bookRespDto.toEntity());
        pinRepository.save(pinRespDto.toEntity());

        Long reviewId = reviewServiceImpl.독후감생성(postRespDto);
        assertThat(reviewRepository.mFindReviewsByUser(fakeUser.getSocialId())).isNotEmpty();
        // when: 저장된 리뷰 삭제 로직 실행
        reviewServiceImpl.독후감삭제(reviewId);

        // then: 리뷰가 데이터베이스에서 제거되었는지 확인
        Optional<Review> deletedReview = reviewRepository.findById(reviewId);
        assertThat(deletedReview).isEmpty();
    }

}
