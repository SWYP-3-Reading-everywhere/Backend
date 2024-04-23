package com.book_everywhere.domain.follow.service;

import com.book_everywhere.domain.auth.entity.User;
import com.book_everywhere.domain.auth.repository.UserRepository;
import com.book_everywhere.domain.follow.dto.SubscribeRespDto;
import com.book_everywhere.domain.follow.entity.Subscribe;
import com.book_everywhere.domain.follow.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SubscribeServiceImpl implements SubscribeService{

    private final UserRepository userRepository;
    private final SubscribeRepository subscribeRepository;

    @Override
    public List<SubscribeRespDto> 구독리스트(Long fromUserSocialId, Long pageUserSocialId) {
        User fromUser = userRepository.findBySocialId(fromUserSocialId)
                .orElseThrow(() -> new IllegalArgumentException("구독하는 사용자를 찾을 수 없습니다. SocialID: " + fromUserSocialId));
        User pageUser = userRepository.findBySocialId(pageUserSocialId)
                .orElseThrow(() -> new IllegalArgumentException("구독 받는 사용자를 찾을 수 없습니다. SocialID: " + pageUserSocialId));

        return subscribeRepository.findSubscriptionsByPageUserId(fromUser.getId(), pageUser.getId());
    }

    @Override
    public List<SubscribeRespDto> 구독자리스트(Long fromUserSocialId, Long pageUserSocialId) {
        User fromUser = userRepository.findBySocialId(fromUserSocialId)
                .orElseThrow(() -> new IllegalArgumentException("구독하는 사용자를 찾을 수 없습니다. SocialID: " + fromUserSocialId));
        User pageUser = userRepository.findBySocialId(pageUserSocialId)
                .orElseThrow(() -> new IllegalArgumentException("구독 받는 사용자를 찾을 수 없습니다. SocialID: " + pageUserSocialId));

        return subscribeRepository.findSubscribersByPageUserId(fromUser.getId(), pageUser.getId());
    }
    @Override
    @Transactional
    public void 구독하기(Long fromUserSocialId, Long toUserSocialId) {
        User fromUser = userRepository.findBySocialId(fromUserSocialId)
                .orElseThrow(() -> new IllegalArgumentException("구독하는 사용자를 찾을 수 없습니다. SocialID: " + fromUserSocialId));
        User toUser = userRepository.findBySocialId(toUserSocialId)
                .orElseThrow(() -> new IllegalArgumentException("구독 받는 사용자를 찾을 수 없습니다. SocialID: " + toUserSocialId));

        if(subscribeRepository.existsByFromUserAndToUser(fromUser,toUser)){
            throw new IllegalStateException("이미 구독 중입니다.");
        }

        Subscribe subscribe = new Subscribe(fromUser,toUser);
        subscribeRepository.save(subscribe);
    }

    @Override
    @Transactional
    public void 구독취소(Long fromUserSocialId, Long toUserSocialId) {
        User fromUser = userRepository.findBySocialId(fromUserSocialId)
                .orElseThrow(() -> new IllegalArgumentException("구독자를 찾을 수 없습니다: " + fromUserSocialId));
        User toUser = userRepository.findBySocialId(toUserSocialId)
                .orElseThrow(() -> new IllegalArgumentException("구독 대상자를 찾을 수 없습니다: " + toUserSocialId));
        Subscribe subscribe = subscribeRepository.findByFromUserAndToUser(fromUser, toUser)
                .orElseThrow(() -> new IllegalArgumentException("구독정보를 찾을 수 없습니다."));;
        subscribeRepository.delete(subscribe);
    }


}
