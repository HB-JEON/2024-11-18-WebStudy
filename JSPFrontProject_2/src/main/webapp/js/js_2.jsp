<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 변수 선언 / 출력 형식 / 주석 처리 --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
// 한줄 주석

/*
   여러줄 주석
   에러 = 어시스트가 안된다.
 */
 // 변수 선언
 window.onload=function(){
	// 메인 함수
	let a=10;
	let b=10.5;
	let c='ABC';
	let d="ABC";
	let e=[1,2,3,"홍길동","남자"]; // 혼합 가능
	let f={name:"심청이",age:30};
	// ; 생략 가능
	/*
	   1. number : 정수, 실수 => 숫자형
	   2. string : 문자열('',"")
	   3. object : {} => 객체, []
	   4. boolean : true / false
	                 |      0, 0.0
	                나머지는 true
	   5. null : 값이 없는 상태
	   6. undefined : 변수 선언이 없는 경우
	       => 데이터형 확인 : typeof
	*/
	// 출력
	console.log("a="+a)
	console.log("b="+b)
	console.log("c="+c)
	console.log("d="+d)
	console.log("e="+e)
	console.log("f="+f)
	
	// 브라우저에 출력
	document.write("a="+a+"<br>")
	document.write("b="+b+"<br>")
	document.write("c="+c+"<br>")
	document.write("d="+d+"<br>")
	document.write("e="+e+"<br>")
	document.write("f="+f+"<br>")
	document.write("<br>")
	document.write("<h3>변수의 데이터형</h3>")
	document.write("a="+typeof a+"<br>")
	document.write("b="+typeof b+"<br>")
	document.write("c="+typeof c+"<br>")
	document.write("d="+typeof d+"<br>")
	document.write("e="+typeof e+"<br>")
	document.write("f="+typeof f+"<br>")
	document.write("i="+typeof i+"<br>")
	document.write("<br>")
	document.write("<h3>데이터형의 변경</h3>")
	let k=10;
	document.write("k="+typeof k+"<br>")
	k="Hello"
	document.write("k="+typeof k+"<br>")
	k={msg:"Hello",len:5}
	document.write("k="+typeof k+"<br>")
	k=["Hello","JavaScript"]
	document.write("k="+typeof k+"<br>")
}
</script>
</head>
<body>

</body>
</html>