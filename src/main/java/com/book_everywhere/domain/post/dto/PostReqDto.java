package com.book_everywhere.domain.post.dto;

import com.book_everywhere.domain.auth.entity.User;
import com.book_everywhere.domain.pin.dto.PinRespDto;
import com.book_everywhere.domain.pin.entity.Pin;
import com.book_everywhere.domain.post.entity.Post;
import com.book_everywhere.domain.post.entity.PostImage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
//장소 리뷰 등록 요청용 Dto
public class PostReqDto {
    private Long socialId;
    //@Valid 어노테이션 사용하기
    @NotBlank
    @Size(max = 20, message = "제목은 20자 이하로 입력해주세요.")
    private String title;
    @NotNull
    @Size(max = 1500, message = "내용은 1500자 이하로 입력해주세요")
    private String content;
    private List<String> imageUrls;
    private PinRespDto pinRespDto;

    // 리뷰 임시저장 or 발행 여부
    @NotNull(message = "발행 여부를 정확히해주세요.")
    private boolean isPublishing;

    //태그 생성을 위한 dto필요

    public Post toEntity(User user, Pin pin) {
        return Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .pin(pin)
                //#@!이미지 관련 코드가 필요합니다.
                .build();
    }
}
