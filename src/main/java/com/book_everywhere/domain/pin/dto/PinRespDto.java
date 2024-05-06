package com.book_everywhere.domain.pin.dto;

import com.book_everywhere.domain.pin.entity.Pin;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PinRespDto {
    private String name;
    private String phone;
    private double placeId;
    private double y;
    private double x;
    private String address;
    //3월 2일 추가 공유지도
    private boolean isPrivate;
    private String url;

    public Pin toEntity() {
        return Pin.builder()
                .placeId(placeId)
                .title(name)
                .phone(phone)
                .address(address)
                .longitude(x)
                .latitude(y)
                .url(url)
                .build();
    }
}
