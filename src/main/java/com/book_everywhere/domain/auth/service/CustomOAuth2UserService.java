package com.book_everywhere.domain.auth.service;

import com.book_everywhere.domain.auth.dto.CustomOAuth2User;
import com.book_everywhere.domain.auth.dto.OAuthAttributes;
import com.book_everywhere.domain.auth.dto.UserDto;
import com.book_everywhere.domain.auth.entity.Role;
import com.book_everywhere.domain.auth.entity.User;
import com.book_everywhere.domain.auth.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final HttpSession httpSession;
    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        logger.info("registrationId: {}", registrationId);
        logger.info("userNameAttributeName: {}", userNameAttributeName);
        logger.info("attributes: {}", attributes);

        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", user);
        UserDto userDto = new UserDto();
        userDto.setNickname(user.getNickname());
        userDto.setRole(String.valueOf(user.getRole()));

        return new CustomOAuth2User(userDto);
    }


    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findBySocialId(attributes.getSocialId())
                .map(entity -> entity.update(attributes.getNickname(),attributes.getImage(), Role.ROLE_MEMBER))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}