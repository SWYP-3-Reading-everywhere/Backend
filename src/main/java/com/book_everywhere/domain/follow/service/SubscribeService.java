package com.book_everywhere.domain.follow.service;

import com.book_everywhere.domain.follow.dto.SubscribeRespDto;

import java.util.List;

public interface SubscribeService {

    void 구독하기(Long fromUserSocialId, Long toUserSocialId);

    void 구독취소(Long fromUserSocialId, Long toUserSocialId);

    List<SubscribeRespDto> 구독자리스트(Long fromUserSocialId, Long pageUserSocialId);

    List<SubscribeRespDto> 구독리스트(Long fromUserSocialId, Long pageUserSocialId);
}
