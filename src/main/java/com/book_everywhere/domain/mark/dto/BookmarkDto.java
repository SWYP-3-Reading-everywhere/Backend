package com.book_everywhere.domain.mark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookmarkDto {
    Long id;
    Long pinId;
}
