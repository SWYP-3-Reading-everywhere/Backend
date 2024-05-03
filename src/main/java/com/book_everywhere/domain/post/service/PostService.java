package com.book_everywhere.domain.post.service;

import com.book_everywhere.domain.post.dto.PostReqDto;
import com.book_everywhere.domain.post.dto.PostRespDto;

import java.util.List;

public interface PostService {
    void 장소_리뷰_생성(PostReqDto postReqDto);
    List<PostRespDto> 모든_장소_리뷰_조회();
    PostRespDto 장소_리뷰_조회(Long id);
    List<PostRespDto> 유저의_모든_장소_리뷰_조회(Long socialId);
    List<PostRespDto> 장소의_모든_리뷰_조회(String address);
    //#@!좋아요 계시글 조회 기능 필요
}
