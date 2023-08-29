package com.hanium.memotion.service.diary;

import com.hanium.memotion.domain.diary.Diary;
import com.hanium.memotion.domain.diary.DiaryContent;
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
        System.out.println(diaryDto.getMemberId());
        Long a = 1L;
        System.out.println(memberRepository.findById(1L));
        Member user = memberRepository.findById(diaryDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. id=" + diaryDto.getMemberId()));
        System.out.println(diaryDto.getMemberId());
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
