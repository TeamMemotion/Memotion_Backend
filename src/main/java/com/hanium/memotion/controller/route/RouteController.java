package com.hanium.memotion.controller.route;

import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.dto.route.request.RouteReqDto;
import com.hanium.memotion.dto.route.response.LocalGuideResDto;
import com.hanium.memotion.dto.route.response.RouteResDto;
import com.hanium.memotion.exception.base.BaseResponse;
import com.hanium.memotion.service.route.RouteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/route")
@Api(tags = {"Route API"})
public class RouteController {
    private final RouteService routeService;

    // 내가 작성한 루트 기록 조회 -> 루트 기록 + 로컬 가이드 메인 화면
    @ApiOperation(value = "내가 작성한 루트 기록 조회", notes = "내가 작성한 모든 루트 기록을 조회하여 리스트 형태로 반환")
    @GetMapping("")
    public BaseResponse<List<RouteResDto>> getRouteList(@AuthenticationPrincipal Member member) {
        List<RouteResDto> routeResDto = routeService.getRouteList(member);
        return BaseResponse.onSuccess(routeResDto);
    }

    // 루트 추가 -> 루트 기록 + 로컬 가이드 메인 화면
    @ApiOperation(value = "루트 저장", notes = "루트 추가 성공 시 RouteId를 반환")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "routeReqDto", value = "이름, 시작일, 종료일, 지역을 RequestBody 형태로 작성")
    })
    @PostMapping("")
    public BaseResponse<RouteResDto.PostResponse>  postRoute(@AuthenticationPrincipal Member member, @RequestBody @Valid RouteReqDto routeReqDto) {
        RouteResDto.PostResponse postResponse = routeService.postRoute(member, routeReqDto);
        return BaseResponse.onSuccess(postResponse);
    }

    // 로컬 가이드 조회 (최신순) -> 루트기록 + 로컬 가이드 메인 화면 and 로컬 가이드 화면
    @ApiOperation(value = "로컬 가이드 조회(최신순)", notes = "최신순으로 루트 기록을 조회하여 리스트 형태로 반환")
    @GetMapping("/localGuide")
    public BaseResponse<List<LocalGuideResDto>> getLocalGuideList(@AuthenticationPrincipal Member member) {
        List<LocalGuideResDto> localGuideResDto = routeService.getLocalGuideList(member);
        return BaseResponse.onSuccess(localGuideResDto);
    }

    // 로컬가이드 조회 (인기 지역) -> 로컬 가이드 화면
    @ApiOperation(value = "로컬 가이드 조회(인기 지역)", notes = "선택한 인기 지역에 해당하는 루트 기록을 조회하여 리스트 형태로 반환")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "latitude", value = "조회하고자 하는 지역의 위도를 RequestParam 형태로 작성"),
            @ApiImplicitParam(name = "longitude", value = "조회하고자 하는 지역의 경도를 RequestParam 형태로 작성"),
    })
    @GetMapping("/localGuide/popular")
    public BaseResponse<List<LocalGuideResDto>> getLocalGuideListByPopularRegion(@AuthenticationPrincipal Member member, @RequestParam Double latitude, @RequestParam Double longitude) {
        List<LocalGuideResDto> localGuideResDto = routeService.getLocalGuideListByPopularRegion(member, latitude, longitude);
        return BaseResponse.onSuccess(localGuideResDto);
    }

    // 로컬가이드 조회 (검색어) -> 로컬 가이드 화면
    @ApiOperation(value = "로컬 가이드 조회(지역)", notes = "선택한 지역에 해당하는 루트 기록을 조회하여 리스트 형태로 반환")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "latitude", value = "조회하고자 하는 지역의 위도를 RequestParam 형태로 작성"),
            @ApiImplicitParam(name = "longitude", value = "조회하고자 하는 지역의 경도를 RequestParam 형태로 작성"),
    })
    @GetMapping("/localGuide/search")
    public BaseResponse<List<LocalGuideResDto>> getLocalGuideListByRegion(@AuthenticationPrincipal Member member, @RequestParam Double latitude, @RequestParam Double longitude) {
        List<LocalGuideResDto> localGuideResDto = routeService.getLocalGuideListByRegion(member, latitude, longitude);
        return BaseResponse.onSuccess(localGuideResDto);
    }
}
