package com.book_everywhere.domain.mark.controller;

import com.book_everywhere.common.dto.CMRespDto;
import com.book_everywhere.domain.mark.dto.BookmarkDto;
import com.book_everywhere.domain.mark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @GetMapping("/api/bookmark/{socialId}")
    public CMRespDto<?> findBookmarkWithUser(@PathVariable Long socialId) {
        List<BookmarkDto> result = bookmarkService.유저_북마크_조회(socialId);
        return new CMRespDto<>(HttpStatus.OK, result, "유저 북마크 조회 성공");
    }

    @PostMapping("/api/bookmark")
    public CMRespDto<?> saveBookmark(@RequestParam Long socialId, @RequestParam String address) {
        bookmarkService.북마크_등록(socialId, address);
        return new CMRespDto<>(HttpStatus.OK, null, "유저 북마크 등록 성공");
    }

    @DeleteMapping("/api/bookmark/{bookmarkId}")
    public CMRespDto<?> deleteBookmark(@PathVariable Long bookmarkId) {
        bookmarkService.북마크_삭제(bookmarkId);
        return new CMRespDto<>(HttpStatus.OK, null, "유저 북마크 삭제 성공");
    }
}
