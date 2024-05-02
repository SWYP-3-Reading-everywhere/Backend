package com.book_everywhere.domain.post.dto;

import com.book_everywhere.domain.pin.dto.PinRespDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostRespDto {

    private String title;
    private String content;
    private List<String> post_imageUrl;
    private PinRespDto pinRespDto;
    private boolean isPublishing;
}
