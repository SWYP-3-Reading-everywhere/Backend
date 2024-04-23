package com.book_everywhere.domain.follow.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscribeRespDto {
    private Long userSocialId;
    private String nickname;
    private String image;
    private boolean subscribeState; // 지금 이 DTO의 사람을 구독한 상태인지
    private boolean equalState; // 나 자신인지

}