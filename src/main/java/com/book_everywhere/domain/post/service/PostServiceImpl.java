package com.book_everywhere.domain.post.service;

import com.book_everywhere.common.exception.customs.CustomErrorCode;
import com.book_everywhere.common.exception.customs.EntityNotFoundException;
import com.book_everywhere.domain.auth.entity.User;
import com.book_everywhere.domain.auth.repository.UserRepository;
import com.book_everywhere.domain.pin.dto.PinRespDto;
import com.book_everywhere.domain.pin.entity.Pin;
import com.book_everywhere.domain.pin.repository.PinRepository;
import com.book_everywhere.domain.post.dto.PostReqDto;
import com.book_everywhere.domain.post.dto.PostRespDto;
import com.book_everywhere.domain.post.entity.Post;
import com.book_everywhere.domain.post.entity.PostImage;
import com.book_everywhere.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PinRepository pinRepository;

    @Override
    @Transactional
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

    @Override
    public PostRespDto 장소리뷰조회(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(CustomErrorCode.REVIEW_NOT_FOUND));
        String address = post.getPin().getAddress();
        Pin pin = pinRepository.mFindPinByAddress(address);
        List<String> postImages = post.getPostImage().stream().map(PostImage::getImageUrl).toList();
        PinRespDto pinRespDto = new PinRespDto(
                pin.getTitle(),
                pin.getPhone(),
                pin.getPlaceId(),
                pin.getLatitude(),
                pin.getLongitude(),
                pin.getAddress(),
                true,
                pin.getUrl());
        return new PostRespDto(post.getTitle(), post.getContent(), postImages, pinRespDto, post.isPublishing());
    }

    @Override
    public List<PostRespDto> 유저의모든장소리뷰조회(Long socialId) {
        List<Post> init = postRepository.mFindAllBySocialId(socialId);
        return init.stream().map(post -> {
            Pin pin = post.getPin();
            PinRespDto pinRespDto = pin.toRespDto();
            return post.toRespDto(pinRespDto);
        }).toList();
    }
}
