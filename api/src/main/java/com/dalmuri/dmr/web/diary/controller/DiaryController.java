package com.dalmuri.dmr.web.diary.controller;

import com.dalmuri.dmr.web.diary.model.DiaryRequestDTO;
import com.dalmuri.dmr.web.diary.model.DiaryResponseDTO;
import com.dalmuri.dmr.web.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins="http://localhost:1003")
@RequestMapping("/diary")
@RequiredArgsConstructor
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

    private final DiaryService service;

    /*
    * [Constructor]
    *
    * (1) RequiredArgsConstructor
    * 클래스에 선언된 final, @NonNull 필드를 자동으로 초기화해주는 생성자를 의미
    * ex) public DiaryController (DiaryService service) { this.service = service; }
    * '필수 필드만' 매개변수 생성자를 대신 만들어줌
    * final : 객체 생성 시 반드시 주입되어야 함, 이후에 변경될 수 없음 '불변 의존성'
    *
    * * 헷갈림 주의 !
    * 상수(constant) : public static final ~ 이라고 선언됨
    * 상수는 **프로그램 전체**에서 고정된 값
    *
    * (2) NoArgsConstructor
    * 기본 생성자를 대신 만들어주는 어노테이션
    * ex) public DiaryController () {}
    * 아무 인자도 없는 빈 생성자를 만들어줌
    * JPA Entity 직렬화, 프레임워크에서 객체를 자동 생성할 때 유용함
    *
    * (3) AllArgsConstructor
    * ex) public DiaryController (DiaryService service) { this.service = service; }
    * 클래스 내에 있는 모든 필드를 매개변수로 받는 전체 매개변수 생성자를 만듦
    * DTO와 같은 단순 데이터 객체를 만들 때 자주 사용
    *
    * */


    @PostMapping("/get-diary-score")
    public ResponseEntity<DiaryResponseDTO> getDiaryScore(@RequestBody DiaryRequestDTO request) {
        return service.getDiaryScore(request);
    } // getDiaryScore


}