<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" buffer="16kb"%>
 <%--
     buffer="8kb" => 8*1024 => JSP 실행 시 저장되는 HTML을 저장해 두는 공간(메모리)
                               => 출력 버퍼
                                  -------- 브라우저에서 HTML을 읽어가는 공간
 --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
  총 버퍼 크기 : <%= out.getBufferSize() %><br>
  사용중인 크기 : <%= out.getBufferSize()-out.getRemaining() %>
  <%--
       잔여 버퍼 크기 확인 : out.getRemaining()
        크기 증가 => HTML 다 출력이 안되는 경우
        ------- 포털 사이트, 공기업
   --%>
</body>
</html>