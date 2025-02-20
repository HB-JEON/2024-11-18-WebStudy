<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--
       BootStrap
       --------- 라이브러리
        => 업체에 지원하는 반응형 웹 CSS 라이브러리
       1. 출력범위 설정
          -----------
           container : 좌우 여백 => 960px
           container-fluid : 여백 없이 출력 => 풀화면
           
           <div class="container">
           <div class="container-fluid">
       2. 출력범위 안 설정
          -------------
          
          --------------------------
          
          -------------------------- row
          
          -------------------------- row
          |     |     |
          => 전체적으로 12등분
          col-sm-4 col-sm-8 => 12 => lg / sm / md / xs
 --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<style type="text/css">
.container {
	border: 1px solid red;
	height: 600px;
}
.row {
	background-color: cyan;
	border: 1px solid black;
	height: 300px;
}
.col-sm-8 {
	background-color: yellow;
	height: 300px;
}
.col-sm-4 {
	background-color: green;
	height: 300px;
	border: 2px solid red;
}
</style>
</head>
<body>
  <div class="container">
    <div class="row"></div>
    <div style="height: 10px">
    <div class="row">
      <div class="col-sm-8"></div>
      <div class="col-sm-4"></div>
    </div>
    <div class="row">
      <div class="col-sm-4"></div>
      <div class="col-sm-4"></div>
      <div class="col-sm-4"></div>
    </div>
  </div>
</body>
</html>