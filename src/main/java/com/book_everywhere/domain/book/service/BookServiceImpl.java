package com.book_everywhere.domain.book.service;

import com.book_everywhere.common.exception.customs.CustomErrorCode;
import com.book_everywhere.common.exception.customs.EntityNotFoundException;
import com.book_everywhere.domain.book.dto.BookDto;
import com.book_everywhere.domain.book.dto.BookRespDto;
import com.book_everywhere.domain.book.entity.Book;
import com.book_everywhere.domain.book.repository.BookRepository;
import com.book_everywhere.domain.review.dto.ReviewRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    //등록 혹은 수정
    @Transactional
    public void 책생성(ReviewRespDto reviewRespDto) {
        BookRespDto bookRespDto = reviewRespDto.getBookRespDto();
        Book book = bookRepository.mFindBookIsbn(bookRespDto.getIsbn());
        if (book == null) {
            Book newBook = bookRespDto.toEntity();
            bookRepository.save(newBook);
        }
    }

    //조회
    //특정 유저의 모든 책 목록 조회
    public List<BookDto> 유저책조회(Long socialId) {
        List<Book> books = bookRepository.mFindBookByUserId(socialId);
        if (books.isEmpty()) {
            throw new EntityNotFoundException(CustomErrorCode.BOOK_NOT_FOUND);
        }
        return books.stream().map(BookDto::toDto).toList();
    }


    //책 한권 조회
    public BookDto 단일책조회(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(CustomErrorCode.BOOK_NOT_FOUND));
        return BookDto.toDto(book);
    }

    //등록된 모든 책 조회
    public List<BookDto> 모든책조회() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(BookDto::toDto).toList();
    }

}
