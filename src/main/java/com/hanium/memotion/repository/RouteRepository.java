package com.hanium.memotion.repository;

import com.hanium.memotion.domain.route.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
