package com.book_everywhere.domain.post.service;

import com.book_everywhere.domain.post.dto.PostReqDto;
import com.book_everywhere.domain.post.dto.PostRespDto;

public interface PostService {
    void 장소리뷰생성하기(PostReqDto postReqDto);
    PostRespDto 장소리뷰조회(Long id);
}
