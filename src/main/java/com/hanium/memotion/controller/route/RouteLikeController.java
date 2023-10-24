package com.hanium.memotion.controller.route;

import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.domain.route.Route;
import com.hanium.memotion.domain.route.RouteDetail;
import com.hanium.memotion.domain.route.RouteLike;
import com.hanium.memotion.dto.routedetail.RouteDetailDto;
import com.hanium.memotion.exception.base.BaseException;
import com.hanium.memotion.exception.base.BaseResponse;
import com.hanium.memotion.exception.base.ErrorCode;
import com.hanium.memotion.service.route.RouteLikeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintViolationException;
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
    public BaseResponse<Long> save(@PathVariable("routeId") Long routeId, @AuthenticationPrincipal Member member) {
        System.out.println(routeId);
        System.out.println(member.getUsername());
        RouteLike routeLike = routeLikeService.savelike(routeId, member);
        return BaseResponse.onSuccess(routeLike.getRoute().getRouteId());
    }
    @ApiOperation(
            value = "좋아요 삭제 api"
            , notes = "routeId랑 member 정보 .")
    @DeleteMapping("/delete/{routeId}")
    public BaseResponse<Integer> delete (@PathVariable("routeId") Long routeId, @AuthenticationPrincipal Member member) throws ParseException {
        return BaseResponse.onSuccess(routeLikeService.delete(routeId, member));
    }

    @ApiOperation(
            value = "마이페이지에서 내가 누른 좋아요 리스트 API"
    )
    @GetMapping("/route-like/list")
    public BaseResponse<List<Route>> likedRouteList(@AuthenticationPrincipal Member member){
            List<Route> result = routeLikeService.findByUserRouteLike(member);
            return BaseResponse.onSuccess(result);
    }

}
