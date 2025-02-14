<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--
      Cookie
       HTTP 프로토콜은 새로운 페이지 실행 시 기존의 입력된 모든 정보는 삭제
       ----------- 로그인 => 새로운 페이지로 이동 (로그인 정보 삭제)
                   지속성(유지성) X
                   --------------
                    정보가 유지될 수 있게 만든 라이브러리 - Session / Cookie
                                                      ----------------
                                                       장바구니, 예약정보
        => 상태의 지속성 (Cookie / Session)
           --- 데이터의 변경 상황
           React / Vue => state 관리 프로그램(데이터 상태 프로그램)
        => 데이터를 유지하기 위한 메모리 저장공간
           브라우저(클라이언트)
        => 사용법
            1. 저장
                new Cookie(키, 값)
                          (String key, String value)
                        => Map 방식으로 사용
                        => key 중복 불가능 (중복된 키는 저장 X)
                ## response
                    => 한개의 JSP 한가지 일만 수행
                       --------- -------
                                  HTML 전송   => 2
                                  Cookie 전송 => 1
                                   => Cookie 여러번 전송 가능
            2. 읽기 : Cookie[] cookies=request.getCookies()
                     @CookieValue => Spring (로드존슨)
                       | Annotation
            3. 삭제 : setMaxAge(0)
            4. 저장기간 : setMaxAge(초 단위) = 60*60*24 => default : 30분
            5. 저장된 데이터 읽기
                => 키 읽기 : getName()
                => 값 읽기 : getValue()
            6. 저장경로 설정
                setPath("/")
            7. 저장할 수 있는 데이터 : String
            8. port가 다르면 서버에서 Cookie를 다룰 수 없다.
                
               React     Spring-Boot
                 | 3000port    | 80port / 8080port
               => 자바스크립트 쿠키를 이용
                  --------------
                   React-Cookie
            9. 자동 로그인 (remember-me)
 --%>
<%
    // 사용자 전송 값
    String fno=request.getParameter("fno");
    // 전체 쿠키 읽기
    Cookie[] cookies=request.getCookies();
    if(cookies!=null)
    {
    	for(int i=0;i<cookies.length;i++)
    	{
    		if(cookies[i].getName().equals("house_"+fno))
    		{
    			cookies[i].setPath("/");
    			cookies[i].setMaxAge(0);
    			response.addCookie(cookies[i]);
    			// 브라우저 전송
    			break;
    		}
    	}
    }
    
    // 새롭게 쿠키를 추가
    Cookie cookie=new Cookie("house_"+fno, fno);
    cookie.setPath("/");
    cookie.setMaxAge(60*60*24);
    response.addCookie(cookie);
    
    // 화면 이동
    response.sendRedirect("detail.jsp?fno="+fno);
%>