package com.book_everywhere.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class CountDto<T> {
    private List<T> data;
    private int count;

    public CountDto(List<T> data) {
        this.data = data;
        this.count = data.size();
    }
}
