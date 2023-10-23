package com.hanium.memotion.dto.route.response;

import com.hanium.memotion.domain.route.Route;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    private boolean isLiked;

    @Builder
    public RouteResDto(Route r, boolean isLiked, Long likeCount) {
        this.routeId = r.getRouteId();
        this.routeImg = r.getUrl();
        this.startDate = r.getStartDate();
        this.endDate = r.getEndDate();
        this.name = r.getName();
        this.profileImg = r.getMember().getImage();
        this.username = r.getMember().getUsername();
        this.likeCount = likeCount;
        this.isLiked = isLiked;
    }

    @Data
    public static class PostResponse {
        private Long routeId;
        private String startDate;
        private String endDate;
        private String name;

        public PostResponse(Route r) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String startDate = r.getStartDate().format(formatter);
            String endDate = r.getEndDate().format(formatter);

            this.routeId = r.getRouteId();
            this.startDate = startDate;
            this.endDate = endDate;
            this.name = r.getName();
        }
    }
}
