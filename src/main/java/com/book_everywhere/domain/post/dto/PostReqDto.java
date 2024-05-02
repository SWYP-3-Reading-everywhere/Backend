package com.book_everywhere.domain.post.dto;

import com.book_everywhere.domain.auth.entity.User;
import com.book_everywhere.domain.pin.dto.PinRespDto;
import com.book_everywhere.domain.pin.entity.Pin;
import com.book_everywhere.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//장소 리뷰 등록 요청용 Dto
public class PostReqDto {
    private Long socialId;
    private String title;
    private String content;
    private String imageUrl;
    private PinRespDto pinRespDto;

    // 리뷰 임시저장 or 발행 여부
    private boolean isPublishing;

    public Post toEntity(User user, Pin pin) {
        return Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .pin(pin)
                .build();
    }
}
