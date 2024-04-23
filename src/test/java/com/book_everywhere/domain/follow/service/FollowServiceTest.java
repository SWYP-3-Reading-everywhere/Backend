package com.book_everywhere.domain.follow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.book_everywhere.domain.auth.UserTestBuilder;
import com.book_everywhere.domain.auth.entity.User;
import com.book_everywhere.domain.auth.repository.UserRepository;
import com.book_everywhere.domain.follow.dto.SubscribeRespDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class FollowServiceTest {

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private UserRepository userRepository;

    private User fromUserTest;
    private User toUserTest;
    private User toUserTest2;

    @BeforeEach

    void setUp() {
        // 사용자 생성 및 저장
        fromUserTest = UserTestBuilder.createDefault();
        toUserTest = UserTestBuilder.createMany(1L);
        toUserTest2 = UserTestBuilder.createMany(2L);
        userRepository.save(fromUserTest);
        userRepository.save(toUserTest);
        userRepository.save(toUserTest2);
    }

    @Test
    void 구독하기_테스트() {
        //given
        subscribeService.구독하기(fromUserTest.getSocialId(), toUserTest.getSocialId());
        subscribeService.구독하기(toUserTest2.getSocialId(), toUserTest.getSocialId());

        //when
        List<SubscribeRespDto> subscriptions = subscribeService.구독자리스트(fromUserTest.getSocialId(), toUserTest.getSocialId());

        //then
        assertThat(subscriptions).hasSize(2);
        assertThat(subscriptions.get(0).getNickname()).isEqualTo(fromUserTest.getNickname());
        assertThat(subscriptions.get(1).getNickname()).isEqualTo(toUserTest2.getNickname());

    }

    @Test
    void 구독취소_테스트() {
        // 먼저 구독하기 실행
        subscribeService.구독하기(fromUserTest.getSocialId(), toUserTest.getSocialId());

        // 구독 취소 실행
        subscribeService.구독취소(fromUserTest.getSocialId(), toUserTest.getSocialId());

        // 구독 취소 확인
        List<SubscribeRespDto> subscriptions = subscribeService.구독자리스트(fromUserTest.getSocialId(), toUserTest.getSocialId());
        assertThat(subscriptions).isEmpty();
    }

    @Test
    void 구독_중복_예외() {
        // 첫 번째 구독 실행
        subscribeService.구독하기(fromUserTest.getSocialId(), toUserTest.getSocialId());

        // 같은 구독을 다시 시도할 때 예외 발생
        assertThrows(IllegalStateException.class, () -> subscribeService.구독하기(fromUserTest.getSocialId(), toUserTest.getSocialId()));
    }
}
