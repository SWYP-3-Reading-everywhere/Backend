package com.book_everywhere.domain.auth.dto;

import com.book_everywhere.domain.auth.entity.Role;
import com.book_everywhere.domain.auth.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Getter
@Builder
@Slf4j
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private Long socialId;
    private String nickname;
    private String image;
    private Role role;

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return ofKakao("id", attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        Long socialId = ((Number) attributes.get(userNameAttributeName)).longValue(); // Change this line


        return OAuthAttributes.builder()
                .nickname((String) properties.get("nickname"))
                .image((String) properties.get("profile_image"))
                .socialId(socialId)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .nickname(nickname)
                .image(image)
                .socialId(socialId)
                .role(Role.ROLE_MEMBER)
                .build();
    }

}