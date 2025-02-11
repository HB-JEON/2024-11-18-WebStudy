<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--
      165page
        내부객체, 내장객체, 기본객체 => 미리 선언된 객체
        public void _jspService(HttpServletRequest request, HttpServletResponse response)
        {
        
	         ServletContext application;
			 ServletConfig config;
			 JspWriter out = null;
			 Object page = this;
		     PageContext _jspx_page_context = null;
		     
		     try
		     {
		       --------------JSP--------------
		         메소드 처리 내용 생성 => 파일 형식으로 제공
		       -------------------------------
		     
		     }catch(Exception e) {}
		     
        }
      JSP는 파일 X => _jspService 메소드 안을 코딩하는 부분
                     ----------- 메소드 구현
       => 지역변수와 비슷하다. 
       
      내부객체의 종류
        *** 1. request
          HttpServletRequest 객체
          ----------------------
           1) 역할
              http://localhost/JSPBasicProject_3/jsp/basic_1.jsp
              ---------------- --------------------------------- 클라이언트 요청 파일(URI)
               서버 정보         ---------------- ContextPath
               --------------------------------------- URL
           
               = 서버 정보
                  서버 이름, 프로토콜, 전송 방식(GET/POST)
                  URL 정보
               = 브라우저 정보
                  클라이언트의 IP / 클라이언트의 PORT
               = 사용자의 요청 정보
               
           2) 주요 메소드
               = 서버 정보 관련 메소드
                  서버 이름 : getServerName() => localhost
                  프로토콜 : getProtocol() => 80
                  URL : getRequestURL()
                  *** URI : getRequestURI()
                  *** ContextPath : getRequestContextPath()
               = 브라우저 정보 관련 메소드
                  IP : getReomteAddr() => 접속자 IP
                  PORT : getPort()
               = 사용자 요청 관련 정보
                  *** 사용자 서버로 요청 데이터를 보낼 때 톰캣에의해서 request에 저장
                  request.setAttribute("key","값")
                  -------------------------------- 여러개 사용 가능
                   ?name=aaa&sex=man&age=20
                   request.setAttribute("name", "aaa")
                     => getParameter("name")
                   request.setAttribute("sex", "man")
                     => getParameter("sex")
                   request.setAttribute("age", "20")
                     => getParameter("age")
                     
                   ?hobby=aaa&hobby=bbb&hobby=ccc
                   String[] hobby=request.getParameterValues("hobby")
                   
                   => 단일값 : getParameter()
                   => 다중값 : getParameterVaues()
                   => 한글이 깨지는 경우 : setCharacterEncoding()
                   
                   http://localhost/JSPBasicProject_3/jsp/basic_1
                   
                   => 웹에서 한글 전송 : byte[]
                                        | 인코딩
                      %ED%99%BD%EA%B8%B8%EB%8F%99                     
                                        | 디코딩
                                      홍길동
                   ** Window 11 => 자동 디코딩
                   
                   => JSP 메소드 영역에 코딩
                      -------------------
                       => 매개변수 전송 불가능
                          -----------------
                           웹 URL 이용하여 전송
                           main.jsp?no=10 => main.jsp에 no변수 전송
                           --------
                            받는 JSP
                            
                   <form action="main.jsp">
                                 -------- 보내준 모든 데이터를 받는다.
                   => request는 화면 변경/새로고침 => 자동 초기화
                   
                   public void display(int a) {}
                   
                   display(10)
                   display(20)
                   display(30)
           
        *** 2. response
        * 3. application
        * 4. out
        *** 5. session
        * 6. pageContext
        7. page
        8.config
        9.exception
 --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>