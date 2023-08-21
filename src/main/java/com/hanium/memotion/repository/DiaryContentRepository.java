package com.hanium.memotion.repository;

import com.hanium.memotion.domain.Diary;
import com.hanium.memotion.domain.DiaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryContentRepository extends JpaRepository<DiaryContent,Long> {
}
