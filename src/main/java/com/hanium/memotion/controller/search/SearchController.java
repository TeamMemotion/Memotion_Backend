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

    @GetMapping("")
    public BaseResponse<List<SearchResDto>> getSearchList(@AuthenticationPrincipal Member member, @RequestBody SearchReqDto searchReqDto) {
        Double latitude = searchReqDto.getLatitude();
        Double longitude = searchReqDto.getLongitude();
        String filter = searchReqDto.getFilter();

        return BaseResponse.onSuccess(searchService.getSearchList(latitude, longitude, filter));
    }
}
