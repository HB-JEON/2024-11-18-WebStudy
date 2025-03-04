<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
// 이항연산자
/*
    1. 산술연산자
         + , - , * , / , %
         + : 덧셈, 문자열 결합 => NaN(연산이 안된 경우)
             --------------
              let a=10+10 => 20
              let a="10"+"10" => 1010
         / : 0으로 나눌 수 없다
             정수/정수=실수
         % : 왼쪽 부호를 따라간다
             + % + => +
             - % - => -
             + % - => +
             - % + => -
    2. 비교연산자
    3. 논리연산자
 */
 window.onload=function(){
	// $(function(){}) , $(document).ready(function(){}) : Jquery
	// mounted():vue , componentDidMount() , useEffect():ready
	let a=5;
	let b=2;
	console.log("a+b="+(a+b)) // 7
	console.log("a-b="+(a-b)) // 3
	console.log("a*b="+(a*b)) // 10
	console.log("a/b="+(a/b)) // 2.5
	console.log("a%b="+(a%b)) // 1
	// 장바구니 계산 => select => 숫자(X) => 문자열 => *NaN
	let c="10"
	let f=20000
	console.log(c+f)
	console.log(parseInt(c)*f)
	/*
	   주의점
	     숫자가 아닌 경우 => + 외에 다른 산술 연산 => 연산 불가능 => NaN
	     "", '' => 문자열이다
	     HTML 통하여 숫자를 가지고 오는 경우 => 문자열
	*/
}
</script>
</head>
<body>

</body>
</html>