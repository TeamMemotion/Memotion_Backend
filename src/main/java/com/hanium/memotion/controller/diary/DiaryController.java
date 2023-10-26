package com.hanium.memotion.controller.diary;

import com.hanium.memotion.domain.diary.Diary;
import com.hanium.memotion.domain.diary.DiaryContent;
import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.dto.diary.DiaryContentDto;
import com.hanium.memotion.dto.diary.DiaryDto;
import com.hanium.memotion.dto.diary.DiaryEmotionDto;
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

    @PostMapping("/emotion")
    public BaseResponse<Long> postEmotion(@RequestBody DiaryDto.Request diaryDto, @AuthenticationPrincipal Member member) {
        Long result = diaryService.save(diaryDto, member);
        return BaseResponse.onSuccess(result);
    }

    @PostMapping("/content")
    public BaseResponse<Long> postContent(@RequestBody DiaryContentDto.Request diaryContentDto, @AuthenticationPrincipal Member member) {
        DiaryContent diaryContent = diaryService.findByContentDate(diaryContentDto.getCreatedDate(),member);

        if(diaryContent != null)
            return BaseResponse.onSuccess(diaryService.diaryContentUpdate(diaryContentDto, member, diaryContent.getDiaryContentId()).getDiaryContentId());
        else
            return BaseResponse.onSuccess(diaryService.saveContent(diaryContentDto, member));
    }

    //전제조회
    @GetMapping("/list/{emotion}")
    public BaseResponse<List<DiaryContentDto.Response>> postList(@PathVariable("emotion") String emotion,@AuthenticationPrincipal Member member){
        List<DiaryContent> diaryList = diaryService.findByEmotion(emotion);
        List<DiaryContentDto.Response> resultDto = diaryList.stream()
                                            .map(data-> modelMapper.map(data, DiaryContentDto.Response.class))
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
    public BaseResponse<DiaryContent> updateContent(@RequestBody DiaryContentDto.Request diaryDto, @AuthenticationPrincipal Member member, @PathVariable("diaryId") Long diaryId) {
        return BaseResponse.onSuccess(diaryService.diaryContentUpdate(diaryDto, member, diaryId));
    }

    @PatchMapping("/emotion/{diaryId}")
    public BaseResponse<Long> updateEmotion(@RequestBody DiaryDto.Request diaryDto, @AuthenticationPrincipal Member member, @PathVariable("diaryId") Long diaryId) throws BaseException {
        Diary diary = diaryService.diaryEmotionUpdate(diaryDto, member, diaryId);
        return BaseResponse.onSuccess(diary.getDiaryId());
    }

//    @GetMapping("/month/{date}")
//    public BaseResponse<List<DiaryEmotionDto>> MonthDateList (@PathVariable("date") String date, @AuthenticationPrincipal Member member) throws ParseException {
//        List<Diary> diaryList = diaryService.findByMonthDate(date, member);
//        List<DiaryEmotionDto> resultDto = diaryList.stream()
//                .map(data-> modelMapper.map(data, DiaryEmotionDto.class))
//                .collect(Collectors.toList());
//        return BaseResponse.onSuccess(resultDto);
//    }
//    @GetMapping("/month/{date}")
//    public BaseResponse<List<DiaryEmotionDto>> MonthDateList (@PathVariable("date") String date, @AuthenticationPrincipal Member member) throws ParseException {
//        List<Diary> diaryList = diaryService.findByMonthDate(date, member);
//        List<DiaryEmotionDto> resultDto = diaryList.stream()
//                .map(data-> modelMapper.map(data, DiaryEmotionDto.class))
//                .collect(Collectors.toList());
//        return BaseResponse.onSuccess(resultDto);
//    }
    @DeleteMapping("/emotion/{diaryId}")
    public BaseResponse<Long> delete (@PathVariable("diaryId") Long id) {
        return BaseResponse.onSuccess(diaryService.delete(id));
    }

    @GetMapping("/content/month/{date}")
    public BaseResponse<List<DiaryEmotionDto>> DiaryContentMonthDateList (@PathVariable("date") String date, @AuthenticationPrincipal Member member) {
        List<DiaryContent> diaryContent = diaryService.findByDiaryContentMonthDate(date, member);
        List<DiaryEmotionDto> resultDto = diaryContent.stream()
                .map(data-> modelMapper.map(data, DiaryEmotionDto.class))
                .collect(Collectors.toList());
        return BaseResponse.onSuccess(resultDto);
    }
    //혹시 emotion list 필요하다 그러면 만들어주기
    //user list 여도 충분할 것 같긴 한데
}
