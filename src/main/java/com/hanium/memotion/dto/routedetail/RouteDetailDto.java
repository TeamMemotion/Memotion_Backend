package com.hanium.memotion.dto.routedetail;

import com.hanium.memotion.domain.route.Route;
import com.hanium.memotion.domain.route.RouteDetail;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
public class RouteDetailDto {

    @NoArgsConstructor
    @Data
    public static class Request {
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

        public RouteDetail toEntity(Route route){
            return RouteDetail.builder()
                    .title(title)
                    .start_time(start_time)
                    .end_time(end_time)
                    .select_date(select_date)
                    .content(content)
                    .place(place)
                    .latitude(latitude)
                    .longitude(longitude)
                    .url(url)
                    .route(route)
                    .build();

        }

        public Request(RouteDetail routeDetail){
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
        }
    }
    @NoArgsConstructor
    @Data
    public static class Response {
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


        public Response(RouteDetail routeDetail){
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
        }
    }

}
