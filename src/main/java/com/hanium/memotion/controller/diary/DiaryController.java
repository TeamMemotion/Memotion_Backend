package com.hanium.memotion.controller.diary;

import com.hanium.memotion.domain.diary.Diary;
import com.hanium.memotion.domain.diary.DiaryContent;
import com.hanium.memotion.dto.diary.DiaryContentDto;
import com.hanium.memotion.dto.diary.DiaryDto;
import com.hanium.memotion.service.diary.DiaryService;
import com.hanium.memotion.exception.base.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/saveEmotion")
    public BaseResponse<Long> postEmotion(@RequestPart("diaryDto") DiaryDto.Request diaryDto)throws Exception{
        System.out.println(diaryDto);
        Long result = diaryService.save(diaryDto);
        return BaseResponse.onSuccess(result);
    }

    @PostMapping("/saveContent")
    public Long postContent(@RequestPart("diaryContentDto") DiaryContentDto.Request diaryContentDto)throws Exception{
        System.out.println(diaryContentDto);

        return diaryService.saveContent(diaryContentDto);
    }


    //전제조회
    @GetMapping("/list")
    public List<Diary> postList()throws Exception{
        List<Diary> diaryList = diaryService.findByAll();
        List<DiaryDto.Response> diaryListResponse = null;
        for(Diary diary : diaryList){
            diaryListResponse.add(new DiaryDto.Response(diary));
        }
        return diaryList;
    }

    //날짜별 조회
    @PostMapping("/date/{memberId}")
    public List<DiaryDto.Response> localDateList (@RequestParam("Date") Date date, @PathVariable("memberId") Long memberId) {

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        List<Diary> diaryList = diaryService.findByDate(date,memberId);

        DiaryContent diaryContentList = diaryService.findByContentDate(date,memberId);

        List<DiaryDto.Response> diaryListResponse = null;
        for(Diary diary : diaryList){
            diaryListResponse.add(new DiaryDto.Response(diary));
        }
        return diaryListResponse;
    }

    @PostMapping("/date/content/{memberId}")
    public DiaryContentDto.Response localDateContentList (@RequestParam("Date") Date date, @PathVariable("memberId") Long memberId) {
        DiaryContent diaryContent = diaryService.findByContentDate(date,memberId);
        return new DiaryContentDto.Response(diaryContent);
    }

    @PostMapping("/update")
    public Long updatePost(@RequestPart("diaryDto") DiaryDto.Request diaryDto)throws Exception{
        System.out.println(diaryDto);

        return diaryService.save(diaryDto);
    }

}
