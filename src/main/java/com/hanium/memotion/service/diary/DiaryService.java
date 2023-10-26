package com.hanium.memotion.service.diary;

import com.hanium.memotion.controller.diary.EmotionAnalyzeController;
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
import java.sql.SQLOutput;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DiaryService {
    private final EmotionAnalyzeController emotionAnalyzeController;
    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

    private final DiaryContentRepository diaryContentRepository;
    @Transactional
    public Long save(DiaryDto.Request diaryDto, Member member) {
        return diaryRepository.save(diaryDto.toEntity(member)).getDiaryId();
    }

    @Transactional
    public Long saveContent(DiaryContentDto.Request diaryDto, Member member) {
        System.out.println("con");
        System.out.println("ser"+member.getId());
        diaryDto.setKeyword(emotionAnalyzeController.sentiment(diaryDto.getContent()));
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
    public Diary diaryEmotionUpdate(DiaryDto.Request diaryDto, Member member, Long diaryId) {
        Diary diary = diaryRepository.findByDiaryId(diaryId);
        System.out.println("test" + "  " + member.getId()+ "  " );
        if(diary == null)
            throw new BaseException(ErrorCode.EMPTY_DIARY);

        if(diary.getMemberId().getId() != member.getId())
            throw new BaseException(ErrorCode.INVALID_USER);


        diary.update(diaryDto.getLatitude(),diaryDto.getLongitude(),diaryDto.getEmotion(),diaryDto.getKeyWord(),diaryDto.isShare(),diaryDto.getPlace());
        diaryRepository.save(diary);
        return diaryRepository.findById(diary.getDiaryId()).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. id=" + diary.getMemberId()));
    }

    @Transactional
    public DiaryContent diaryContentUpdate(DiaryContentDto.Request diaryContentDto, Member member, Long diaryId){
        DiaryContent diaryContent= diaryContentRepository.findById(diaryId).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id:"+diaryId));
        //if(!diaryContent.getMemberId().equals(member.getId()))
          //  throw new BaseException(ErrorCode.INVALID_USER);
        System.out.println("3" + diaryContent.getContent());
        diaryContentDto.setKeyword(emotionAnalyzeController.sentiment(diaryContentDto.getContent()));
        String keyword = emotionAnalyzeController.sentiment(diaryContentDto.getContent());
        diaryContent.update(diaryContent.getDiaryContentId(),diaryContent.getCreatedDate(),diaryContentDto.getTitle(), diaryContentDto.getContent(),keyword,diaryContent.getMemberId().getId());

        diaryContentRepository.save(diaryContent);
        return diaryContentRepository.findById(diaryId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. id=" + diaryContent.getMemberId().getId()));
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

    public Long delete(Long id){
        diaryRepository.delete(diaryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 다이어리가 존재하지 않습니다. id=" + id)));
        return  id;
    }

    public List<DiaryContent> findByEmotion(String emotion){
        return diaryContentRepository.findByKeyWord(emotion);
    }
    public List<DiaryContent> findByDiaryContentMonthDate(String date, Member member) {
        String sqlDate = "%"+date+"%";
        return diaryContentRepository.findByCreatedDateLikeAndMemberId(sqlDate, member);
    }

}
