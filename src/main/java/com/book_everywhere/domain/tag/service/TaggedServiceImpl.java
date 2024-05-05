package com.book_everywhere.domain.tag.service;

import com.book_everywhere.common.exception.customs.CustomErrorCode;
import com.book_everywhere.common.exception.customs.EntityNotFoundException;
import com.book_everywhere.domain.auth.entity.User;
import com.book_everywhere.domain.auth.repository.UserRepository;
import com.book_everywhere.domain.pin.entity.Pin;
import com.book_everywhere.domain.pin.repository.PinRepository;
import com.book_everywhere.domain.review.dto.ReviewRespDto;
import com.book_everywhere.domain.tag.dto.TagDto;
import com.book_everywhere.domain.tag.entity.Tag;
import com.book_everywhere.domain.tag.entity.Tagged;
import com.book_everywhere.domain.tag.repository.TagRepository;
import com.book_everywhere.domain.tag.repository.TaggedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TaggedServiceImpl implements TaggedService {
    private final TagRepository tagRepository;
    private final TaggedRepository taggedRepository;
    private final PinRepository pinRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void 태그등록(ReviewRespDto reviewRespDto) {
        List<String> tags = reviewRespDto.getTags();
        User user = userRepository.findBySocialId(reviewRespDto.getSocialId()).orElseThrow(() -> new EntityNotFoundException(CustomErrorCode.USER_NOT_FOUND));

        if (tags.isEmpty()) {
            return;
        }
        for (String tagResp : tags) {
            Tag tag = tagRepository.mFindTagByName(tagResp);
            Pin pin = pinRepository.mFindPinByAddress(reviewRespDto.getPinRespDto().getAddress());

            Tagged tagged = taggedRepository.mFindTagged(tag.getId(), pin.getId());
            if (tagged == null) {
                Tagged newTagged = Tagged.builder()
                        .pin(pin)
                        .tag(tag)
                        .user(user)
                        .build();
                taggedRepository.save(newTagged);
            }
        }
    }

    @Override
    public List<TagDto> 모든태그조회() {
        List<Tag> tags = tagRepository.findAllWithCategory();
        return tags.stream().map(tag -> new TagDto(
                tag.getContent(),
                false,
                tag.getCategory().getName())).toList();
    }

    @Override
    @Transactional
    public void 태그삭제(String address, Long socialId) {
        Pin pin = pinRepository.mFindPinByAddress(address);
        if (pin == null) {
            throw new EntityNotFoundException(CustomErrorCode.PIN_NOT_FOUND);
        }
        taggedRepository.deleteAllByPinAndUser(pin.getId(), socialId);
    }
}
