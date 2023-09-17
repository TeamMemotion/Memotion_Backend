package com.hanium.memotion.controller.diary;

import com.hanium.memotion.domain.diary.Diary;
import com.hanium.memotion.domain.diary.DiaryContent;
import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.dto.diary.DiaryContentDto;
import com.hanium.memotion.dto.diary.DiaryDto;
import com.hanium.memotion.exception.base.BaseException;
import com.hanium.memotion.service.diary.DiaryService;
import com.hanium.memotion.exception.base.BaseResponse;
import com.hanium.memotion.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;
    private final ModelMapper modelMapper;

    private final MemberService memberService;

    @PostMapping("/emotion")
    public BaseResponse<Long> postEmotion(@RequestBody DiaryDto.Request diaryDto, @AuthenticationPrincipal Member member)throws Exception{
        Long result = diaryService.save(diaryDto, member);
        return BaseResponse.onSuccess(result);
    }

    @PostMapping("/content")
    public BaseResponse<Long> postContent(@RequestBody DiaryContentDto.Request diaryContentDto, @AuthenticationPrincipal Member member) throws Exception{
        System.out.println("con");
        System.out.println("conconcon"+member.getId());
        //DiaryContent diaryContent=diaryService.findByContentDate(diaryContentDto.getCreatedDate(),member);
        if( diaryService.findByContentDate(diaryContentDto.getCreatedDate(),member) != null )
            diaryService.diaryContentUpdate(diaryContentDto, member, diaryService.findByContentDate(diaryContentDto.getCreatedDate(),member).getDiaryContentId());


        Long result = diaryService.saveContent(diaryContentDto, member);


        return BaseResponse.onSuccess(result);
    }


    //전제조회
    @GetMapping("/list")
    public BaseResponse<List<DiaryDto.Response>> postList(@AuthenticationPrincipal Member member){
        List<Diary> diaryList = diaryService.findById(member);
        List<DiaryDto.Response> resultDto = diaryList.stream()
                                            .map(data-> modelMapper.map(data, DiaryDto.Response.class))
                                            .collect(Collectors.toList());
        return BaseResponse.onSuccess(resultDto);
    }

    //날짜별 조회
    @GetMapping("/{date}")
    public BaseResponse<List<DiaryDto.Response>> localDateList (@PathVariable("date") String date, @AuthenticationPrincipal Member member) throws ParseException {
        List<Diary> diaryList = diaryService.findByDate(date, member);
        List<DiaryDto.Response> resultDto = diaryList.stream()
                .map(data-> modelMapper.map(data, DiaryDto.Response.class))
                .collect(Collectors.toList());

        return BaseResponse.onSuccess(resultDto);
    }

    @GetMapping("/content/{date}")
    public BaseResponse<DiaryContentDto.Response> localDateContentList (@PathVariable("date") String date, @AuthenticationPrincipal Member member) throws ParseException {
        DiaryContent diaryContent = diaryService.findByContentDate(date, member);
        return BaseResponse.onSuccess(new DiaryContentDto.Response(diaryContent, member.getId()));
    }

    @PatchMapping("/content/{diaryId}")
    public BaseResponse<DiaryContent> updateContent(@RequestBody DiaryContentDto.Request diaryDto, @AuthenticationPrincipal Member member, @PathVariable("diaryId") Long diaryId) throws Exception{
        return BaseResponse.onSuccess(diaryService.diaryContentUpdate(diaryDto, member, diaryId));
    }
    @PatchMapping("/emotion/{diaryId}")
    public BaseResponse<Diary> updateEmotion(@RequestBody DiaryDto.Request diaryDto, @AuthenticationPrincipal Member member, @PathVariable("diaryId") Long diaryId) throws BaseException {
        return BaseResponse.onSuccess(diaryService.diaryEmotionUpdate(diaryDto, member, diaryId));
    }

    @GetMapping("/month/{date}/{emotion}")
    public BaseResponse<List<DiaryDto.Response>> MonthDateList (@PathVariable("date") String date, @AuthenticationPrincipal Member member,@PathVariable("emotion") String emotion) throws ParseException {
        List<Diary> diaryList = diaryService.findByMonthDate(date, member,emotion);
        List<DiaryDto.Response> resultDto = diaryList.stream()
                .map(data-> modelMapper.map(data, DiaryDto.Response.class))
                .collect(Collectors.toList());
        return BaseResponse.onSuccess(resultDto);
    }

    @DeleteMapping("/emotion/{diaryId}")
    public BaseResponse<Long> delete (@PathVariable("diaryId") Long id) throws ParseException {
        return BaseResponse.onSuccess(diaryService.delete(id));
    }

    //혹시 emotion list 필요하다 그러면 만들어주기
    //user list 여도 충분할 것 같긴 한데
}
