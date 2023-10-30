package com.hanium.memotion.repository;

import com.hanium.memotion.domain.route.RouteDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteDetailRepository extends JpaRepository<RouteDetail,Long> {
    RouteDetail findByRecordDetailId(Long id);
    List<RouteDetail> findByRouteId(Long id);

    List<RouteDetail> findBySelect_dateAndRoute_Id(String selectDate, Long id);
}
