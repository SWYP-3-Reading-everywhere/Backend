package com.book_everywhere.domain.mark.dto;

import com.book_everywhere.domain.pin.dto.PinRespDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookmarkDto {
    PinRespDto pinRespDto;
}
