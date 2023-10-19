package com.hanium.memotion.domain.route;

import com.hanium.memotion.domain.core.BaseTime;
import com.hanium.memotion.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
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
    private LocalDateTime start_date;

    @Column(name = "end_date")
    @NotNull
    private LocalDateTime end_date;

    @Column(name = "content", length = 500)
    private String content;

    @Column(name = "url", length = 500, unique = true)
    private String url;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "route")
    private List<RouteDetail> route = new ArrayList<>();
}
