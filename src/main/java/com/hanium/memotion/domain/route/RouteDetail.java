package com.hanium.memotion.domain.route;

import com.hanium.memotion.domain.core.BaseTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Table(name = "RouteDetail")
public class RouteDetail extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recordDetail_id")
    private Long recordDetailId;

    @Column(name = "title")
    @NotNull
    private String title;

    @Column(name = "start_time")
    @NotNull
    private LocalDateTime start_time;

    @Column(name = "end_time")
    @NotNull
    private LocalDateTime end_time;

    @Column(name = "select_date")
    @NotNull
    private LocalDate select_date;

    @Column(name = "content", length = 500)
    private String content;

    @Column(name = "place", length = 500)
    @NotNull
    private String place;

    @Column(name = "latitude")
    @NotNull
    private Double latitude;

    @Column(name = "longitude")
    @NotNull
    private Double longitude;

    @Column(name = "url", length = 500, unique = true)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;
}
