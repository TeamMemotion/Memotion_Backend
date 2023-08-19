package com.hanium.memotion.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmotionAnalyzeController {

    @Value("${sentiment.clientId}")
    private String clientId;

    @Value("${sentiment.clientSecret}")
    private String clientSecret;


    @GetMapping("/sentiment")
    public String sentiment() {

        StringBuffer response = new StringBuffer();
        try {

            String content = "{\"content\":\"싸늘하다. 가슴에 비수가 날아와 꽂힌다.\"}";
            String apiURL = "https://naveropenapi.apigw.ntruss.com/sentiment-analysis/v1/analyze";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
            con.setRequestProperty("Content-Type", "application/json");
            System.out.println(clientId + "," + clientSecret);
            System.out.println(content);

            // post request
            String postParams = "content=" + "안녕";
            System.out.println(postParams);
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
//            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//            wr.writeBytes(postParams);
//            wr.flush();
//            wr.close();
            try (OutputStream os = con.getOutputStream()){
                byte request_data[] = content.getBytes("utf-8");
                os.write(request_data);
                os.close();
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

            System.out.println(response.toString());
            return response.toString();
        } catch (Exception e) {
            System.out.println(e);
        }
        return response.toString();
    }



}
