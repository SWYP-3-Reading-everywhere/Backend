package com.book_everywhere.domain.follow.repository;

import com.book_everywhere.domain.auth.entity.User;
import com.book_everywhere.domain.follow.dto.SubscribeRespDto;
import com.book_everywhere.domain.follow.entity.Subscribe;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    boolean existsByFromUserAndToUser(User fromUser, User toUser);

    Optional<Subscribe> findByFromUserAndToUser(User fromUser, User toUser);


    @Query("SELECT new com.book_everywhere.domain.follow.dto.SubscribeRespDto(" +
            "u.socialId, u.nickname, u.image, " +
            "(SELECT COUNT(s2) > 0 FROM Subscribe s2 WHERE s2.fromUser.id = :fromUserId AND s2.toUser.id = s.fromUser.id), " +
            "(CASE WHEN s.fromUser.id = :fromUserId THEN true ELSE false END)) " +
            "FROM Subscribe s " +
            "JOIN s.fromUser u " +
            "WHERE s.toUser.id = :pageUserId")
    List<SubscribeRespDto> findSubscribersByPageUserId(@Param("fromUserId") Long fromUserId, @Param("pageUserId") Long pageUserId);

    @Query("SELECT new com.book_everywhere.domain.follow.dto.SubscribeRespDto(" +
            "u.socialId, u.nickname, u.image, " +
            "(SELECT COUNT(s2) > 0 FROM Subscribe s2 WHERE s2.fromUser.id = :fromUserId AND s2.toUser.id = s.toUser.id), " +
            "(CASE WHEN s.toUser.id = :fromUserId THEN true ELSE false END)) " +
            "FROM Subscribe s " +
            "JOIN s.toUser u " +
            "WHERE s.fromUser.id = :pageUserId")
    List<SubscribeRespDto> findSubscriptionsByPageUserId(@Param("fromUserId") Long fromUserId, @Param("pageUserId") Long pageUserId);

}
