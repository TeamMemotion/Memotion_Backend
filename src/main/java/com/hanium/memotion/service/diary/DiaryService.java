package com.hanium.memotion.service.diary;

import com.hanium.memotion.domain.diary.Diary;
import com.hanium.memotion.domain.diary.DiaryContent;
import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.dto.diary.DiaryContentDto;
import com.hanium.memotion.dto.diary.DiaryDto;
import com.hanium.memotion.exception.base.BaseException;
import com.hanium.memotion.exception.base.ErrorCode;
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
    public Long save(DiaryDto.Request diaryDto, Member member) {
        return diaryRepository.save(diaryDto.toEntity(member)).getDiaryId();
    }

    @Transactional
    public Long saveContent(DiaryContentDto.Request diaryDto, Member member) {
        return diaryContentRepository.save(diaryDto.toEntity(member)).getDiaryContentId();
    }

    public List<Diary> findByAll() {
        return diaryRepository.findAll();
    }

    public List<Diary> findById(Member member){
        return diaryRepository.findByMemberId(member);
    }

    public List<Diary> findByDate(String date, Member member) {
        return diaryRepository.findDiaryByCreatedDateAndMemberId(date, member);
    }


    public DiaryContent findByContentDate(String date, Member member) {
        return diaryContentRepository.findDiaryContentByCreatedDateLikeAndMemberId(date, member);
    }

    @Transactional
    public Diary diaryEmotionUpdate(DiaryDto.Request diaryDto, Member member) {
        Diary diary = diaryRepository.findById(diaryDto.getDiaryId()).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id:"+diaryDto.getDiaryId()));
        if(!diary.getMemberId().equals(member.getId()))
           throw new BaseException(ErrorCode.INVALID_USER);

        diary.update(diaryDto.getLatitude(),diaryDto.getLongitude(),diaryDto.getEmotion(),diaryDto.getKeyWord(),diaryDto.isShare());
        diaryRepository.save(diary);
        return diaryRepository.findById(diary.getDiaryId()).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. id=" + diary.getMemberId()));
    }

    @Transactional
    public DiaryContent diaryContentUpdate(DiaryContentDto.Request diaryContentDto, Member member){
        DiaryContent diaryContent= diaryContentRepository.findById(diaryContentDto.getDiaryContentId()).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id:"+diaryContentDto.getDiaryContentId()));
        if(!diaryContent.getMemberId().equals(member.getId()))
            throw new BaseException(ErrorCode.INVALID_USER);

        diaryContent.update(diaryContentDto.getTitle(), diaryContent.getContent());
        diaryContentRepository.save(diaryContent);
        return diaryContentRepository.findById(diaryContentDto.getDiaryContentId()).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. id=" + diaryContent.getMemberId().getId()));
    }

    public List<Diary> findByMonthDate(String date, Member member) {
        String sqlDate = "%"+date+"%";
        return diaryRepository.findByCreatedDateLikeAndMemberId(sqlDate, member);
    }
//    public List<DiaryContent> findByMonthDate(String date,Long memberId) {
//        //System.out.println(date + " "+ memberId);
//        Member user = memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. id=" + memberId));
//        return diaryRepository.findByCreatedDateLikeAndMemberId(date,user);
//    }
}
