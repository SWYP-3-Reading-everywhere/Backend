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
    //#@!S3 연동하는 코드가 필요합니다. postImageRepository 부분이 필요합니다

    @Override
    @Transactional
    public void 장소_리뷰_생성(PostReqDto postReqDto) {
        User user = userRepository.findBySocialId(postReqDto.getSocialId())
                .orElseThrow(() -> new EntityNotFoundException(CustomErrorCode.USER_NOT_FOUND));
        Pin pin = pinRepository.mFindPinByAddress(postReqDto.getPinRespDto().getAddress());
        if(pin == null) {
            throw new EntityNotFoundException(CustomErrorCode.PIN_NOT_FOUND);
        }
        Post post = postReqDto.toEntity(user, pin); //#@! 이미지 관련 코드가 필요합니다
        postRepository.save(post);
    }

    @Override
    public List<PostRespDto> 모든_장소_리뷰_조회() {
        List<Post> init = postRepository.findAll();
        return init.stream().map(post -> post.toRespDto(post.getPin().toRespDto())).toList();
    }

    @Override
    public PostRespDto 장소_리뷰_조회(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(CustomErrorCode.REVIEW_NOT_FOUND));
        String address = post.getPin().getAddress();
        Pin pin = pinRepository.mFindPinByAddress(address);
        PinRespDto pinRespDto = pin.toRespDto();
        return post.toRespDto(pinRespDto);
    }

    @Override
    public List<PostRespDto> 유저의_모든_장소_리뷰_조회(Long socialId) {
        List<Post> init = postRepository.mFindAllBySocialId(socialId);
        return init.stream().map(post -> {
            Pin pin = post.getPin();
            PinRespDto pinRespDto = pin.toRespDto();
            return post.toRespDto(pinRespDto);
        }).toList();
    }

    @Override
    public List<PostRespDto> 장소의_모든_리뷰_조회(String address) {
        List<Post> init = postRepository.mFindAllByPinAddress(address);
        return init.stream().map(post -> post.toRespDto(post.getPin().toRespDto())).toList();
    }
}
