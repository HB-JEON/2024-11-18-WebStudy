<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
  <table border="1" bordercolor="black" width="500" heigth="600">
    <tr>
      <td colspan="2" align="center" height="100">
       <%
         pageContext.include("a.jsp");
       %>
      </td>
    </tr>
    <tr>
      <td width="250" height="400">
       <%
         pageContext.include("b.jsp");
       %>
      </td>
      <td width="250" height="400">
       <%
         pageContext.include("c.jsp");
       %>
      </td>
    </tr>
    <tr>
      <td colspan="2" align="center" height="100">
       <%
         pageContext.include("d.jsp");
       %>
      </td>
    </tr>
  </table>
</body>
</html>