<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--
    174page => response(서버 응답)
       1. 클래스 : HttpServletResponse
       2. 역할
           1) 서버 응답
               => HTML
                   setContentType("text/html;charset=UTF-8")
               => Cookie
                   addCookie(cookie)
                   ** 동시 전송 불가능 => 한개씩 가능
               => setHeader() : 다운로드
           2) 화면 이동
               sendRedirect("이동할 파일명") => GET 방식만 사용 가능
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