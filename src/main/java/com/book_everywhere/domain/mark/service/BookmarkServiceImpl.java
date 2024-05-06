package com.book_everywhere.domain.mark.service;

import com.book_everywhere.common.exception.customs.CustomErrorCode;
import com.book_everywhere.common.exception.customs.EntityNotFoundException;
import com.book_everywhere.domain.auth.entity.User;
import com.book_everywhere.domain.auth.repository.UserRepository;
import com.book_everywhere.domain.mark.dto.BookmarkDto;
import com.book_everywhere.domain.mark.entity.Bookmark;
import com.book_everywhere.domain.mark.repository.BookmarkRepository;
import com.book_everywhere.domain.pin.dto.PinRespDto;
import com.book_everywhere.domain.pin.entity.Pin;
import com.book_everywhere.domain.pin.repository.PinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookmarkServiceImpl implements BookmarkService{
    private final BookmarkRepository bookmarkRepository;
    private final PinRepository pinRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void 북마크_등록(Long socialId,String address) {
        Pin pin = pinRepository.mFindPinByAddress(address);
        User user = userRepository.findBySocialId(socialId)
                .orElseThrow(() -> new EntityNotFoundException(CustomErrorCode.USER_NOT_FOUND));
        Bookmark init = Bookmark.builder()
                .user(user)
                .pin(pin)
                .build();
        bookmarkRepository.save(init);
    }

    @Override
    @Transactional
    public void 북마크_삭제(Long id) {
        Bookmark bookmark = bookmarkRepository.findById(id)
                .orElseThrow();
        bookmarkRepository.delete(bookmark);
    }

    @Override
    public List<BookmarkDto> 유저_북마크_조회(Long socialId) {
        List<Bookmark> init = bookmarkRepository.findAllBySocialId(socialId);
        return init.stream().map(bookmark -> new BookmarkDto(bookmark.getPin().toRespDto())).toList();
    }
}
