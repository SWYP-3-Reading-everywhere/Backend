package com.book_everywhere.domain.review.controller;

import com.book_everywhere.domain.book.service.BookService;
import com.book_everywhere.domain.likes.service.LikesService;
import com.book_everywhere.domain.pin.service.PinService;
import com.book_everywhere.domain.pin.service.VisitService;
import com.book_everywhere.domain.review.dto.ReviewDto;
import com.book_everywhere.domain.review.dto.ReviewRespDto;
import com.book_everywhere.domain.review.service.ReviewService;
import com.book_everywhere.domain.tag.service.TaggedService;
import com.book_everywhere.common.dto.CMRespDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {


    private final PinService pinService;
    private final TaggedService taggedService;
    private final VisitService visitService;
    private final BookService bookService;
    private final ReviewService reviewService;
    private final LikesService likesService;

    @PostMapping("/api/write")
    @Operation(summary = "독후감 추가", description = "독후감을 새로 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "독후감 추가 완료",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CMRespDto.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content)
    })
    public CMRespDto<?> addReview(@RequestBody ReviewRespDto reviewRespDto) {
        reviewService.등록또는수정전예외처리(reviewRespDto);
        bookService.책생성(reviewRespDto);
        pinService.핀생성리뷰(reviewRespDto);
        taggedService.태그등록(reviewRespDto);
        visitService.독후감쓰기전방문등록또는수정(reviewRespDto);
        reviewService.독후감생성(reviewRespDto);
        return new CMRespDto<>(HttpStatus.OK, null, "독후감 추가 완료");
    }

    //조회
    //공개 독후감 조회
    @Operation(summary = "모든 독후감 조회", description = "조건에 없이 모든 독후감을 조회합니다 return = List<ReviewDto>")
    @GetMapping("/api/reviews")
    public CMRespDto<?> publicReviews(@RequestParam(value = "socialId") Long socialId) {
        List<ReviewDto> result = reviewService.모든독후감조회(socialId);
        return new CMRespDto<>(HttpStatus.OK, result, "전체 공유 독후감 조회");
    }

    @Operation(summary = "모든 공유 독후감 조회", description = "공유 독후감을 조회합니다 return = List<ReviewDto>")
    @GetMapping("/api/review/public")
    public CMRespDto<?> findPublicReviews(@RequestParam(value = "socialId") Long socialId) {
        List<ReviewDto> result = reviewService.모든공유독후감조회(socialId);
        return new CMRespDto<>(HttpStatus.OK, result, "모든 공유 독후감 조회 완료");
    }

    @Operation(summary = "단일 책 독후감 조회", description = "단일 책 독후감을 조회합니다 return = List<ReviewDto>")
    @GetMapping("/api/detail/{bookId}")
    public CMRespDto<?> bookReviews(@RequestParam(value = "socialId") Long socialId, @PathVariable Long bookId) {
        List<ReviewDto> result = reviewService.책에따른모든리뷰(socialId, bookId);
        return new CMRespDto<>(HttpStatus.OK, result, "책에 따른 전체 독후감 조회");
    }

    //수정
    @Operation(summary = "단일 독후감 조회", description = "특정 독후감을 조회합니다 return = ReviewDto")
    @GetMapping("/api/review/{reviewId}")
    public CMRespDto<?> getReview(@RequestParam(value = "socialId") Long socialId, @PathVariable Long reviewId) {
        ReviewDto reviewDto = reviewService.단일독후감조회(socialId, reviewId);
        return new CMRespDto<>(HttpStatus.OK, reviewDto, "단일 독후감 조회");
    }

    @PutMapping("/api/review/edit/{reviewId}")
    @Operation(summary = "독후감 수정", description = "독후감을 수정합니다.")
    public CMRespDto<?> updateReview(@PathVariable Long reviewId,
                                     @RequestParam String prevBookTitle,
                                     @RequestParam String prevAddress,
                                     @RequestBody ReviewRespDto reviewRespDto) {
        reviewService.등록또는수정전예외처리(reviewRespDto);
        pinService.핀생성리뷰(reviewRespDto);
        bookService.책생성(reviewRespDto);
        taggedService.태그삭제(reviewRespDto.getPinRespDto().getAddress(), reviewRespDto.getSocialId());
        taggedService.태그등록(reviewRespDto);
        visitService.독후감쓰기전방문등록또는수정(reviewRespDto);
        reviewService.독후감수정(reviewId, reviewRespDto);
        reviewService.독후감개수검증삭제(prevBookTitle);
        reviewService.독후감개수검증후핀삭제(prevAddress, reviewRespDto.getSocialId());
        return new CMRespDto<>(HttpStatus.OK, null, "독후감 수정 완료");
    }
    @Operation(summary = "단일 독후감 삭제", description = "특정 독후감을 삭제합니다.")
    @DeleteMapping("/api/review/delete/{reviewId}")
    public CMRespDto<?> deleteReview(@PathVariable Long reviewId,
                                     @RequestParam Long socialId,
                                     @RequestParam String bookTitle,
                                     @RequestParam String address) {
        reviewService.독후감삭제(reviewId);
        taggedService.태그삭제(address, socialId);
        reviewService.유저독후감개수검증후책삭제(bookTitle);
        reviewService.독후감개수검증후핀삭제(address, socialId);
        return new CMRespDto<>(HttpStatus.OK, null, "독후감 삭제 완료");
    }

    // 좋아요 구현
    @Operation(summary = "좋아요", description = "특정 독후감을 좋아요합니다. 중복불가")
    @PostMapping("/api/review/{reviewId}/likes")
    public CMRespDto<?> like(@RequestParam Long socialId, @PathVariable Long reviewId) {
        likesService.좋아요(socialId, reviewId);
        return new CMRespDto<>(HttpStatus.OK, null, "좋아요 등록 완료!");
    }

    @Operation(summary = "좋아요", description = "특정 독후감의 좋아요를 삭제합니다.")
    @DeleteMapping("/api/review/{reviewId}/likes")
    public CMRespDto<?> unLike(@RequestParam Long socialId, @PathVariable Long reviewId) {
        likesService.좋아요취소(socialId, reviewId);
        return new CMRespDto<>(HttpStatus.OK, null, "좋아요 취소 완료!");
    }
}
