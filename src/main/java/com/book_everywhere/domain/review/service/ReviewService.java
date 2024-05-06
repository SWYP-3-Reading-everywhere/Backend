package com.book_everywhere.domain.review.service;

import com.book_everywhere.domain.review.dto.ReviewDto;
import com.book_everywhere.domain.review.dto.ReviewRespDto;

import java.util.List;

public interface ReviewService {
    Long 독후감생성(ReviewRespDto reviewRespDto);

    List<ReviewDto> 책에따른모든리뷰(Long socialId, Long bookId);

    List<ReviewDto> 모든독후감조회(Long socialId);

    List<ReviewDto> 모든공유독후감조회(Long socialId);

    void 등록또는수정전예외처리(ReviewRespDto postRespDto);

    ReviewDto 단일독후감조회(Long socialId, Long reviewId);

    void 독후감수정(Long reviewId, ReviewRespDto reviewRespDto);

    void 유저독후감개수검증후책삭제(String isbn);

    void 독후감개수검증삭제(String prevBookTitle);

    void 독후감개수검증후핀삭제(String prevAddress, Long socialId);

    void 독후감삭제(Long reviewId);

    List<ReviewDto> 유저모든독후감조회(Long socialId);

    List<ReviewDto> 단일핀독후감조회(Long socialId, Long pinId);


}
