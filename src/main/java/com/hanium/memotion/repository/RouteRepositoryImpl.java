package com.hanium.memotion.repository;

import com.hanium.memotion.domain.route.Route;
import com.hanium.memotion.domain.route.RouteLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hanium.memotion.domain.route.QRoute.route;

@Repository
public class RouteRepositoryImpl extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public RouteRepositoryImpl(JPAQueryFactory jpaQueryFactory){
        super(Route.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }
    public List<Route> getRouteLikeMembers(Long[] ids) {
        return jpaQueryFactory
                .select(route)
                .from(route)
                .where(route.routeId.in(ids))
                .fetch();
    }

}
