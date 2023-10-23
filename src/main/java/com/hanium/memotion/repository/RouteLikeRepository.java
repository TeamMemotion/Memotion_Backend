package com.hanium.memotion.repository;

import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.domain.route.Route;
import com.hanium.memotion.domain.route.RouteLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteLikeRepository extends JpaRepository<RouteLike, Long> {
    Long countByRoute(Route route);

    Optional<RouteLike> findByRouteAndMember(Route route, Member member);

    List<RouteLike> findAllByMember(Member member);

}
