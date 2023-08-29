package com.hanium.memotion.service.diary;

import com.google.gson.JsonObject;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.List;



@Service
@RequiredArgsConstructor
public class EmotionAnalyzeService {
    @Value("${sentiment.clientId}")
    private String clientId;

    @Value("${sentiment.clientSecret}")
    private String clientSecret;

    @Value("${chatgpt.key}")
    private String apiKey;

    private static final String MODEL = "gpt-3.5-turbo";

    private OpenAiService openAiService;

    // Naver CLOVA Sentiment
    public String createSentiment(String diaryContent) {
        StringBuffer response = new StringBuffer();

        try {
            JsonObject object = new JsonObject();
            object.addProperty("content", diaryContent);

            String apiURL = "https://naveropenapi.apigw.ntruss.com/sentiment-analysis/v1/analyze";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
            con.setRequestProperty("Content-Type", "application/json");

            // post request
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            try (OutputStream os = con.getOutputStream()){
                byte request_data[] = object.toString().getBytes("utf-8");
                os.write(request_data);
            }

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else { // 오류 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    // ChatGPT 감정분석
    public String createGptComment(String content) {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(60));
        String prompt = "내가 작성한 다이어리를 한 줄 단위로 감정분석한 결과는 다음과 같다.\n"
                + content
                + "모든 문장의 postive와 negative 값을 분석해서 positive 80 이상이면 happy, positive 60 이상이면 smile, positive 40 이상이면 not bad, negative 60 이상이면 sad, negative 80 이상이면 upset을 의미한다.\n"
                + "오늘 하루 내 감정이 어떤 것에 가까운지 알려줘.\n"
                + "단 어떠한 설명도 없이 happy, smile, not bad, sad, upset 중 한 단어로만 알려줘.";

        ChatCompletionRequest requester = ChatCompletionRequest.builder()
                .model(MODEL)
                .maxTokens(2048)
                .messages(List.of(
                        new ChatMessage("user", prompt)
                )).build();
        return openAiService.createChatCompletion(requester).getChoices().get(0).getMessage().getContent();
    }
}
