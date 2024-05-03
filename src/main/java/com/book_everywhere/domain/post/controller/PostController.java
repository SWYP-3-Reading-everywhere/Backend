package com.book_everywhere.domain.post.controller;

import com.book_everywhere.common.dto.CMRespDto;
import com.book_everywhere.domain.post.dto.PostReqDto;
import com.book_everywhere.domain.post.dto.PostRespDto;
import com.book_everywhere.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //전체 장소 리뷰 조회
    @GetMapping("/api/posts")
    public CMRespDto<?> findAllPosts() {
        List<PostRespDto> result = postService.모든_장소_리뷰_조회();
        return new CMRespDto<>(HttpStatus.OK, result, "모든 장소 리뷰 조회 성공!");
    }

    //단일 장소 리뷰 조회
    @GetMapping("/api/post/{postId}")
    public CMRespDto<?> findPost(@PathVariable Long postId) {
        PostRespDto result = postService.장소_리뷰_조회(postId);
        return new CMRespDto<>(HttpStatus.OK,result,"단일 장소 리뷰 조회 성공!");
    }

    //유저 장소 리뷰 조회
    @GetMapping("/api/post/{socialId}")
    public CMRespDto<?> findUserPosts(@PathVariable Long socialId) {
        List<PostRespDto> result = postService.유저의_모든_장소_리뷰_조회(socialId);
        return new CMRespDto<>(HttpStatus.OK, result, "유저의 장소 리뷰 조회 성공!");
    }

    //유저 큐레이팅 생성
    @PostMapping("/api/post")
    public CMRespDto<?> savePost(@Valid @RequestBody PostReqDto postReqDto) {
        postService.장소_리뷰_생성(postReqDto);
        return new CMRespDto<>(HttpStatus.OK, null, "큐레이팅 저장 성공!");
    }

    //#@!유저의 좋아요 장소 리뷰 조회

    //#@!유저 장소 리뷰 수정

    //#@!유저 장소 리뷰 삭제
}
