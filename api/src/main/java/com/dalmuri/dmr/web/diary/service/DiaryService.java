package com.dalmuri.dmr.web.diary.service;

import com.dalmuri.dmr.web.diary.model.DiaryRequestDTO;
import com.dalmuri.dmr.web.diary.model.DiaryResponseDTO;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryService {

    /*
     * [try-with-resources 구문]
     * try-catch 구문에선
     * try - catch - "finally"를 무조건 명시해야 한다. 메모리 누수를 위해.... (ex. stream.close()와 같이)
     * 근데 try-with-resources는 finally를 굳이 쓰지 않아도 된다. 자원을 자동으로 관리하고 해제해주기 때문
     * 내가 여태 쓴게 try-with-resources였다. try-catch가 아니라
     *
     * 주로 finally는 예외가 발생하든, 발생하지 않든 무조건 처리해야 하는 로직이 있을 때 사용한다.
     * ex. 자원 해제 : 파일, 네트워크, DB연결 후 반납해야 할 때
     *     정리 작업 : 예외가 발생하더라도 반드시 수행해야 하는 로직이 있을 때
     *     보장된 실행 : try구문에서 예외가 실행되든, 실행되지 않든 반드시 수행해야 하는 로직이 있을 때
     * */


    public ResponseEntity<DiaryResponseDTO> getDiaryScore(DiaryRequestDTO request) {
        DiaryResponseDTO response = new DiaryResponseDTO();

//        try (LanguageServiceClient language = LanguageServiceClient.create()) {
//
//            String text = request.getText();
//            Document doc = Document.newBuilder()    // Google Cloud Natural Language API에 넘겨줄 Document 객체 생성
//                    .setContent(text)               // 분석할 실제 텍스트 설정
//                    .setType(Document.Type.PLAIN_TEXT)       // 분석 대상이 일반 텍스트임을 지정
//                    .build();                       // 최종 Document 객체 생성
//
//            Sentiment sentiment = language.analyzeSentiment(doc)    // Google Cloud에 감정 분석 요청을 보냄. 텍스트의 감정을 분석해서 점수를 메기고 반환
//                    .getDocumentSentiment();                        // 문서 전체에 대한 평균 감정 점수(Sentiment)를 가져옴
//
//            /*
//             * [구글에서 제공하는 감정 점수(Sentiment)의 범위]
//             * 출처 : https://cloud.google.com/natural-language/docs/basics?hl=ko#interpreting_sentiment_analysis_values
//             * 1) score : -1.0 → 매우 부정적, 0.0 → 중립, +1.0 → 매우 긍정적
//             * 2) magnitude : 감정의 강도 (0.0 이상), 높을수록 감정의 강도가 큼 (기쁨, 분노, 슬픔 등 강렬한 감정 표현)
//             * */
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

//        [TEST LOGIC]
        response.setScore(0.8f);
        response.setMagnitude(0.8f);
        return ResponseEntity.ok(response);

    } // getDiaryScore
}
