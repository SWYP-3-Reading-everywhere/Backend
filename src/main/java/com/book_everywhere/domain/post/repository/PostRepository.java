package com.book_everywhere.domain.post.repository;

import com.book_everywhere.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    //해당 유저의 계시글 조회
}
