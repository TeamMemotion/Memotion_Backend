package com.hanium.memotion.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;



@Service
@RequiredArgsConstructor
public class EmotionAnalyzeService {
    private OpenAiService openAiService;

    @Value("${chatgpt.key}")
    private String apiKey;

    private static final String MODEL = "gpt-3.5-turbo";

    // ChatGPT 감정분석
    public String createGptComment(String content) {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(60));
        String prompt = "내가 오늘 다이어리에 작성한 내용은 " + content + "야. 다이어리 내용 바탕으로 감정분석 해줘.";
        ChatCompletionRequest requester = ChatCompletionRequest.builder()
                .model(MODEL)
                .maxTokens(2048)
                .messages(List.of(
                        new ChatMessage("user", prompt)
                )).build();
        return openAiService.createChatCompletion(requester).getChoices().get(0).getMessage().getContent();
    }
}
