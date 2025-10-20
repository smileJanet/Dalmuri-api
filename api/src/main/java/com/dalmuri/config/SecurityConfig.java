package com.dalmuri.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/*
* [@Configuration]
* 해당 Java 파일이 'Spring 설정 클래스'임을 선언. Spring 컨테이너가 이 클래스를 스캔하여 @Bean으로 정의된 메소드를 실행하고 Bean을 생성함
*
* [@EnableWebSecurity]
* Spring Security 핵심 기능을 활성화하고, 웹 보안 설정을 이 클래스에서 설정함
*
* */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /*
    * [의존성 주입 DI]
    *
    * 1. private final ClientSecurityProperties securityProperties
    * 기존에 정의했던 ClientSecurityProperties Bean을 주입받음
    * Spring Boot는 생성자 주입 방식을 사용하는데, 컨테이너는 ClientSecurityProperties 객체를 생성하고,
    * 그 객체를 SecurityConfig에서 사용할 수 있도록 생성자 매개 변수로 전달
    *
    * 2. public SecurityConfig(){}
    * 이 클래스에 대한 기본 생성자 생성
    *
    * 3. this.securityProperties : private final ~ 에서 선언한 securityProperties
    * = securityProperties : ClientSecurityProperties에서 받은 값들을 사용할 수 있도록 대입함
    *
    * */

    private final ClientSecurityProperties securityProperties;

    public SecurityConfig(ClientSecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    /*
    * [보안 필터 체인 설정]
    * Spring security 핵심 보안 정책을 설정하는 곳
    *
    * 1. authorizeHttpRequests : 접근 권한 설정 영역
    * 1 - (1) .authorizeHttpRequests(auth -> auth ... ) : Http 요청에 대한 검증 시작을 선언
    * 1 - (2) .requestMatchers(...).permitAll() : ClientSecurityProperties에서 가져온 URL 목록에 해당하는 요청들은 인증 없이 접근을 허용
    * 1 - (3) .anyRequest().authenticated() : 앞에 규칙에 해당되지 않는 모든 요청들은 접근을 허용하도록 강제 (ex. 로그인 필요 etc)
    *
    * 2. CSRF 보호 설정
    * 2 - (1) .csrf(csrf -> csrf.disable()) : CSRF 보호 기능을 비활성화
    *   API 서버나 상태 비저장 아키텍쳐(ex. JWT 사용)에서는 CSRF보호가 불필요하거나 복잡도를 높여 비활성화함
    *
    * 3. 헤더 설정
    * 3 - (1) .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()) :
    *   X-Frame-Options 헤더 설정을 비활성화. 주로 iFrame 내에서 페이지 로드를 허용해야 할 경우에 사용됨
    *
    * 4. 반환
    * 4 - (1) return http.build() : 설정 보안이 모두 갖춰진 SecurityFilterChain 객체를 반환, Spring Bean으로 등록.
    *   실제 웹 요청을 처리하는 보안 필터 체인으로 사용
    *
    * */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(securityProperties.getPermitUrls().toArray(new String[0])).permitAll()
                        .anyRequest().authenticated())
                .csrf(csrf->csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())
                );

        return http.build();
    }
}
