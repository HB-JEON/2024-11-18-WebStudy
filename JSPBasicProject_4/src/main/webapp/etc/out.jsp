<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>JspWriter : 버퍼관리</h1>
1. 버퍼 관리 : <%= out.getBufferSize() %><br> <%-- 8 * 1024 --%>
2. 잔여 버퍼 확인 : <%= out.getRemaining() %><br>
3. 사용 중인 버퍼 확인 : <%= out.getBufferSize()-out.getRemaining() %>
4. 사용처 : 복잡한 HTML이 있는 경우 사용
 <table border="1" bordercolor="black" width="300">
  <tr>
    <td>Hello JSP</td>
  </tr>
  <tr>
   <td>
    <%
      for(int i=1;i<=2;i++)
      {
         out.println("&nbsp;&nbsp");
      }
    %>
    Hello JSP 2...
    <%
      if(true) // 오늘 날짜 => new
      {
    	  out.println("<sub>new</sub>");
      }
    %>
   </td>
  </tr>
 </table>
</body>
</html>