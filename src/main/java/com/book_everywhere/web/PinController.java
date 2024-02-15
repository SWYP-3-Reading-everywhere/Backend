package com.book_everywhere.web;

import com.book_everywhere.domain.pin.Pin;
import com.book_everywhere.service.PinService;
import com.book_everywhere.web.dto.CMRespDto;
import com.book_everywhere.web.dto.pin.PinDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class PinController {

    private final PinService pinService;

    @GetMapping("/pin")
    public CMRespDto<?> pin() {
        List<Pin> result = pinService.전체핀조회();
        return new CMRespDto<>(1, result);
    }

    @GetMapping("/pin/{id}")
    public CMRespDto<?> pinDetails(@PathVariable int id) {
        Pin result = pinService.단일핀조회(id);
        return new CMRespDto<>(1, result);
    }

    @PostMapping("/pin")
    public CMRespDto<?> createPin(PinDto pinDto) {
        pinService.핀생성(pinDto);
        return new CMRespDto<>(1, null);
    }

}
