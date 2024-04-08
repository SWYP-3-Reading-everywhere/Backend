package com.book_everywhere.domain.book.service;

import com.book_everywhere.domain.book.dto.BookDto;
import com.book_everywhere.domain.review.dto.ReviewRespDto;

import java.util.List;

public interface BookService {
    List<BookDto> 모든책조회();

    BookDto 단일책조회(Long id);

    List<BookDto> 유저책조회(Long socialId);

    void 책생성(ReviewRespDto reviewRespDto);
}
