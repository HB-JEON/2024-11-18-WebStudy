<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="http://code.jquery.com/jquery.js"></script>
<script type="text/javascript">
$(function(){
	// nth-child() => 1번부터 시작
	// $('span:nth-child(1)').text("Hello JQuery")
	// 같은 태그 여러개가 존재할 경우 인덱스로 이용 => 0번부터 시작
	$('span:eq(0)').text("Hello JQuery")
	// textContent => html로 출력시에만 적용
	$('span:eq(1)').text("<font color=red>Hello JQuery</font>")
	// innerHTML
	$('span:eq(1)').html("<font color=red>아이디를 입력하세요.</font>")
	// appendChild() => append()
})
</script>
</head>
<body>
  <span>
    
  </span><br>
  <span>
  
  </span>
</body>
</html>