package com.hanium.memotion.repository;


import com.hanium.memotion.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary,Long> {

    List<Diary> findDiaryByCreatedDateAndMemberId(Date createdDate, Long memberId);
}
