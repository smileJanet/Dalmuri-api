package com.dalmuri.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration // @Component를 사용해도 상관은 없지만, 설정과 관련된 파일임을 명시하기 위해 @Configuration을 사용하는게 관례이긴 하다.
@ConfigurationProperties(prefix="security")
public class ClientSecurityProperties {

    /*
    * [ClientSecurityProperties]
    * 1. application.yml의 값을 Java 객체로 바인딩
    * application.yml 혹은 application.properties의 값을 자바 코드 내에서 List<String> 자료구조로 사용하려면 특정 객체의 필드에 매핑하는 과정이 필요하다.
    * @ConfigurationProperties(prefix="security") : application.yml의 security : 아래의 모든 속성을 이 클래스에 매핑시킨다.
    * private List<String> permitUrls; yaml의 permit-urls 목록이 이 필드에 자동 바인딩 되도록 한다.
    *
    * 2. Spring Bean 으로 등록 및 의존성 주입(DI)
    * SecurityConfig에서 permitUrls목록을 사용하려면, 해당 목록을 담고 있는 객체(=ClientSecurityProperties의 인스턴스)가 Spring IoC 컨테이너에 의해 관리되는 Bean이어야 한다.
    * @Configuration : 지금 이 클래스를 '설정 클래스'로 만들고, Spring 컨테이너에 등록되게 함.
    * SpringConfig의 생성자에서 ClientSecurityProperties securityProperties를 매개변수로 받아 의존성 주입(DI)를 실행할 수 있도록 한다.
    *
    * 3. 동적인 목록 사용
    * 목록이 아닌 단일 값(String)이라면 @Value를 이용할 수도 있지만,
    * 지금과 같이 여러개(chat, uploads, diary)를 관리하는 상황일 땐(List<String>) @ConfigurationProperties를 사용하는 것이 더 권장됨
    *
    * 결론 : SecurityConfig.java와 application.yml의 '중간 다리' 역할
    *
    * */

    /*
    * [필드부 Private Field]
    * 이 ClientSecurityProperties.java에서만 접근할 수 있는 List<String> 타입의 변수
    * application.yml 내 security.permit-urls 키에 해당하는 변수들을 받아서 사용하는 저장소 역할
    * private로 선언된 이유: 캡슐화를 위해! getter-setter로만 접근할 수 있도록 한다.
    *
    * */
    private List<String> permitUrls;

    /*
    * [기본 생성자]
    * 기본 생성자의 형태 : public ClientSecurityProperties(){}
    * 지금은 Java Compiler가 자동으로 기본 생성자를 만들어준 상태
    * setter 방식 바인딩을 위해서라도 @ConfigurationProperties는 기본 생성자가 있어야 한다.
    *
    * +)
    * [Java Compiler vs JVM]
    * 1. Java Compiler : .java → .class 변환, 컴파일 시점에 실행
    * 2. JVM : .class 실행, Run 시점에 실행
    *
    * */

    /*
    * [Getter]
    * 해당 필드에 대입된 값을 반환하는 메소드
    *
    * */
    public List<String> getPermitUrls() {
        return permitUrls;
    }

    /*
    * [Setter]
    * 해당 필드에 대입하고자 하는 값을 전달받아 필드에 대입시켜주는 메소드
    * = 를 기준으로
    * this.permitUrls : 필드부의 private List<String< permitUrls를 의미
    * = permitUrls : Spring boot가 application.yml을 파싱한 후 이 setter을 호출한다.
    *
    * 실행 흐름:
    *   (1) Spring boot run
    *   (2) application.yml 읽음
    *   (3) security.permit-urls 발견
    *   (4) setPermitUrls() 호출하여 자동으로 바인딩
    *
    * */
    public void setPermitUrls(List<String> permitUrls) {
        this.permitUrls = permitUrls;
    }
}
