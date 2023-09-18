package com.hanium.memotion.repository;

import com.hanium.memotion.domain.diary.Diary;
import com.hanium.memotion.domain.diary.DiaryContent;
import com.hanium.memotion.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface DiaryContentRepository extends JpaRepository<DiaryContent,Long> {

    DiaryContent findDiaryContentByCreatedDateLikeAndMemberId(String createdDate, Member memberId);


    DiaryContent findByCreatedDateLikeAndMemberId(String date, Member memberId);




}
