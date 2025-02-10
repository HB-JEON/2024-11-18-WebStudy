<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--
     JSP : Java Server Page => 서버에서 실행되는 자바 파일
            = Back-End
            = JSP : Servlet 쉽게 작성이 가능하게 만든 파일
                     => 영역 : doGet/doPost
                     => 메소드 영역 => 대부분은 지역변수
                     => JSP는 View 역할(화면 출력)
                     => Front-End로 전향
            = Servlet : 보안 (라이브러리, 보안 파일, 연결)
                             -------- 스프링
            Back-End : Java / oracle / servlet / spring
            Front-End : HTML / CSS / JavaScript / JSP
            --------- + Full Stack
     JSP => HTML / CSS / JavaScript / Java / Oracle 연동
                         ----------
         1. HTML + JAVA : 구분
             자바를 코딩하는 영역 : 스크립트
             <%
             	일반 자바 : 메소드 호출 / 변수 설정 / 제어문...
             %> : 스크립트릿
             
             <%= %> : 표현식 => out.println()
                      데이터 출력
             <%! %> : 선언문 => 전역변수, 메소드 제작
             *** 스크립트를 벗어나면 일반 문자열
             
         2. 동작방식 => 정적 페이지 / 동적 페이지
                       ---------   --------- 한개의 파일을 이용하여 데이터를 변경 => JSP / PHP / ASP / CGI
                        데이터 변경X(HTML)									   ---------------
         
         3. JSP => 사용법
             = 지시자
                page : JSP에 대한 정보 저장
                        1) 변환 => 브라우저 출력시 어떤 형식으로 출력할지 지정
                            HTML => text/html
                            XML  => text/xml
                            JSON =>text/plain
                            -----------------
                             자바 스크립트 객체 표현법
                              => JavaScript Object Notation
                              => 자바 : VO => 자바스크립트에서는 VO 인식 불가능
                                  VO => {변수:값...}
                                  let sawon={}
                                  List => [] => Array
                                  ------------------- 자바 변환 후 전송
                                                       => Ajax / Vue / React
                        2) 라이브러리 읽기 : import
                        3) 한글 변환 : pageEncoding
                        4) 에러페이지 : errorPage
                taglib : 자바 / HTML을 분리한 경우에 주로 사용
                          JSTL / EL
                   <%
                   		for()
                   		{
                   %>
                   		HTML <%= %>
                   <%
                   		}
                   %>
                   
                   <c:forEach>
                     <html> $ {}
                   </c:forEach>
                include
             = 자바 코딩 방법
                <% %> : 스크립트릿
                        일반 자바 (메소드 안에 코딩되는 자바)
                        메소드 호출 / 지연변수 선언 / 연산자 처리
                <%= %> : 표현식 => out.println()
                         데이터 출력
             ------------------------- _jspService 안에 첨부
                <%! %> : 선언식 : 전역 변수, 메소드 선언시 사용
             ------------------------- HTML 외의 자바를 코딩하는 영역
               *** 해당 영역에서는 자바 문법을 기준으로 한다.
                <% %>
                <%! %>
                ---------------- 문장이 끝나면 ; 사용
                <%= %>
                ---------------- ; 사용 시 오류
                 out.println( [<%= %> 코딩 위치] );
             = 지원 라이브러리(내장 객체)
                    
				final jsp.PageContext pageContext;
		        HttpSession session = null;
		        ServletContext application;
			    ServletConfig config;
			    jsp.JspWriter out = null;
			    final java.lang.Object page = this;
             
                request / response / session / out
                application / pageContext
                              ----------- 웹 흐름
                               include / forward
                                  |         |
                                  ----------- 핵심 기술
                       화면 이동
                       --------
                       
                config / exception / page
                ---------------------------------- 9개 사용 가능
                 미리 객체를 생서한 다음 필요시마다 사용 가능한 객체
             = Cookie / Session : 일부 정보를 기억(메모리 공간)
               ------   ------- 서버
                브라우저
             = JSP 액션태그
                <jsp:include> : JSP 특정 영역에 다른 JSP 포함

                <jsp:useBean> : 클래스의 메모리 할당
                   <jsp:useBean id="dao" class="BoardDAO"
                                     |   -----------------
                                    객체명  메모리 할당 대상
                      => BoardDAO dao=new BoardDAO()";
                <jsp:setProperty> : 멤버변수에 값을 채우는 경우
                <jsp:parma> : 다른 JSP로 값을 전송 시 사용
                
                => 자바 HTEML
             = JSTL / EL
             = MVC => MV => MVC
            
            => JSP 브라우저에서 실행
                   --------------
                    => 톰캣 실행 => 클래스로 변경
                     a.jsp => a_jsp.java => 컴파일 => a_jsp.class
                                                     -----------
                                                     서블릿 파일
                                                       |
                                                     인터프리터
                                                       |
                                                     HTML만 메모리에 저장
--%>
<%--
	  public void _jspService(http.HttpServletRequest request,
	   http.HttpServletResponse response)
	   {
	   final jsp.PageContext pageContext;
       HttpSession session = null;
       ServletContext application;
	   ServletConfig config;
	   jsp.JspWriter out = null;
	   final java.lang.Object page = this;
	   }
 --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%!
    int a=10;
	public void display() {}
%>
<% 
  String name="홍길동";
  // 자바 문법 적용
  %>
<%= name %>
</body>
</html>