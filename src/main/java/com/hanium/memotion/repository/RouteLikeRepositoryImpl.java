package com.hanium.memotion.repository;

import com.hanium.memotion.domain.route.Route;
import com.hanium.memotion.domain.route.RouteDetail;
import com.hanium.memotion.domain.route.RouteLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import static com.hanium.memotion.domain.route.QRouteLike.routeLike;

@Repository
public class RouteLikeRepositoryImpl extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public RouteLikeRepositoryImpl (JPAQueryFactory jpaQueryFactory){
        super(RouteLike.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }
//    public List<Route> findByMemberAndRoute(Long id) {
//        return jpaQueryFactory
//                .selectFrom(routeLike)
//                .where(routeLike.member.id.eq(id))
//                .fetch();
//    }
}
