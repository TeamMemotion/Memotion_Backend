package com.hanium.memotion.repository;


import com.hanium.memotion.domain.diary.Diary;
import com.hanium.memotion.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary,Long> {


    List<Diary> findDiaryByCreatedDateAndMemberId(String createdDate, Member memberId);
    List<Diary> findByCreatedDateLikeAndMemberId(String createdDate, Member member);

    List<Diary> findByMemberId(Member user);

    Diary findByDiaryId(Long DiaryId);

    List<Diary> findAllByShareOrderByCreatedDateDesc(boolean share);

    List<Diary> findAllByShareOrderByCreatedDate(boolean share);

    @Query(value = "SELECT\n" +
            "    * , (\n" +
            "       6371 * acos ( cos ( radians(:lat) ) * cos( radians(latitude) ) * cos( radians(longitude) - radians(:lon) )\n" +
            "          + sin ( radians(:lat) ) * sin( radians(latitude) )\n" +
            "       )\n" +
            "   ) AS distance\n" +
            "FROM diary d\n" +
            "where d.share = 1\n" +
            "HAVING distance <= 0.005\n" +
            "ORDER BY distance, d.created_date desc", nativeQuery = true)
    List<Diary> findAllLatestNearByPlace(@Param("lat") Double lat, @Param("lon") Double lon);

    @Query(value = "SELECT\n" +
            "    * , (\n" +
            "       6371 * acos ( cos ( radians(:lat) ) * cos( radians(latitude) ) * cos( radians(longitude) - radians(:lon) )\n" +
            "          + sin ( radians(:lat) ) * sin( radians(latitude) )\n" +
            "       )\n" +
            "   ) AS distance\n" +
            "FROM diary d\n" +
            "where d.share = 1\n" +
            "HAVING distance <= 0.005\n" +
            "ORDER BY distance, d.created_date", nativeQuery = true)
    List<Diary> findAllEarliestNearByPlace(@Param("lat") Double lat, @Param("lon") Double lon);

    @Query(value = "SELECT d.key_word, count(*)\n" +
            "FROM (SELECT *, ( \n" +
            "6371 * acos ( cos ( radians(:lat) ) * cos( radians(latitude) ) * cos( radians(longitude) - radians(:lon))\n" +
            "+ sin ( radians(:lat) ) * sin( radians(latitude) )\n" +
            ")) AS distance\n" +
            "FROM diary\n" +
            "where share = 1\n" +
            "HAVING distance <= 0.005\n" +
            ") as d\n" +
            "GROUP BY d.key_word", nativeQuery = true)
    List<ARResult> findAllNearByPlace(@Param("lat") Double lat, @Param("lon") Double lon);
    interface ARResult {
        String getKeyword();
        Long getCount();
    }
}
