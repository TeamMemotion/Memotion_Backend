package com.hanium.memotion.repository;

import com.hanium.memotion.domain.Diary;
import com.hanium.memotion.domain.DiaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface DiaryContentRepository extends JpaRepository<DiaryContent,Long> {

    DiaryContent findDiaryByCreatedDateAndMemberId(Date createdDate, Long memberId);
}
