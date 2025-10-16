package com.dalmuri.dmr.web.diary.controller;

import com.dalmuri.dmr.web.diary.model.DiaryRequestDTO;
import com.dalmuri.dmr.web.diary.model.DiaryResponseDTO;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    * 웹사이트에서 json키를 발급 받고, 환경변수에서 환경 설정을 해준다. json키는 매우 민감한 키여서 git push가 거절되므로 프로젝트 외부에 심어준다.
    *
    * 3. 환경변수 설정
    * Run > Edit Configuration > Modify Options > Environment Variables에서 JSON키를 보관한 경로를 지정
    *
    * */

    @PostMapping("/get-diary-score")
    public ResponseEntity<DiaryResponseDTO> getDiaryScore(@RequestBody DiaryRequestDTO request) {
        DiaryResponseDTO response = new DiaryResponseDTO();

//        try (LanguageServiceClient language = LanguageServiceClient.create()) {
//
//            String text = request.getText();
//            Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
//
//            Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();
//
//            response.setScore(sentiment.getScore());
//            response.setMagnitude(sentiment.getMagnitude());
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("Google Cloud API 호출 중 예외 발생: " + e.getMessage());
//
//            return ResponseEntity.badRequest().build();
//        }

        response.setScore(0.8f);
        response.setMagnitude(0.8f);

        return ResponseEntity.ok(response);

    }

}