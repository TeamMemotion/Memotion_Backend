package com.hanium.memotion.repository;


import com.hanium.memotion.domain.diary.Diary;
import com.hanium.memotion.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary,Long> {


    List<Diary> findDiaryByCreatedDateAndMemberId(Date createdDate, Member memberId);
}
