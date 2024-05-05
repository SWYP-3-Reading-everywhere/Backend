package com.book_everywhere.domain.post.dto;

import com.book_everywhere.domain.auth.UserTestBuilder;
import com.book_everywhere.domain.auth.entity.User;
import com.book_everywhere.domain.pin.dto.PinRespDto;
import com.book_everywhere.domain.pin.dto.PinRespDtoTestBuilder;
import com.book_everywhere.domain.pin.entity.Pin;
import com.book_everywhere.domain.post.entity.Post;

import java.util.Collections;

public class PostReqDtoTestBuilder {
    public static PostReqDto createDefault(PinRespDto pinRespDto) {
        return new PostReqDto(
                123L,
                "Default Name",
                "Default Content",
                Collections.singletonList("http://default.url"),
                pinRespDto,
                true
        );
    }

    public static Post toEntity(Pin pin){
        PostReqDto postReqDto = createDefault(PinRespDtoTestBuilder.createDefault());
        User user = UserTestBuilder.createDefault();
        Post post = postReqDto.toEntity(user,pin);
        return post;
    }
}
