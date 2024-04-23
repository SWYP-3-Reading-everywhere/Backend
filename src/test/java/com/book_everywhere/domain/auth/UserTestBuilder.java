package com.book_everywhere.domain.auth;


import com.book_everywhere.domain.auth.entity.Role;
import com.book_everywhere.domain.auth.entity.User;

import java.util.ArrayList;

public class UserTestBuilder {

    public static User createDefault() {
        return User.builder()
                .id(123456L)
                .socialId(123L) // 가짜 socialId 설정
                .nickname("testUser") // 가짜 닉네임 설정
                .image("default_image.jpg") // 가짜 이미지 경로 설정
                .role(Role.ROLE_MEMBER) // 가짜 역할 설정
                .visits(new ArrayList<>()) // 빈 visit 리스트 설정
                .taggeds(new ArrayList<>()) // 빈 tagged 리스트 설정
                .reviews(new ArrayList<>()) // 빈 review 리스트 설정
                .build();
    }
    public static User createMany(Long add) {
        return User.builder()
                .id(123456L+add)
                .socialId(123L+add) // 가짜 socialId 설정
                .nickname("testUser"+add) // 가짜 닉네임 설정
                .image("default_image.jpg") // 가짜 이미지 경로 설정
                .role(Role.ROLE_MEMBER) // 가짜 역할 설정
                .visits(new ArrayList<>()) // 빈 visit 리스트 설정
                .taggeds(new ArrayList<>()) // 빈 tagged 리스트 설정
                .reviews(new ArrayList<>()) // 빈 review 리스트 설정
                .build();
    }
}