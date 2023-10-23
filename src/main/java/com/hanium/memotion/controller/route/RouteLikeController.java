package com.hanium.memotion.controller.route;

import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.domain.route.RouteDetail;
import com.hanium.memotion.domain.route.RouteLike;
import com.hanium.memotion.dto.route.response.RouteResDto;
import com.hanium.memotion.dto.routedetail.RouteDetailDto;
import com.hanium.memotion.exception.base.BaseResponse;
import com.hanium.memotion.service.route.RouteLikeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/route-like")
@RequiredArgsConstructor
public class RouteLikeController {
    private final RouteLikeService routeLikeService;

    //TODO
    //  - 저장 api 1개
    //  - 관심루트 (내가 좋아요 누른 관심 루트 기록) 조회
    //  - 삭제 api 1개

    @ApiOperation(
            value = "좋아요 저장 api"
            , notes = "routeId랑 member 정보 .")
    @GetMapping("/save/{routeId}")
    public BaseResponse<RouteLike> save(@PathVariable("routeId") Long routeId, @AuthenticationPrincipal Member member){

        RouteLike routeLike = routeLikeService.save(routeId, member);
        return BaseResponse.onSuccess(routeLike);
    }
    @ApiOperation(
            value = "좋아요 삭제 api"
            , notes = "routeId랑 member 정보 .")
    @DeleteMapping("/delete/{routeId}")
    public BaseResponse<Integer> delete (@PathVariable("routeId") Long routeId, @AuthenticationPrincipal Member member) throws ParseException {
        return BaseResponse.onSuccess(routeLikeService.delete(routeId, member));
    }

    @ApiOperation(
            value = "좋아요 전체 조회 api"
            , notes = "로그인 한 유저가 좋아요 눌러둔 모든 루트 반환.")
    @GetMapping("")
    public BaseResponse<List<RouteResDto>> getRouteLikeList(@AuthenticationPrincipal Member member) {
        List<RouteResDto> routeResDto = routeLikeService.getRouteLikeList(member);
        return BaseResponse.onSuccess(routeResDto);
    }

}
