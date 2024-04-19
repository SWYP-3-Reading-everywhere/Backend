package com.book_everywhere.domain.review.repository;

import com.book_everywhere.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    //공유 목록에서의 모든 독후감 조회
    List<Review> findByIsPrivateOrderByCreatedDateDesc(boolean isPrivate);

    Optional<Review> findById(Long reviewId);

    // 개인지도에서 핀을 눌렀을때 독후감이 모두 뜨는 기능
    // Entity 바뀐다면 수정 필요 -> 수정완료
    @Query("SELECT review FROM Review review WHERE review.user.socialId = :socialId AND review.pin.id = :pinId ORDER BY review.createdDate DESC")
    List<Review> mFindReviewUserMap(@Param("socialId") Long socialId, @Param("pinId") Long pinId);

    // socialId를 통한 모든 독후감 생성
    @Query("SELECT r FROM Review r WHERE r.user.socialId = :socialId ORDER BY r.createdDate DESC")
    List<Review> mFindReviewsByUser(@Param("socialId") Long socialId);

    @Query("SELECT r FROM Review r WHERE r.book.id = :bookId ORDER BY r.createdDate DESC")
    List<Review> mFindReviewsByBook(@Param("bookId") Long bookId);

    @Query("SELECT r FROM Review r WHERE r.pin.id = :pinId ORDER BY r.createdDate DESC")
    List<Review> mFindReviewsByPin(@Param("pinId") Long pinId);
}
