package com.book_everywhere.domain.post.facade;

import com.book_everywhere.domain.pin.service.PinService;
import com.book_everywhere.domain.post.dto.PostReqDto;
import com.book_everywhere.domain.post.service.PostService;
import com.book_everywhere.domain.tag.service.TaggedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostFacade {
    private final PostService postService;
    private final PinService pinService;
    private final TaggedService taggedService;

    public void 장소_리뷰_등록(PostReqDto postReqDto) {
        pinService.핀생성포스트(postReqDto);
        //태그 생성
        postService.장소_리뷰_생성(postReqDto);
    }
}
