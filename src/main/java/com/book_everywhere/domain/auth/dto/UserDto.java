package com.book_everywhere.domain.auth.dto;


import lombok.Data;

@Data
public class UserDto {
    private String nickname;
    private String role;
    private Long socialId;
}
