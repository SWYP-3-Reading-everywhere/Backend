package com.book_everywhere.domain.tag.controller;

import com.book_everywhere.domain.tag.dto.TagDto;
import com.book_everywhere.domain.tag.service.TaggedService;
import com.book_everywhere.common.dto.CMRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagController {

    private final TaggedService taggedService;
    @GetMapping("/api/tags")
    public CMRespDto<?> findAllTag() {
        List<TagDto> result = taggedService.모든태그조회();
        return new CMRespDto<>(HttpStatus.OK, result, "태그 조회 성공");
    }
}
