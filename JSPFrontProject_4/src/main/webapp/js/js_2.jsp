<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--
      내장 객체
      --------
       window : 브라우저 자체 제어
                 open
                 close
       document : write
                  querySelector() => 태그 읽기
       history : back, go(-1)
       location : href="이동할 파일명"
       
      데이터형
      -------
       Number => 형변환 / parseInt()
       ------ toLocaleString()
       String => "abcd" => 0부터
        length() = 문자 개수 : 비밀번호 유효성 검사
        indexOf / lastIndexOf : 문자 위치 찾기
        replace() => 문자 변경
        split() => 문자 분리 => 배열로 저장
        substring() : 문자 자르기
        *** substr(number(시작 인덱스 번호), number(개수)) => 오라클
        trim() : 좌우 공백 제거
       Array
        push() => 데이터 첨부
        pop() => 데이터 삭제
        slice() => 원하는 위치의 데이터를 잘라서 새로운 배열 생성
        length => 데이터 개수
       Date : 날짜 시간 관리
        let today=new Date()
        year : 년도 => today.getFullYear()
        month : 월 => today.getMonth() => 0부터
        date : 일 => today.getDate()
        daye : 요일 => today.getDay()
       Math
        round() => 반올림
 --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
/*
    코딩 테스트 : 차량번호 임의로 등록 => 오늘 날짜 쉬는 날 여부 
 */
window.onload=function(){
	let today=new Date()
	let year=today.getFullYear()
	let month=today.goetMonth()+1
	let date=today.getDate()
	let day=today.getDay()
	let strWeek=["일","월","화","수","목","금","토"]
	document.write("오늘은 "+year+"년 "+month+"월 "+date+"일 "+strWeek[day]+"요일 입니다.")
	// 자바스크립트 기초
</script>
</head>
<body>

</body>
</html>