package com.hanium.memotion.repository;

import com.hanium.memotion.domain.route.QRouteDetail;
import com.hanium.memotion.domain.route.RouteDetail;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.hanium.memotion.domain.route.QRouteDetail.routeDetail;

@Repository
public class RouteDetailRepositoryImpl extends QuerydslRepositorySupport{
    private final JPAQueryFactory jpaQueryFactory;

    public RouteDetailRepositoryImpl (JPAQueryFactory jpaQueryFactory){
        super(RouteDetail.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }
    public RouteDetail findByRecordDetailId(Long id) {
        return (RouteDetail) jpaQueryFactory
                .selectFrom(routeDetail)
                .where(routeDetail.recordDetailId.eq(id))
                .fetch();
    }
    public List<RouteDetail> findByRouteId(Long id) {
        return  jpaQueryFactory
                .selectFrom(routeDetail)
                .where(routeDetail.route.routeId.eq(id))
                .fetch();
    }

    public List<RouteDetail> findBySelectDateAndRouteId(String selectDate, Long id) {
        QRouteDetail routeDetail = QRouteDetail.routeDetail; // Q클래스 생성

        JPAQuery<RouteDetail>query = jpaQueryFactory
                .selectFrom(routeDetail)
                .where(
                        routeDetail.select_date.eq(selectDate),
                        routeDetail.route.routeId.eq(id)
                );
        return query.fetch();

    }
    public String findLatestImageUrlByRouteId(Long id) {
        QRouteDetail routeDetail = QRouteDetail.routeDetail;

        RouteDetail latestImage = jpaQueryFactory
                .selectFrom(routeDetail)
                .where(
                        routeDetail.route.routeId.eq(id),
                        routeDetail.url.isNotNull()
                )
                .orderBy(routeDetail.start_time.asc())
                .fetchFirst();

        if (latestImage != null) {
            return latestImage.getUrl();
        } else {
            return null;
        }
    }

}
