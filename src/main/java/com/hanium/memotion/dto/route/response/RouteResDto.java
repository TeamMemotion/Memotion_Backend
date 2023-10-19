package com.hanium.memotion.dto.route.response;

import com.hanium.memotion.domain.route.Route;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RouteResDto {
    private Long routeId;
    private String routeImg;
    private LocalDate startDate;
    private LocalDate endDate;
    private String name;
    private String content;
    private String profileImg;
    private String username;
    private Long likeCount;

    @Builder
    public RouteResDto(Route r, Long likeCount) {
        this.routeId = r.getRouteId();
        this.routeImg = r.getUrl();
        this.startDate = r.getStartDate();
        this.endDate = r.getEndDate();
        this.name = r.getName();
        this.profileImg = r.getMember().getImage();
        this.username = r.getMember().getUsername();
        this.likeCount = likeCount;
    }
}
