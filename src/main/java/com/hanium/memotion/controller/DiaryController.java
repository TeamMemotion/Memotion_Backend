package com.hanium.memotion.controller;

import com.hanium.memotion.domain.Diary;
import com.hanium.memotion.dto.diary.DiaryDto;
import com.hanium.memotion.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/save")
    public Long post(@RequestPart("diaryDto") DiaryDto.Request diaryDto)throws Exception{
        System.out.println(diaryDto);

        return diaryService.save(diaryDto);
    }
    //전제조회
    @GetMapping("/list")
    public List<DiaryDto.Response> postList()throws Exception{
        List<Diary> diaryList = diaryService.findByAll();
        List<DiaryDto.Response> diaryListResponse = null;
        for(Diary diary : diaryList){
            diaryListResponse.add(new DiaryDto.Response(diary));
        }
        return diaryListResponse;
    }

    //날짜별 조회
    @PostMapping("/date/{memberId}")
    public List<DiaryDto.Response> localDateList (@RequestParam("Date") Date date, @PathVariable("memberId") Long memberId) {

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        List<Diary> diaryList = diaryService.findByDate(date,memberId);

        List<DiaryDto.Response> diaryListResponse = null;
        for(Diary diary : diaryList){
            diaryListResponse.add(new DiaryDto.Response(diary));
        }
        return diaryListResponse;
    }
    @PostMapping("/update")
    public Long updatePost(@RequestPart("diaryDto") DiaryDto.Request diaryDto)throws Exception{
        System.out.println(diaryDto);

        return diaryService.save(diaryDto);
    }
}
