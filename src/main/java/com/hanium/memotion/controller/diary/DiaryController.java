package com.hanium.memotion.controller.diary;

import com.hanium.memotion.domain.diary.Diary;
import com.hanium.memotion.domain.diary.DiaryContent;
import com.hanium.memotion.dto.diary.DiaryContentDto;
import com.hanium.memotion.dto.diary.DiaryDto;
import com.hanium.memotion.service.diary.DiaryService;
import com.hanium.memotion.exception.base.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @PostMapping("/save-emotion")
    public BaseResponse<Long> postEmotion(@RequestPart("diaryDto") DiaryDto.Request diaryDto)throws Exception{
        System.out.println(diaryDto);
        Long result = diaryService.save(diaryDto);
        return BaseResponse.onSuccess(result);
    }

    @PostMapping("/save-content")
    public BaseResponse<Long> postContent(@RequestPart("diaryContentDto") DiaryContentDto.Request diaryContentDto)throws Exception{
        System.out.println(diaryContentDto);
        Long result = diaryService.saveContent(diaryContentDto);
        return BaseResponse.onSuccess(result);
    }


    //전제조회
    @GetMapping("/list")
    public BaseResponse<List<DiaryDto.Response>> postList()throws Exception{
        List<Diary> diaryList = diaryService.findByAll();
        List<DiaryDto.Response> resultDto = diaryList.stream()
                                            .map(data-> modelMapper.map(data, DiaryDto.Response.class))
                                            .collect(Collectors.toList());
//        List<DiaryDto.Response> diaryListResponse = (List<DiaryDto.Response>) new DiaryDto.Response();
//        for(Diary diary : diaryList){
//            System.out.println(diary.getDiaryId());
//            diaryListResponse.add(diary);
//        }
        return BaseResponse.onSuccess(resultDto);
    }

    //날짜별 조회
    @GetMapping("/{date}/{memberId}")
    public List<Diary> localDateList (@PathVariable("date") String date, @PathVariable("memberId") Long memberId) throws ParseException {

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //Date date1 = (Date) formatter.parse(date);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = format.parse(date);

        System.out.println(date1);


        List<Diary> diaryList = diaryService.findByDate(date1,memberId);

        //DiaryContent diaryContentList = diaryService.findByContentDate(date1,memberId);

        List<DiaryDto.Response> diaryListResponse = null;
        for(Diary diary : diaryList){
            diaryListResponse.add(new DiaryDto.Response(diary));
        }
        return diaryList;
    }

    @PostMapping("/date/content/{memberId}")
    public DiaryContentDto.Response localDateContentList (@RequestParam("Date") Date date, @PathVariable("memberId") Long memberId) {
        DiaryContent diaryContent = diaryService.findByContentDate(date,memberId);
        return new DiaryContentDto.Response(diaryContent);
    }


    @PostMapping("/update-content")
    public BaseResponse<DiaryContent> updateContent(@RequestPart("diaryContentDto") DiaryContentDto.Request diaryDto)throws Exception{
        //System.out.println(diaryDto);

        return BaseResponse.onSuccess(diaryService.diaryContentUpdate(diaryDto));
    }
    @PostMapping("/update-emotion")
    public BaseResponse<Diary> updateEmotion(@RequestPart("diaryDto") DiaryDto.Request diaryDto)throws Exception{
        //System.out.println(diaryDto);

        return BaseResponse.onSuccess(diaryService.diaryEmotionUpdate(diaryDto));
    }

}
