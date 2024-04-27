package com.book_everywhere.domain.mark.service;

import com.book_everywhere.domain.mark.dto.BookmarkDto;

import java.util.List;

public interface BookmarkService {

    void 북마크_등록(Long socialId, String address);

    void 북마크_삭제(Long id);

    List<BookmarkDto> 유저_북마크_조회(Long socialId);
}
