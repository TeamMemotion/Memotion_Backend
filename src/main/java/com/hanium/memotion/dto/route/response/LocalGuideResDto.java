package com.hanium.memotion.dto.route.response;

import com.hanium.memotion.domain.route.Route;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class LocalGuideResDto {
    private Long routeId;
    private String routeImg;
    private String routeName;
    private String startDate;
    private String endDate;
    private String profileImg;
    private String username;
    private boolean isLiked;
    private Long likeCount;

    @Builder
    public LocalGuideResDto(Route r, boolean isLiked, Long likeCount) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedStartDate = r.getStartDate().format(formatter);
        String formattedEndDate = r.getEndDate().format(formatter);

        this.routeId = r.getRouteId();
        this.routeImg = r.getUrl();
        this.routeName = r.getName();
        this.startDate = formattedStartDate;
        this.endDate = formattedEndDate;
        this.profileImg = r.getMember().getImage();
        this.username = r.getMember().getUsername();
        this.isLiked = isLiked;
        this.likeCount = likeCount;
    }
}
