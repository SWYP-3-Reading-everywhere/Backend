package com.book_everywhere.domain.tag.repository;

import com.book_everywhere.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    // content를 통해 가져옴
    @Query("SELECT tag FROM Tag tag WHERE tag.content = :content")
    Tag mFindTagByName(@Param("content") String content);


    @Query("SELECT t FROM Tag t JOIN FETCH t.category")
    List<Tag> findAllWithCategory();

    @EntityGraph(attributePaths = {"category"})
    List<Tag> findAll();
}
