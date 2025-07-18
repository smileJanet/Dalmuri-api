package com.dalmuri.config;

import com.dalmuri.socket.ChatHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    /*
    * 1) @Configuration : 해당 클래스가 스프링의 설정(Configuration)파일이라는 뜻. 스프링은 이 클래스를 읽어서 애플리케이션을 어떻게 구성할지 파악한다.
    * "이 파일에 중요한 설정 정보가 있으니까 잘 봐둬!"
    * 2) @EnableWebSockt : 스프링에게 웹소켓 기능을 활성화하라고 지시. 스프링이 웹소켓 관련 빈(Bean)들을 자동으로 등록하고, 웹소켓 통신을 처리할 준비를 할 수 있다.
    * "이제부터 웹소켓 통신을 사용할 거니까 준비해 둬!"
    *
    * */

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){
        /*
        * 1) @Override : 현재 메소드가 부모 인터페이스의 메소드를 "재정의"하고 있다는 뜻.
        * 2) WebSocketConfigurer : 웹소켓 핸들러(Handler)를 등록하는 방법을 제공
        * "나는 웹소켓을 설정하는 규칙에 따라 코드를 작성할거야."
        * + implements"구현" : 구현한다는 것은 WebSocketConfigurer가 정의한 메소드를 "반드시" 구현할 것이라는 의미.
        * 왜 "반드시" 구현하냐면, 부모에게 해당 클래스가 정의되어있지 않기 때문.
        *
        * [갑분 extends, implements]
        * extends = "기능을 그대로 물려받는다" (부모가 메서드를 이미 가지고 있음)
        * implements = "기능을 설계도로 받아서 내가 직접 만든다" (부모가 메서드의 이름만 알려줌)
        *
        * */
        registry.addHandler(new ChatHandler(), "/chat").setAllowedOrigins("*");

        /*
        * 1) addHandler : 웹소켓 핸들러 추가
        * 2) new ChatHandler : ChatHandler(임의로 작성)라는 웹소켓 핸들러의 새로운 인스턴스를 생성. ChatHandler에 실제로 웹소켓 메시지를 주고받는 로직(=채팅 처리 로직)을 담고 있을 것이다.
        * 3) /chat : 클라이언트 측에서 웹소켓 연결을 요청할 "경로"를 지정. ex : ws://localhost:8080/chat과 같은 주소로 웹소켓 연결을 시도한다면 ChatHandler가 이를 처리할 것이다.
        * 4) setAllowedOrigins("*") : 교차 출처(Cross Origin)요청을 허용하는 설정. *는 모든 출처(origin)에서의 접근을 허용하겠다는 의미. 단 보안상 권장되지 않으며, 실제 운영에선 특정 도메인만 허용되도록 설정할 것
        *   ex: "http://localhost:3000", "https://yourdomain.com"
        *
        * */
    }

}
