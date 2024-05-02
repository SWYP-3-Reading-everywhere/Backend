package com.book_everywhere.domain.post.service;

import com.book_everywhere.common.exception.customs.CustomErrorCode;
import com.book_everywhere.common.exception.customs.EntityNotFoundException;
import com.book_everywhere.domain.auth.entity.User;
import com.book_everywhere.domain.auth.repository.UserRepository;
import com.book_everywhere.domain.pin.entity.Pin;
import com.book_everywhere.domain.pin.repository.PinRepository;
import com.book_everywhere.domain.post.dto.PostReqDto;
import com.book_everywhere.domain.post.entity.Post;
import com.book_everywhere.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PinRepository pinRepository;

    public void 장소리뷰생성하기(PostReqDto postReqDto) {
        User user = userRepository.findBySocialId(postReqDto.getSocialId())
                .orElseThrow(() -> new EntityNotFoundException(CustomErrorCode.USER_NOT_FOUND));
        Pin pin = pinRepository.mFindPinByAddress(postReqDto.getPinRespDto().getAddress());
        if(pin == null) {
            throw new EntityNotFoundException(CustomErrorCode.PIN_NOT_FOUND);
        }
        Post post = postReqDto.toEntity(user, pin);
        postRepository.save(post);
    }
}
