package com.dalmuri.socket;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class MediachatHandler extends AbstractWebSocketHandler {
    /*
    * [개발 순서]
    * 연결 설정 → 텍스트 메시지 처리 → 바이너리 메시지(이미지, 동영상 등) 처리 → 연결 종료
    * */

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    /**
     * 세션으로부터 클라이언트가 전달한 userId를 추출하는 메소드
     * */
    private String getUserIdFromSession(WebSocketSession session){
        String query = session.getUri().getQuery();
        if(query != null && query.contains("id=")){
            return query.split("id=")[1];
        }
        return "unknown"; // 쿼리에 id없을 경우 unknown 반환
    }

    // 1. 연결 설정
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserIdFromSession(session);

        sessions.stream()
                        .filter(s -> getUserIdFromSession(s).equals(userId))
                                .forEach(existingSession -> {
                                    try{
                                        existingSession.close();
//                                        System.out.println("🔴 기존 세션 종료 (중복): " + existingSession.getId());

                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                });

        sessions.add(session);
        System.out.println("🟢 연결됨: " + session.getUri());
    }

    // 2. 텍스트 메시지 처리
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage msg)throws Exception {
        if (!session.isOpen()) {
//            System.out.println("세션이 닫혀있음: 메시지 처리 안함");
            return;
        }

//        System.out.println("💬 받은 메시지: " + msg.getPayload());

        for(WebSocketSession s : sessions){
            if(s.isOpen() && !s.getId().equals(session.getId())){
                s.sendMessage(msg);
            }
        }
    }

    // 3. 바이너리 메시지 처리
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage bMsg) throws Exception {
//        System.out.println("💬 받은 메시지: " + bMsg.getPayload());
        ByteBuffer byteBuffer = bMsg.getPayload();

        // 수신한 이미지를 저장하는 로직
        try(FileOutputStream out = new FileOutputStream("uploaded_files")){
            while(byteBuffer.hasRemaining()){
                out.write(byteBuffer.get());
            }
        }

        // 연결된 모든 세션에 바이너리 메시지 전송하기
        for(WebSocketSession s :  sessions){
            if(s.isOpen() && !s.getId().equals(session.getId())){
                s.sendMessage(bMsg);
            }
        }
    }

    // 4. 연결 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
//        if (sessions.remove(session)) {
//            System.out.println("🔴 연결 종료 - 세션 제거 완료: " + session.getId());
//        } else {
//            System.out.println("🔴 연결 종료 - 세션을 찾을 수 없음: " + session.getId());
//        }

    }

}
