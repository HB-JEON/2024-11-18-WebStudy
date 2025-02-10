<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
   // servlet 형식 => service 메소드 영역
   // response.setContentType("text/html;charset=UTF-8")
   // PrintWrite out=response.getWriter()
   out.write("<html>");
   out.write("<body>");
   out.write("<h1>Hello Servlet!!</h1>");
   out.write("</body>");
   out.write("</html>");
%>