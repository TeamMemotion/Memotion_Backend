package com.hanium.memotion.controller.route;

import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.dto.route.request.RouteReqDto;
import com.hanium.memotion.dto.route.response.LocalGuideResDto;
import com.hanium.memotion.dto.route.response.RouteResDto;
import com.hanium.memotion.exception.base.BaseResponse;
import com.hanium.memotion.service.route.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/route")
public class RouteController {
    private final RouteService routeService;

    // 루트기록 메인 화면 - 로컬 가이드 조회 (최신순)
    @GetMapping("/localGuide")
    public BaseResponse<List<LocalGuideResDto>> getLocalGuideList() {
        List<LocalGuideResDto> localGuideResDto = routeService.getLocalGuideList();
        return BaseResponse.onSuccess(localGuideResDto);
    }

    // 로컬가이드 조회 (지역)
    @GetMapping("/localGuide/")
    public BaseResponse<List<LocalGuideResDto>> getLocalGuideListByRegion(@PathVariable("region") String region) {
        List<LocalGuideResDto> localGuideResDto = routeService.getLocalGuideListByRegion(region);
        return BaseResponse.onSuccess(localGuideResDto);
    }

    // 루트기록 메인 화면 - 루트 기록 조회
    @GetMapping("")
    public BaseResponse<List<RouteResDto>> getRouteList(@AuthenticationPrincipal Member member) {
        List<RouteResDto> routeResDto = routeService.getRouteList(member);
        return BaseResponse.onSuccess(routeResDto);
    }

    // 루트 추가
    @PostMapping("")
    public BaseResponse<Long>  postRoute(@AuthenticationPrincipal Member member, @RequestBody @Valid RouteReqDto routeReqDto) {
        Long routeId = routeService.postRoute(member, routeReqDto);
        return BaseResponse.onSuccess(routeId);
    }
}
