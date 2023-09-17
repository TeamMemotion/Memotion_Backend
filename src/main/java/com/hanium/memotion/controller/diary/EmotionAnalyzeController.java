package com.hanium.memotion.controller.diary;


import com.hanium.memotion.exception.base.BaseException;
import com.hanium.memotion.exception.base.BaseResponse;
import com.hanium.memotion.exception.base.ErrorCode;
import com.hanium.memotion.service.diary.EmotionAnalyzeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmotionAnalyzeController {
    private final EmotionAnalyzeService emotionAnalyzeService;

    // 다이어리 작성 내용을 바탕으로 감정분석 (네이버 감정 분석 -> GPT 분석)

    public String sentiment(String content) {
        if(content.isEmpty())
            throw new BaseException(ErrorCode.EMPTY_DIARY);
        // 1. Naver CLOVA Sentiment를 활용한 감정 분석
        String sentimentResult = emotionAnalyzeService.createSentiment(content);
        // 2. ChatGPT를 활용한 감정 분석
        String gptResult = emotionAnalyzeService.createGptComment(sentimentResult);
        return gptResult;
    }
}
