<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.io.*, com.sist.dao.*"%>
<%-- _ok : 데이터베이스 연동
      => list.jsp 이동
 --%>
<%
   /*
      server => context.xml
      <Context allowCasualMultipartParsing="true" path="/">
      <Resources cachingAllowed="true" cacheMaxSize="100000"/>
      </Context>
      
      XML 파일은 지정된 속성만 사용 가능
      ------- 대소문자를 구분
              지정된 태그 / 속성을 정의하고 있는 파일
              .DTD
              속성은 반드시 ""을 사용
      웹 개발
      ------ XML / JSON => 교재 X, 교정에 미포함
             --- 처리방식
             
             => XML을 이용하는 라이브러리
                MyBatis / Spring
             => JSON
                Ajax / VueJS / ReactJS / NextJS
                 => 자바에서 만들어 전송
                    jackson / simple-json
   */
   // 오라클에 데이터 첨부
   DataBoardVO vo=new DataBoardVO();
   DataBoardDAO dao=DataBoardDAO.newInstance();
   
   // 사용자가 보내준 데이터
   String name=request.getParameter("name");
   String subject=request.getParameter("subject");
   String content=request.getParameter("content");
   String pwd=request.getParameter("pwd");
   
   // 사용자가 데이터 전송 request.getParameter("name");
   // 사용자가 파일 전송 request.getPart("upload");
   
   Part filePart=request.getPart("upload"); 
   String fileName=filePart.getSubmittedFileName();
   if(fileName==null || fileName.equals("")) // 업로드가 안된 상태
   {
	   vo.setFilename("");
	   vo.setFilesize(0);
   }
   else // 업로드가 되는 상태
   {
	   String uploadDir="c:\\upload";
	   File file=new File(uploadDir, fileName);
	   // 오라클 => 파일 업로드
	   // try ~ resource => 자동으로 input/output
	   try(InputStream input=filePart.getInputStream();
			   FileOutputStream output=new FileOutputStream(file))
	   {
		   byte[] buffer=new byte[1024];
		   int i=0;
		   while((i=input.read(buffer, 0, 1024))!=-1)
		   {
			   output.write(buffer, 0, i);
		   }
	   }
	   vo.setFilename(fileName);
	   vo.setFilesize((int)file.length());
	   
   }
   vo.setName(name);
   vo.setSubject(subject);
   vo.setContent(content);
   vo.setPwd(pwd);
   dao.databoardInsert(vo);
  /*
   System.out.println("name : "+name);
   System.out.println("subject : "+subject);
   System.out.println("content : "+content);
   System.out.println("pwd : "+pwd);
   System.out.println("fileName : "+fileName);
   */
   response.sendRedirect("list.jsp");
%>