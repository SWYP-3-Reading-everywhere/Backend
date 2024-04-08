package com.book_everywhere.domain.tag.repository;

import com.book_everywhere.domain.tag.entity.Tagged;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaggedRepository extends JpaRepository<Tagged, Long> {
    // 선택한 태그에 1번이라도 Tag당한 핀 모두 호출
    @Query("SELECT t FROM Tagged t WHERE t.tag.id = :tagId AND t.pin.id = :pinId")
    Tagged mFindTagged(@Param("tagId") Long tagId, @Param("pinId") Long pinId);

    //pin에 tag들에 대한 count개수 상위 5개
    @Query("SELECT t.tag.content, COUNT(t) FROM Tagged t WHERE t.pin.id = :pinId GROUP BY t.tag.content ORDER BY COUNT(t) DESC LIMIT 4")
    List<Object[]> mCountByPinId(@Param("pinId") Long pinId);

    //핀에 해당하는 모든 태그 조회하기 - WebDataService에서 사용하는 용도
    List<Tagged> findAllByPinId(Long pinId);

    @Query("SELECT t FROM Tagged t WHERE t.pin.id = :pinId AND t.user.socialId = :socialId")
    List<Tagged> mFindAllByPinAndUser(@Param("pinId") Long pinId, @Param("socialId") Long socialId);

    @Modifying
    @Query("DELETE FROM Tagged t WHERE t.pin.id = :pinId AND t.user.socialId = :socialId")
    void deleteAllByPinAndUser(@Param("pinId") Long pinId, @Param("socialId") Long socialId);
}