package com.book_everywhere.domain.mark.repository;

import com.book_everywhere.domain.mark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("SELECT b FROM Bookmark b WHERE b.user.socialId = :socialId")
    List<Bookmark> findAllBySocialId(@Param("socialId") Long socialId);
}
