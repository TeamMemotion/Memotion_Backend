package com.hanium.memotion.service;

import com.hanium.memotion.domain.Diary;
import com.hanium.memotion.domain.DiaryContent;
import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.dto.diary.DiaryContentDto;
import com.hanium.memotion.dto.diary.DiaryDto;
import com.hanium.memotion.repository.DiaryContentRepository;
import com.hanium.memotion.repository.DiaryRepository;
import com.hanium.memotion.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

    private final DiaryContentRepository diaryContentRepository;
    @Transactional
    public Long save(DiaryDto.Request diaryDto) {
        Member user = memberRepository.findById(diaryDto.getMemberId().getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. id=" + diaryDto.getMemberId().getId()));
        return diaryRepository.save(diaryDto.toEntity(user)).getDiaryId();
    }

    @Transactional
    public Long saveContent(DiaryContentDto.Request diaryDto) {
        Member user = memberRepository.findById(diaryDto.getMemberId().getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. id=" + diaryDto.getMemberId().getId()));
        return diaryContentRepository.save(diaryDto.toEntity(user)).getDiaryContentId();
    }
    public List<Diary> findByAll() {
        return diaryRepository.findAll();
    }
    public List<Diary> findByDate(Date date, Long memberId) {
        return diaryRepository.findDiaryByCreatedDateAndMemberId(date,memberId);
    }

    public DiaryContent findByContentDate(Date date, Long memberId) {
        return diaryContentRepository.findDiaryByCreatedDateAndMemberId(date,memberId);
    }
}