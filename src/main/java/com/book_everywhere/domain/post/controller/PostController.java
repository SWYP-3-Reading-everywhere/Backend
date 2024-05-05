package com.book_everywhere.domain.post.controller;

import com.book_everywhere.common.dto.CMRespDto;
import com.book_everywhere.common.dto.CountDto;
import com.book_everywhere.domain.post.facade.PostFacade;
import com.book_everywhere.domain.post.dto.PostReqDto;
import com.book_everywhere.domain.post.dto.PostRespDto;
import com.book_everywhere.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostFacade postFacade;

    //전체 장소 리뷰 조회
    @Operation(summary = "모든 큐레이팅 조회", description = "모든 큐레이팅 조회 return = List<PostRespDto>")
    @GetMapping("/api/posts")
    public CMRespDto<?> findAllPosts() {
        List<PostRespDto> result = postService.모든_장소_리뷰_조회();
        return new CMRespDto<>(HttpStatus.OK, result, "모든 장소 리뷰 조회 성공!");
    }

    //단일 장소 리뷰 조회
    @Operation(summary = "단일 큐레이팅 조회", description = "단일 큐레이팅 조회 return = PostRespDto")
    @GetMapping("/api/post/{postId}")
    public CMRespDto<?> findPost(@PathVariable Long postId) {
        PostRespDto result = postService.장소_리뷰_조회(postId);
        return new CMRespDto<>(HttpStatus.OK,result,"단일 장소 리뷰 조회 성공!");
    }

    //유저 장소 리뷰 조회
    @Operation(summary = "유저의 모든 큐레이팅 조회", description = "유저의 모든 큐레이팅을 개수와 함께 조회합니다. return = CountDto<List<PostRespDto>>")
    @GetMapping("/api/user/post/{socialId}")
    public CMRespDto<?> findUserPosts(@PathVariable Long socialId) {
        List<PostRespDto> postRespDtos = postService.유저의_모든_장소_리뷰_조회(socialId);
        CountDto<?> result = new CountDto<>(postRespDtos);
        return new CMRespDto<>(HttpStatus.OK, result, "유저의 장소 리뷰 조회 성공!");
    }

    //유저 큐레이팅 생성 (임시)
    @Operation(summary = "큐레이팅 생성", description = "새 큐레이팅을 저장합니다.")
    @PostMapping("/api/post")
    public CMRespDto<?> savePost(@Valid @RequestBody PostReqDto postReqDto) {
        postFacade.장소_리뷰_등록(postReqDto); //#@! 이미지 관련 부분 및 태그 부분 구현이 필요합니다.
        return new CMRespDto<>(HttpStatus.OK, null, "큐레이팅 저장 성공!");
    }

    //해당 장소의 모든 리뷰 조회
    @Operation(summary = "장소의 모든 큐레이팅 조회", description = "장소의 모든 큐레이팅을 조회합니다. return = List<PostRespDto>")
    @GetMapping("/api/pin/post")
    public CMRespDto<?> findAllPostInPin(@RequestParam("address") String address) {
        List<PostRespDto> result = postService.장소의_모든_리뷰_조회(address);
        return new CMRespDto<>(HttpStatus.OK, result, "해당 장소의 모든 리뷰 조회 성공!");
    }

    //#@!유저의 좋아요 장소 리뷰 조회

    //#@!유저 장소 리뷰 수정

    //#@!유저 장소 리뷰 삭제
}
