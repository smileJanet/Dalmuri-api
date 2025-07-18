package com.dalmuri.socket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ChatHandler extends TextWebSocketHandler {
    /*
    * 1) TextWebSocketHandler : 스프링에서 텍스트 기반의 웹소켓 메시지를 처리하는데 필요한 기본적인 기능들을 미리 구현해 놓은 추상 클래스
    * + extends"확장" : 부모가 추상클래스일 때 붙임. 부모로부터 기능을 "물려받을 때". 부모에게 기능을 상속받음(=재사용)
    *
    * [갑분 extends, implements]
    * extends = "기능을 그대로 물려받는다" (부모가 메서드를 이미 가지고 있음)
    * implements = "기능을 설계도로 받아서 내가 직접 만든다" (부모가 메서드의 이름만 알려줌)
    *
    * */

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    /*
    * 1) Set : 웹소켓에 연결된 모든 클라이언트의 세션 정보(WebSocketSession)를 저장하는 공간을 제공.
    * Set은 순서 유지도 없고(인덱스 안씀), 중복도 안되서 많이 안쓰긴 한다...
    * 2) private : 이 sessions라는 변수가 ChatHandler클래스 안에서만 접근 가능하다는 의미
    * 3) final : 한번 초기화되면 다시 할당할 수 없음을 의미
    *
    * */

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
//        System.out.println("🟢 연결됨: " + session.getUri());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        System.out.println("💬 받은 메시지: " + message.getPayload());

        // 연결된 모든 세션에 메시지 전송 (단, 메시지를 보낸 본인은 제외)
        for(WebSocketSession s : sessions){
//            System.out.println("너 누구냐 : " + s.getId() + ", 세션은 누구냐 : " + session.getId());
            if(s.isOpen() && !s.getId().equals(session.getId())){
                s.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        sessions.remove(session);
//        System.out.println("🔴 연결 종료!");
    }


}
