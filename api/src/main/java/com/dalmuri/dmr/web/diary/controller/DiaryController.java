package com.dalmuri.dmr.web.diary.controller;

import com.dalmuri.dmr.web.diary.model.DiaryResponseDTO;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins="http://localhost:1003")
@RequestMapping("/diary")
public class DiaryController {

    /*
    * [Google Cloud - Natural Language API]
    * 해당 라이브러리, api를 통해서 텍스트의 감정을 분석
    * 참고 사이트 : https://cloud.google.com/natural-language/docs/reference/libraries?hl=ko#client-libraries-usage-java
    *
    * 1. 구글 웹사이트 설명 문서에 따라 pom.xml에 필요한 의존성 설치, maven 업데이트
    *
    * 2. 구글 애플리케이션 기본 사용자 인증 정보 설정 (ADC 방식을 통해)
    * 참고 사이트 : https://cloud.google.com/docs/authentication/application-default-credentials?hl=ko
    *
    *
    *
    *
    *
    * */

    @GetMapping("/get-diary-score")
    public ResponseEntity<DiaryResponseDTO> getDiaryScore() {
        DiaryResponseDTO response = new DiaryResponseDTO();

        try (LanguageServiceClient language = LanguageServiceClient.create()) {

            String text = "야 이거 한국어도 되냐? 끝내준다 기분 째지는데? ㅋㅋㅋㅋ";
            Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();

            Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();

            response.setSentiment(sentiment);
            response.setScore(sentiment.getScore());
            response.setMagnitude(sentiment.getMagnitude());

            System.out.println("sentiment : " + sentiment);
            System.out.println("sentiment.getScore() : " + sentiment.getScore());
            System.out.println("sentiment.getMagnitude() : " + sentiment.getMagnitude());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Google Cloud API 호출 중 예외 발생: " + e.getMessage());

            return ResponseEntity.badRequest().build();
        }

    }

}