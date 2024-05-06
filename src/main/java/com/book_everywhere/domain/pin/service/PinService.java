package com.book_everywhere.domain.pin.service;

import com.book_everywhere.domain.pin.dto.PinDto;
import com.book_everywhere.domain.pin.dto.PinWithTagCountRespDto;
import com.book_everywhere.domain.post.dto.PostReqDto;
import com.book_everywhere.domain.review.dto.ReviewRespDto;

import java.util.List;

public interface PinService {
    List<PinDto> 전체지도조회();

    void 핀생성포스트(PostReqDto postReqDto);

    void 핀생성리뷰(ReviewRespDto reviewRespDto);

    List<PinWithTagCountRespDto> 핀의상위4개태그개수와함께조회();

    List<PinDto> 태그조회(String content);

    List<PinWithTagCountRespDto> 공유또는개인핀의상위5개태그개수와함께조회(boolean isPrivate);

    List<PinDto> 나만의지도조회(Long socialId);
}
