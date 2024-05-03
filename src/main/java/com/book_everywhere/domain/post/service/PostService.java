package com.book_everywhere.domain.post.service;

import com.book_everywhere.domain.post.dto.PostReqDto;
import com.book_everywhere.domain.post.dto.PostRespDto;

import java.util.List;

public interface PostService {
    void 장소_리뷰_생성(PostReqDto postReqDto);
    PostRespDto 장소_리뷰_조회(Long id);
    List<PostRespDto> 유저의_모든_장소_리뷰_조회(Long socialId);
}
