package com.book_everywhere.domain.post.repository;

import com.book_everywhere.domain.post.dto.PostRespDto;
import com.book_everywhere.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    //해당 유저의 계시글 조회
    @Query("SELECT p FROM Post p WHERE p.user.socialId = :socialId")
    List<Post> mFindAllBySocialId(@Param("socialId") Long socialId);

    @Query("SELECT p FROM Post p WHERE p.pin.address = :address")
    List<Post> mFindAllByPinAddress(@Param("address") String address);
}
