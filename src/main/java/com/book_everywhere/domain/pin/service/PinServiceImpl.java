package com.book_everywhere.domain.pin.service;

import com.book_everywhere.domain.pin.entity.Pin;
import com.book_everywhere.domain.pin.repository.PinRepository;
import com.book_everywhere.domain.tag.repository.TaggedRepository;
import com.book_everywhere.domain.pin.dto.PinDto;
import com.book_everywhere.domain.pin.dto.PinRespDto;
import com.book_everywhere.domain.pin.dto.PinWithTagCountRespDto;
import com.book_everywhere.domain.review.dto.ReviewRespDto;
import com.book_everywhere.domain.tag.dto.TagCountRespDto;
import com.book_everywhere.common.exception.customs.CustomErrorCode;
import com.book_everywhere.common.exception.customs.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PinServiceImpl implements PinService {
    private final PinRepository pinRepository;
    private final TaggedRepository taggedRepository;

    //DTO 변환단계
    public List<PinDto> 전체지도조회() {
        List<Pin> init = pinRepository.mFindAllPin();

        return init.stream()
                .map(PinDto::toDto)
                .toList();
    }
    @Transactional
    public void 핀생성(ReviewRespDto reviewRespDto) {
        PinRespDto pinRespDto = reviewRespDto.getPinRespDto();
        Pin pined = pinRepository.mFindPinByAddress(reviewRespDto.getPinRespDto().getAddress());
        if (pined == null) {
            Pin pin = pinRespDto.toEntity();
            pinRepository.save(pin);
        }
    }

    public List<PinDto> 나만의지도조회(Long userId) {
        List<Pin> init = pinRepository.mUserMap(userId);

        return init.stream()
                .map(PinDto::toDto)
                .toList();
    }


    public List<PinDto> 태그조회(String tagContent) {
        List<Pin> init = pinRepository.mFindTaggedPin(tagContent);
        if (init.isEmpty()) {
            throw new EntityNotFoundException(CustomErrorCode.PIN_NOT_FOUND);
        }

        return init.stream()
                .map(PinDto::toDto)
                .toList();
    }


    public List<PinWithTagCountRespDto> 핀의상위4개태그개수와함께조회() {
        List<Pin> pins = pinRepository.findAll();

        return pins.stream().map(pin -> {
            List<Object[]> taggeds = taggedRepository.mCountByPinId(pin.getId());
            List<TagCountRespDto> tagCountRespDtos = taggeds.stream()
                    .map(tagged -> new TagCountRespDto(
                            (String) tagged[0],
                            (Long) tagged[1])).toList();
            return PinWithTagCountRespDto.toDto(pin, tagCountRespDtos);
        }).toList();
    }

    public List<PinWithTagCountRespDto> 공유또는개인핀의상위5개태그개수와함께조회(boolean pinIsPrivate) {
        List<Pin> pins = pinRepository.mFindPinByIsPrivate(pinIsPrivate);
        return pins.stream().map(pin -> {
            List<Object[]> taggeds = taggedRepository.mCountByPinId(pin.getId());
            List<TagCountRespDto> tagCountRespDtos = taggeds.stream()
                    .map(tagged -> new TagCountRespDto(
                            (String) tagged[0],
                            (Long) tagged[1])).toList();
            return PinWithTagCountRespDto.toDto(pin, tagCountRespDtos);
        }).toList();
    }
}
