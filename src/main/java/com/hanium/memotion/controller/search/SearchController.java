package com.hanium.memotion.controller.search;

import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.dto.search.request.SearchReqDto;
import com.hanium.memotion.dto.search.response.SearchResDto;
import com.hanium.memotion.exception.base.BaseResponse;
import com.hanium.memotion.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;

    // 검색 (default or 검색어 + 최신순 or 검색어 + 오래된순)
    @GetMapping("")
    public BaseResponse<List<SearchResDto>> getSearchList(@RequestBody SearchReqDto searchReqDto) {
        List<SearchResDto> searchResDto = searchService.getSearchList(searchReqDto);

        return BaseResponse.onSuccess(searchResDto);
    }
}
