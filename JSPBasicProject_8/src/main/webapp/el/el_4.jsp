<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.sist.vo.*"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
	SawonVO vo=new SawonVO();
	vo.setSabun(1);
	vo.setName("홍길동");
	vo.setDept("개발부");
	vo.setJob("대리");
	vo.setPay(3500);
	// ${} 출력 불가능
	/* 
	Model 1 
	 JSP=JSP => 간단, 재사용 불가능 => 소규모(홈페이지)
	Model 2
	 
	*/
	request.setAttribute("vo", vo);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<style type="text/css">
.container {
	margin-top: 50px;
}
.row {
	width: 560px;
	margin: 0px auto;
}
</style>
</head>
<body>
  <div class="container">
  <h3>사원 정보</h3>
  사번 : ${vo.sabun }, ${vo.getSabun() }<br>
  이름 : ${vo.name }, ${vo.getName() }<br>
  부서 : ${vo.dept }, ${vo.getDept() }<br>
  직위 : ${vo.job }, ${vo.getJob() }<br>
  연봉 : ${vo.pay }, ${vo.getPay() }<br>
 </div>
</body>
</html>