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
    *           : https://cloud.google.com/docs/authentication/set-up-adc-on-premises?hl=ko
    * 웹사이트에서 json키를 발급 받고, 환경변수에서 환경 설정을 해준다. json키는 매우 민감한 키여서 git push가 거절되므로 프로젝트 외부에 심어준다.
    *
    * 3. 환경변수 설정
    * Run > Edit Configuration > Modify Options > Environment Variables에서 JSON키를 보관한 경로를 지정
    *
    * */

    /*
    * [GetMapping과 PostMapping의 차이]
    * 1. Get : 서버에서 데이터를 가져올 때
    *    요청 데이터의 위치 : 쿼리스트링(?text="loremipsum....") - 데이터 노출로 다소 취약
    *    idempotent : 같은 요청을 보내도 결과가 같아야 함 - 데이터의 조회, 검색(서버 상태 변경이 x)
    *    용도 : 단순 조회, 검색
    *
    * 2. Post : 서버로 데이터를 보내야 할 때
    *    요청 데이터 위치 : Http Body(Json, form) - 비교적 보안에 안정
    *    idempotent : 요청마다 결과가 달라질 수 있음 - 데이터의 생성, 변경(서버 상태도 변경 o)
    *    용도 : 로그인, 분석 요청, 로그인, 글쓰기 등 검색 이외의 다양한 기능
    *
    * */

    /*
    * [ResponseEntity]
    * 응답 본문에 DiaryResponseDTO 객체를 담음
    * Http 상태 코드(ex. 200 ok, 400 bad request)도 함께 보낼 수 있도록 하는 Spring의 응답 클래스
    *
    * [@RequestBody]
    * 요청 데이터 위치: Http Body 본문
    * 데이터 형식 : JSON, XML등 구조화된 데이터
    * 사용 목적 : Post 요청 시 복잡한 객체 데이터 생성 및 수정
    * 바인딩 대상 : Http Body 전체를 Java 객체로 매핑 (역직렬화)
    *
    * + ) 직렬화 : Java 데이터를 Json으로 바꾸는 것
    *
    * Json 데이터를 Java에서 사용할 수 있도록 변환하는 어노테이션
    * 클라이언트로부터 온 Http Body의 데이터를 Java로 변환
    * 데이터는 주로 Json 혹은 XML로 찾아온다
    *
    * ex. 요청: POST /users, Body: {"username": "gemini", "email": "g@ai.com"}
      @PostMapping("/users")
      public String createUser(@RequestBody UserDto user) {
          // user 객체 안에 username과 email 데이터가 모두 담겨 있음
          // ...
      }
    *
    * [<-> @RequestParam?]
    * 요청 데이터 위치 : Http URL의 쿼리스트링 혹은 폼데이터
    * 데이터 형식 : 단순 문자열, 숫자 등 단일값
    * 사용 목적 : Get 요청 시, 데이터 필터링/조회 조건 전달
    * 바인딩 대상 : URL의 특정 key=value 쌍을 Java 변수로 매핑
    *
    * 클라이언트에서 HTTP URL 쿼리스트링 혹은 폼 데이터로 값을 보내면 Java에서 사용할 수 있도록 변환
    * 단순한 값을 전달할 때(ex. 조건 조회) 사용, 따라서 Get 방식에서 사용됨
    *
    * ex. 요청: GET /products?keyword=책&page=2
      @GetMapping("/products")
      public String getProducts(
          @RequestParam("keyword") String searchWord,
          @RequestParam("page") int pageNumber) {
          // searchWord = "책", pageNumber = 2
          // ...
      }
    *
    * */

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

    @PostMapping("/get-diary-score")
    public ResponseEntity<DiaryResponseDTO> getDiaryScore(@RequestBody DiaryRequestDTO request) {
        DiaryResponseDTO response = new DiaryResponseDTO();

        try (LanguageServiceClient language = LanguageServiceClient.create()) {

            String text = request.getText();
            Document doc = Document.newBuilder()    // Google Cloud Natural Language API에 넘겨줄 Document 객체 생성
                    .setContent(text)               // 분석할 실제 텍스트 설정
                    .setType(Type.PLAIN_TEXT)       // 분석 대상이 일반 텍스트임을 지정
                    .build();                       // 최종 Document 객체 생성

            Sentiment sentiment = language.analyzeSentiment(doc)    // Google Cloud에 감정 분석 요청을 보냄. 텍스트의 감정을 분석해서 점수를 메기고 반환
                    .getDocumentSentiment();                        // 문서 전체에 대한 평균 감정 점수(Sentiment)를 가져옴

            /*
            * [구글에서 제공하는 감정 점수(Sentiment)의 범위]
            * 출처 : https://cloud.google.com/natural-language/docs/basics?hl=ko#interpreting_sentiment_analysis_values
            * 1) score : -1.0 → 매우 부정적, 0.0 → 중립, +1.0 → 매우 긍정적
            * 2) magnitude : 감정의 강도 (0.0 이상), 높을수록 감정의 강도가 큼 (기쁨, 분노, 슬픔 등 강렬한 감정 표현)
            * */

            response.setScore(sentiment.getScore());
            response.setMagnitude(sentiment.getMagnitude());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Google Cloud API 호출 중 예외 발생: " + e.getMessage());

            return ResponseEntity.badRequest().build();
        }

//        [TEST LOGIC]
//        response.setScore(0.8f);
//        response.setMagnitude(0.8f);
//        return ResponseEntity.ok(response);

    }

}