package com.dalmuri.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MediaConfig implements WebMvcConfigurer {

    /*
    * [WebMvcConfigurer]
    * 스프링 MVC 동작을 사용자화할 수 있는 인터페이스.
    * 직접 MVC 설정을 조절하고 싶을 때 이 인터페이스를 구현해서 원하는 메소드를 오버라이드함.
    * 
    * */

    @Value("${file.upload-dir}") // 민감한 정보 보호하려고 ^^
    private String dir;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        
        /*
        * [addResourceHandlers(ResourceHandlerRegistry registry)] 
        * 웹에서 접근 가능한 URL경로와 실제 서버(파일 시스템)의 물리적 위치를 연결(mapping)하는 작업
        * 
        * */

        registry
                .addResourceHandler("/uploads/**") // http://localhost:3001/upload/파일명.jpg 라고 web에서 열려야 한다.
                .addResourceLocations("file:" + dir); // 실제 파일이 있는 경로

        /*
        * 1) registry.addResourceHandler() : 웹 URL에서 접근할 경로를 지정
        *   웹 브라우저에서 http://localhost:3001/uploads/파일명 형식으로 접근하면 매핑이 됨.
        *
        * 2) registry.addResourceLocations() : 실제 파일이 저장된 물리적 경로를 지정
        * "file:C:/Dalmuri-api/api/uploads"는 윈도우 컴퓨터 내의 폴더 경로를 뜻함.
        *
        * 웹에서 여는 방식 : http://localhost:3001/uploads/파일명.jpg
        * 실제 사진이 있는 물리적 장소 : C:\Dalmuri-api\api\\uploads\파일명.jpg
        * 기존의 addResourcesLocations는 files:uploads여서 오류가 났는데, 위와 같이 바꾸니 http://localhost:3001/uploads/파일명.jpg으로 해도 잘 보였다.
        * 물리 경로를 제대로 입력하지 못하면 403에러(권한 없음 에러)가 난다. 내가 Spring Security를 깔아서...
        *
        * */

    }

}
