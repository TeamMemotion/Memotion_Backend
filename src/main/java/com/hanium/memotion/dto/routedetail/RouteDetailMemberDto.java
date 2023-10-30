package com.hanium.memotion.dto.routedetail;

import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.domain.route.Route;
import com.hanium.memotion.domain.route.RouteDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RouteDetailMemberDto {

    private Long loginMemberId;

    private Long routeMemberId;
    List<RouteDetailDto.Response> routeDetail;

    public RouteDetailMemberDto(Member member, Route route, List<RouteDetailDto.Response> routeDetailList) {
        this.loginMemberId = member.getId();
        this.routeMemberId = route.getMember().getId();
        this.routeDetail = routeDetailList;
    }

}
