package com.hanium.memotion.repository;

import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.domain.route.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findAllByOrderByCreatedAtDesc();

    List<Route> findAllByMemberOrderByCreatedAtDesc(Member member);

    @Query(value = "SELECT *\n" +
            "FROM route r\n" +
            "WHERE r.route_id in (\n" +
            "\tSELECT distinct rd.route_id\n" +
            "\tFROM (\n" +
            "\t\tSELECT *, (6371 * acos(cos(radians(:lat)) * cos(radians(latitude) ) * cos(radians(longitude) - radians(:lon)) + sin(radians(:lat)) * sin(radians(latitude)))) AS distance\n" +
            "\t\tFROM route_detail\n" +
            "\t) AS rd\n" +
            "\tWHERE rd.distance <= 15\n" +
            "\tORDER BY rd.distance, rd.created_at DESC\n" +
            ")", nativeQuery = true)
    List<Route> findAllByPopularRegion(@Param("lat") Double lat, @Param("lon") Double lon);

    @Query(value = "SELECT *\n" +
            "FROM route r\n" +
            "WHERE r.route_id in (\n" +
            "\tSELECT distinct d.route_id\n" +
            "\tFROM (\n" +
            "\t\tSELECT *, (6371 * acos(cos(radians(:lat)) * cos(radians(latitude) ) * cos(radians(longitude) - radians(:lon)) + sin(radians(:lat)) * sin(radians(latitude)))) AS distance\n" +
            "\t\tFROM route_detail\n" +
            "\t) AS d\n" +
            "\tWHERE d.distance <= 0.005\n" +
            "\tORDER BY d.distance, d.created_at DESC\n" +
            ");", nativeQuery = true)
    List<Route> findAllByRegion(@Param("lat") Double lat, @Param("lon") Double lon);

    List<Route> getRouteLikeMembers(Long[] ids);
}
