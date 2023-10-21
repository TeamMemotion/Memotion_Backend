package com.hanium.memotion.domain.route;

import com.hanium.memotion.domain.core.BaseTime;
import com.hanium.memotion.domain.member.Member;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Table(name = "Route")
@Entity
public class Route extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Long routeId;

    @Column(name = "route_name")
    @NotNull
    private String name;

    @Column(name = "start_date")
    @NotNull
    private LocalDate startDate;

    @Column(name = "end_date")
    @NotNull
    private LocalDate endDate;

    @Column(name = "url", length = 500, unique = true)
    private String url;

    @Column(name = "region")
    @NotNull
    private String region;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "route")
    private List<RouteDetail> routeDetails = new ArrayList<>();

    @Builder
    public Route(String name, String startDate, String endDate, String region, Member member) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        this.name = name;
        this.startDate = LocalDate.parse(startDate, formatter);
        this.endDate = LocalDate.parse(endDate, formatter);
        this.region = region;
        this.member = member;
    }
}
