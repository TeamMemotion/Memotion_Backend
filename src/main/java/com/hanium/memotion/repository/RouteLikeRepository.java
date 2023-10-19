package com.hanium.memotion.repository;

import com.hanium.memotion.domain.route.Route;
import com.hanium.memotion.domain.route.RouteLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteLikeRepository extends JpaRepository<RouteLike, Long> {
    Long countByRoute(Route route);
}
