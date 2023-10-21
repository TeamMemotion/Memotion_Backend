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
    private boolean isLiked;
    private Long likeCount;

    @Builder
    public LocalGuideResDto(Route r, boolean isLiked, Long likeCount) {
        this.routeId = r.getRouteId();
        this.routeImg = r.getUrl();
        this.profileImg = r.getMember().getImage();
        this.username = r.getMember().getUsername();
        this.isLiked = isLiked;
        this.likeCount = likeCount;
    }
}
