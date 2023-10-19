package com.hanium.memotion.dto.route.response;

import com.hanium.memotion.domain.route.Route;
import lombok.Builder;
import lombok.Data;

@Data
public class LocalGuideResDto {
    private Long routeId;
    private String routeImg;
    private String profileImg;
    private String username;

    @Builder
    public LocalGuideResDto(Route r) {
        this.routeId = r.getRouteId();
        this.routeImg = r.getUrl();
        this.profileImg = r.getMember().getImage();
        this.username = r.getMember().getUsername();
    }
}
