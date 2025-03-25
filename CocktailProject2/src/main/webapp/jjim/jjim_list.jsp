<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
  <table class="table">
   <c:forEach var="vo" items="${list }">
    <tr>
     <td class="text-center">${vo.jno }</td>
     <td class="text-center">${vo.cpvo.name }</td>
     <td>
       <img src="${vo.cpvo.poster }" style="width: 30px; height: 30px">
     </td>
     <td class="text-cneter">${vo.cpvo.price }</td>
    </tr>
   </c:forEach>
  </table>
</body>
</html>