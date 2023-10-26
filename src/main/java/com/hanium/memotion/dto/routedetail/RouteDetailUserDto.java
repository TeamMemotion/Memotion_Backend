package com.hanium.memotion.dto.routedetail;

import com.hanium.memotion.domain.route.RouteDetail;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RouteDetailUserDto {

    private Long recordDetailId;
    private String title;

    private String start_time;

    private String end_time;

    private String select_date;

    private String content;

    private String place;

    private Double latitude;

    private Double longitude;

    private String url;

    private Long routeId;

    private Long memberId;

    public RouteDetailUserDto(RouteDetail routeDetail, Long memberId){
        this.recordDetailId = routeDetail.getRecordDetailId();
        this.title=routeDetail.getTitle();
        this.start_time= routeDetail.getStart_time();
        this.end_time= routeDetail.getEnd_time();
        this.select_date=routeDetail.getSelect_date();
        this.content=routeDetail.getContent();
        this.place=routeDetail.getPlace();
        this.latitude=routeDetail.getLatitude();
        this.longitude=routeDetail.getLongitude();
        this.url=routeDetail.getUrl();
        this.routeId=routeDetail.getRoute().getRouteId();
        this.memberId=memberId;
    }

}
