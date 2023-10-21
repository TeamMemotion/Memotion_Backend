package com.hanium.memotion.controller.route;

import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.domain.route.RouteDetail;
import com.hanium.memotion.dto.routedetail.RouteDetailDto;
import com.hanium.memotion.exception.base.BaseResponse;
import com.hanium.memotion.service.route.RouteDetailService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/route")
@RequiredArgsConstructor
public class RouteDetailController {

    private final RouteDetailService routeDetailService;
    //TODO
    // - 루트기록 상세
    //    - 제목, 좋아요, 여행 기간 및 날짜들 조회 api 1개
    //    - 각 날짜별 첫 등록 사진, 제목, 장소, 시작시간, 종료 시간 조회 리스트 api 1개
    // - 루트 일정 추가 및 조회 (루트 기록 상세 안에 있는 리스트 한 개 누르거나 추가할때)
    //    - 저장 api 1개(날짜, 제목, 시작시간, 종료시간, 메모, 장소, 사진)
    //    - 수정 api 1개
    //    - 삭제 api 1개
    @ApiOperation(
            value = "루트 디테일 저장 api"
            , notes = "날짜, 제목, 시작시간, 종료시간, 메모, 장소, 사진 저장.")
    @PostMapping("/save")
    public BaseResponse<RouteDetailDto.Response> save(@RequestBody RouteDetailDto.Request request, @AuthenticationPrincipal Member member){

        RouteDetail routeDetail = routeDetailService.save(request);
        return BaseResponse.onSuccess(new RouteDetailDto.Response(routeDetail));
    }
    @ApiOperation(
            value = "루트 디테일 수정 api"
            , notes = "날짜, 제목, 시작시간, 종료시간, 메모, 장소, 사진 수정, routeDetailId를 PathVariable로 보내줘야행")
    @PatchMapping("/update/{routeDetailId}")
    public BaseResponse<RouteDetailDto.Response> update(@PathVariable("routeDetailId") Long routeDetailId, @RequestBody RouteDetailDto.Request request, @AuthenticationPrincipal Member member){
        RouteDetail routeDetail = routeDetailService.update(request,routeDetailId);
        return BaseResponse.onSuccess(new RouteDetailDto.Response(routeDetail));
    }
}
