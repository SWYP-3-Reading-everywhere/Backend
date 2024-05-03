package com.book_everywhere.domain.post.dto;

import com.book_everywhere.domain.pin.dto.PinRespDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostRespDto {
    //@Valid 어노테이션 사용하기
    @NotBlank
    @Size(max = 20, message = "제목은 20자 이하로 입력해주세요.")
    private String title;

    @NotNull
    @Size(max = 1500, message = "내용은 1500자 이하로 입력해주세요")
    private String content;
    private List<String> post_imageUrl;

    private PinRespDto pinRespDto;

    @NotNull(message = "발행 여부를 정확히해주세요.")
    private boolean isPublishing;
}
