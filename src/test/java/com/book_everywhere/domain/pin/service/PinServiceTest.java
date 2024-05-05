package com.book_everywhere.domain.pin.service;

import com.book_everywhere.domain.pin.dto.PinRespDtoTestBuilder;
import com.book_everywhere.domain.post.dto.PostReqDto;
import com.book_everywhere.domain.post.dto.PostReqDtoTestBuilder;
import com.book_everywhere.domain.pin.dto.PinDto;
import com.book_everywhere.domain.pin.dto.PinRespDto;
import com.book_everywhere.domain.pin.entity.Pin;
import com.book_everywhere.domain.pin.repository.PinRepository;
import com.book_everywhere.domain.review.dto.PostRespDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PinServiceTest {
    @MockBean
    private PinRepository pinRepository;

    @Autowired
    private PinServiceImpl pinServiceImpl;

    @DisplayName("Service_전체지도조회_테스트")
    @Test
    public void 전체지도조회_테스트() {
        List<Pin> mockPins = Arrays.asList(
                PinRespDtoTestBuilder.createDefault().toEntity(),
                PinRespDtoTestBuilder.createDefault().toEntity()
        );

        when(pinRepository.mFindAllPin()).thenReturn(mockPins);

        // when
        List<PinDto> resultDto = pinServiceImpl.전체지도조회();

        // then
        assertNotNull(resultDto);
        assertEquals(2, resultDto.size());
    }
    @DisplayName("Service_핀생성_테스트_새_핀_생성")
    @Test
    public void 핀생성_새핀생성() {
        // given
        PinRespDto pinRespDto = PinRespDtoTestBuilder.createDefault();
        PostReqDto postReqDto = PostReqDtoTestBuilder.createDefault(pinRespDto);
        when(pinRepository.mFindPinByAddress(anyString())).thenReturn(null); // 아직 핀이 없는 주소라고 가정

        // when
        pinServiceImpl.핀생성포스트(postReqDto);

        // then
        verify(pinRepository).save(any(Pin.class));
    }

    @DisplayName("Service_핀생성_테스트_기존_핀_재사용")
    @Test
    public void 핀생성_기존핀재사용() {
        // given
        PinRespDto pinRespDto = PinRespDtoTestBuilder.createDefault();
        PostReqDto postRespDto = PostReqDtoTestBuilder.createDefault(pinRespDto);
        when(pinRepository.mFindPinByAddress(anyString())).thenReturn(pinRespDto.toEntity());

        // when
        pinServiceImpl.핀생성포스트(postRespDto);

        // then
        verify(pinRepository, never()).save(any(Pin.class));
    }

    @DisplayName("Service_나만의_지도_조회_테스트")
    @Test
    public void 나만의지도조회_테스트() {
        // given
        Long userId = 1L;
        List<Pin> mockPins = Arrays.asList(
                PinRespDtoTestBuilder.createDefault().toEntity(),
                PinRespDtoTestBuilder.createDefault().toEntity()
        );

        when(pinRepository.mUserMap(userId)).thenReturn(mockPins);

        // when
        List<PinDto> resultDto = pinServiceImpl.나만의지도조회(userId);

        // then
        assertNotNull(resultDto);
        assertEquals(2, resultDto.size());
        assertEquals(mockPins.get(0).getTitle(), resultDto.get(0).getTitle());
        assertEquals(mockPins.get(1).getTitle(), resultDto.get(1).getTitle());
    }

}
