<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.io.*"%>
<%
   // 1. 다운로드 할 파일명
   String fn=request.getParameter("fn");
   try
   {
	   File file=new File("c:\\upload\\"+fn);
	   response.setContentLength((int)file.length());
	   // 자동으로 프로그래스바 실행
	   response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fn, "UTF-8"));
	   BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));
	   BufferedOutputStream bos=new BufferedOutputStream(response.getOutputStream());
	   
	   int i=0;
	   byte[] buffer=new byte[1024];
	   
	   while((i=bis.read(buffer, 0, 1024))!=-1)
	   {
		   bos.write(buffer, 0, i);
	   }
	   bis.close();
	   bos.close();
   }catch(Exception ex) {}
%>