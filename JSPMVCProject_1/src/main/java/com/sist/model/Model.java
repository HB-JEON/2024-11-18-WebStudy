package com.sist.model;

import jakarta.servlet.http.HttpServletRequest;
// 모델은 사용자 요청 처리 / 처리 결과를 전송 / 어떤 JSP 전송할지
/*
    사용자 : 요청(주문)
    Controller : 요청 값(서빙) => Model 연결 => 결과값 전송
                   => 메뉴판을 알고 있어야한다.(application.xml)
    Model : 주문 처리(주방)
    View : 식탁
    
    실행 순서
    --------     주문(request)
     JSP(사용자) ==============> Controller(서빙)
                                  | 주문처리 => Model
                                    -------
                                     해당 모델을 찾는다.
                                       => 요청처리
                                            | request
                                            | request.setAttribute()
                                           => Controller 전송
                                               |
                                              JSP => JSTL/EL 화면 출력
     => 실무 사용 : JSP(View), Model
                              | 역할 => 데이터베이스 연동
                                       ----------
                                        MyBatis
                                        ------- 설정 : XML
      관련된 클래스(모델) => 통합(인터페이스)
                           서로 다른 클래스를 연결하여 사용
       => 클래스에서 메소드 여러개 사용 : 어노테이션
                                     --------- 구분자(인덱스)
                                                | 메소드 찾기 
                                                   @RequestMapping()
                                                      |
                                                   @getMapping()
                                                   @PostMapping()
                                                | 클래스 찾기
                                                   @Controller
                                                   @Repository
                                                   @Component
                                                   @Service
                                                | 멤버변수 찾기
                                                   @Autowired
                                                 => Spring
                                                 => Spring-Boot
       => 환경 설정 파일 : XML / Properties
 */
public interface Model {
	public String handlerRequest(HttpServletRequest request);
}
