<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 
     18page
     ------
      1) 웹 동작
          요청 / 응답
          ---   ---- 서버 (response) => C/S => 네트워크
           클라이언트 (request)
            => 서버 연결시 브라우저를 이용
               ---------------- 브라우저에서 서버 연결되는 부분
                                ------------------------- 주소창
            => 반드시 URL 이용할 때는
            http://서버IP:PORT/프로젝트명/폴더명/파일명
                                             -----
            파일명 생략이 가능한 파일 : welcom 파일
            index.html / index.jsp
            ** 파일의 확장자는 개발자가 설정 가능
                .회사명 (.naver, .daum, .do)
            => 나머지는 반드시 파일명을 첨부
            
            http://localhost/JSPBasicProject_1/jsp/basic_2.jsp
            -------------------------------------------------- URL
            ---------------- ---------------------------------
               서버 정보                    URI
                             -----------------
                                  ContextPath
                   |                        |
                   --------------------------
                               request
            사용자가 html / jsp / servlet 요청
            서버에서는 출력된 HTML을 브라우저로 전송 실행
                     ---------- 소스보기
            
            => JSP 활용하는 이유 : 동적 페이지
                          한개의 파일을 이용하여 데이터 변경 후 출력
                          --------- 여러개처럼 사용 가능
               서블릿 =====> JSP
                       | 사용 편리
               서블릿 =>  HTML 출력에 불편
                         수정시마다 컴파일, .class파일(보안성 높음)
                         로직, 중요한 데이터가 있는 경우
                HTML을 그대로 사용
                수정시에 컴파일 없이 실행 => JSP
                => 자바로 변경 (톰캣에 의해 변경)
                => 소스가 노출 (보안에 취약)
                => 화면 출력에 주력
          구성요소
           브라우저 : 크롬 / 파이어폭스 / IE
           웹서버 : Apache / IIS
           웹 애플리케이션 : 자바번역 / JSP 자바로 변경
                           => tomcat
                           => 테스트용 웹서버를 내장
           데이터베이스 : 오라클 / MySQL
           
           동작
           ---
             1. 사용자가 요청한 JSP 파일 찾기
                 = 없는 경우 : 404 전송
                 = 있는 경우 :
             2. JSP에 해당되는 JAVA 파일이 생성 되어있는지 확인
                 = 자바 파일이 있는 경우
                 = 자바 파일이 없는 경우
                 a_jsp.java
                 
                 main.jsp => main_jsp.java
                 a_1.jsp => a_005f.java
                 
                 => 파일명은 숫자로 시작 X
             3. 컴파일 : a_jsp.class
                인터프리터 : 한줄씩 번역
                           ----------
                            자바는 삭제
                            out.wirte("<html>");
                             |
                            브라우저에서 읽어가는 메모리에 저장
     19page
     ------
      파일 => 웹(페이지)
      상용화된 파일 => 75page
      => 자바 파일
      => 정적 페이지 : HTML/CSS => 데이터 갱신이 불가능
      => 동적 페이지 : JSP/Servlet => 자바가 데이터 관리
                                    필요 시에 데이터 갱신
                      => CGI, ASP, PHP, Django
                              ---  ---
                               |    |
                               ------
                                교보정보통신(2차) : 서울시청
                                 C# + ASP
    22page
    ------
     Servlet / JSP
     -------------
      1. 자바 기본문법
      2. 오라클 연동
      3. HTML/CSS
      ---------------
      4. 자바스크립트
      --------------- + JSP
       Servlet
       Server + let
        : 서버에서 실행되는 가벼운 프로그램
           Applet / MIDlit
        : 확장자 .java
        : 자바만 이용하는 웹 프로그램(자바 웹 프로그램의 시작)
        : 2000(servlet) => 2002(jsp)
        : 단점 => 변경된 소스를 바로 확인하기 어렵다.
                  : 컴파일 후 확인 가능
          장점 => 보안, 보안과 관련된 웹파일
          라이브러리 => 서블릿으로 제작 (Spring, Spring-Boot)
          
        : 서블릿 => JSP가 컴파일이 될 경우 서블릿으로 변경
                   -------------------------------- 톰캣, 레진
        : 서블릿, JSP => HTML+Java
        : 분리 작업
          -------
           ThymeLeaf (HTML)
                      ---- 제어문 사용
                           사용빈도 낮음 : JSP보다 속도 느림
          최근
          ---
           React == Spring
            Vu   == Spring  => 여러개를 만들어서 한번에 처리 MSA
           ---------------
            => CDN 기반
            ---------- JDK / Tomcat / 이클립스 => .zip
     61page
     
            
            
--%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
  URL : <%= request.getRequestURL() %><br>
  URI : <%= request.getRequestURI() %><br>
  ContextPath : <%= request.getContextPath() %><br>
  Server 정보 : <%= request.getServerName() %><br>
  ServerPort : <%= request.getServerPort() %>
</body>
</html>