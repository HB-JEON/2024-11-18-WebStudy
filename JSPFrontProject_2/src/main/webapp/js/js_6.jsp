<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
// 비교연산자 => 조건문 => 결과값은 true / false
/*
     비교연산자 , 논리연산자 , 부정연산자 => if / for 주로 사용
     모든 데이터형 비교 가능
         ----------- 숫자 / 문자 / 날짜 / 논리형
                     ---                -----
                      |                   |
                      --------------------- 자바
     if(name=="") name > yname
     
     연산자의 종류
       == , != , < , > , <= , >=
 */
 window.onload=function(){
    	   let a=10;
    	   let b=5;
    	   
    	   console.log("a==b"+(a==b)) // false
    	   console.log("a!=b"+(a!=b)) // true
    	   console.log("a<b"+(a<b)) // false
    	   console.log("a>b"+(a>b)) // true
    	   console.log("a<=b"+(a<=b)) // false
    	   console.log("a>=b"+(a>=b)) // true
       }
</script>
</head>
<body>

</body>
</html>