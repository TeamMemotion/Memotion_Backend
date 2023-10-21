package com.hanium.memotion.repository;

import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.domain.route.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findTop8ByOrderByCreatedAtDesc();

    List<Route> findAllByMember(Member member);

    List<Route> findAllByRegion(String region);
}
