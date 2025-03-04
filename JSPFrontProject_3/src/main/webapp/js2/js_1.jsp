<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--
      자바스크립트는 브라우저에서 실행 => 인터프리터 => 에러 출력 X
       => 개발자 도구
       
      1. 변수 => 대입값에 따라 데이터형이 변경
          let (지역변수) / const (상수)
          => number : 숫자형 (int, double)
          => string : 문자형 (char, string)
          => object : 배열 / 객체
                       []    {}  =>  JSON
          => function : 함수
      2. 연산자
          => 단항연산자
              1) 증감연산자 ++, --
              2) 형변환연산자 
                   숫자변환 : Number("10"), parseInt("10")
                   문자변환 : String(10) => "10"
                   논리변환 : Boolean(1) > true
                             Boolean(0) > false
                   
                   => HTML 태그에서 읽는 값 => String
                      -----------------------------
          => 이항연산자
             산술연산자 : +  => 문자열 결합 / 덧셈
                         -  =>  0으로 나누면 Infinity, 정수/정수 = 실수
             비교연산자 : true
                         문자 / 숫자 / 날짜 => 비교가능
                         엄격한 기준 => 권상 사항
                         ===, !== : VueJS / ReactJS
                                    --------------- 경고
                         
                         5=='5' => true
                             - parseInt('5')
                         5==='5' => false
             논리연산자 : &&(범위 포함) , || (범위 불포함)
             대입연산자 : = , += , -=
             삼항연산자 : (조건) ? 값1:값2
                           true => 값1
                          false => 값2
             => 문자열 결합 / 상품 갯수 => 총합(오라클)
             => 모든 처리 => 자바(서버)
             => 자바스크립트 / HTML => 출력
                ----------   ---- 데이터 변경(정적페이지)
                 | 데이터 변경 가능(동적페이지)
      3. 제어문
          조건문 : if
           if(조건문)
           {
           }           
           if(조건문)
           {
              조건 true
           }
           else
           {
              조건 false
           }
          반복문 : for / for-each
           => 일반 for
               for(let i=0;i<10;i++)
               {
                  반복 수행문
               }
               1-2-3-4
                 2-3-4
                 2-3-4
                 - false면 종료
            => for-each => 배열일 경우에만 사용 가능
               for(변수 in 배열)
                   --- 배열의 index 번호 => 0
               for(변수 in 객체)
                   --- 객체의 key 값
               let arr=[1,2,3,4,5]
               for(let i in arr)
                   --- 0,1,2,3,4
               let sa={sabun:1,name:"",sex:""}
               for(let key in sa)
                   ------- sabun, name, sex
               -------------------------------
               for(변수 of 배열)
                   --- 배열의 실제 저장된 값
                   
               => forEach
                배열.forEach(function(value){})
                            ---------------
                             callback => 자동 실행되는 함수
                let arr=[1,2,3,4,5]
                arr.forEach(function(value){})
                                     ----- 1 2 3 4 5
               => map
                배열.map(function(value){})
                            ---------------
                             callback => 자동 실행되는 함수
                let arr=[1,2,3,4,5]
                arr.map(function(value){})
                                     ----- 1 2 3 4 5
          반복제어문 : break => 반복문 중단 => 출력 개수를 정한 후 서버에서 전송
      
      4. 함수 : 기능처리(이벤트 : 사용자가 행위를 했을 때)
                       -----   -------------------- 사용자에 대한 처리
                       | 키보드, 마우스                브라우저에서만 작동
                          onclick / onmouseover
                          onchange(select) / onmouseout
                          onkeydown / onkeyup...
          => 함수 생성 방법
              선언적 함수
               => 리턴형 서술 X
               => 매개변수에 변수명 설정 (name) => (let name) X
               function 함수명(매개변수)
               {
                  function 함수명() => X
               }
              익명의 함수(***)
               let 함수명=function(){}
               let 함수명=()=>{}
               
               => 사용자 정의 함수 => 자동 호출 불가능
               => 호출 시기
         
         --------------------------------------------------- 
                            리턴형       매개변수
         --------------------------------------------------- 
                              O            O
                          function func(name)
                          {
                             return name
                          }
                          
                          let name=func("홍길동")
         --------------------------------------------------- 
                              O            X
                          function func()
                          {
                             retrun ""
                          }
                          
                          let msg=func()
         --------------------------------------------------- 
                              X            O
                          function func(name)
                          {
                              ...
                              ...
                          }
                          
                          func("홍길동")
         --------------------------------------------------- 
                              X            X
                          function func()
                          {
                          }
                          
                          func()
         ---------------------------------------------------
          권장 : => (화살표 함수 이용)
                --- function / return 제거 : 람다식(함수포인터)
           function func()
           let func=function(){}
           let func=()=>{} ***
      
      5. 배열 / 객체
          배열 : []
                 값추가 => push()
                 자르기 => slice()
          객체 : {}(JSON) => 서버에서 값 전송
                 자바 : VO
                 객체
                  let a={aa: ,bb: }
                    => a.aa a['aa']
                    => a.bb a['bb']
                  => 자바 : List
       6. 객체 문서 모델
           => HTML 태그를 선택해서 제어
              --------
              이벤트 처리
              속성 값 변경
              CSS 적용
              
              *** HTML파일 => 전체를 가지고 있는 객체 : document
              태그 1개
                = document.getElementById(아이디명)
                = document.querySelector(CSS의 Selector)
                                         --------------
                                          화면 디자인
                                          태그 선택
                                          크롤링
              태그 여러개
               = document.getElementClassName(클래스명)
               = document.getElementByTagName(태그명)
               = document.querySelectorAll(클래스명, 태그명)
               
              => Jquery
                  $('태그,아이디명,클래스명')
              -------------------------------
              => Vue => <a ref="id"> : 양방향
                  $refs.id
              => React => target.id  : 단방향
                 Next
              -------------------------------
       7. 이벤트 등록
       8. 내장객체
          내장객체
          브라우저 내장 객체
       -------------------------------- 라이브러리화 : JQuery
         
                      
         
 --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
// 시작점 => 자동 호출
/*
    Jquery : $(function(){})
    Vue    : mounted(){}
    React  : componentDidMount(){}
              => 고전
              useEffect() => hooks => 16 => 17~19.ver
                                               ------ 18.ver 호환 X
 */
window.onload=function(){
	/* // id
	let h1=document.getElementById("h1")
	console.log("h1"+typeof h1)
	// 태그는 자바 => class
	// 속성은 자바 => 멤버변수
	h1.style.backgroundColor='yellow'
	// <h1 style="background-color:yellow"> */
	// let h1=document.querySelector("h1")
	// h1.style.backgroundColor='pink'
	let h1=document.querySelectorAll("h1") // 배열
	h1[0].style.backgroundColor='yellow'
	h1[1].style.backgroundColor='pink'
	
	// h2 => 글자 색상을 red 출력
	let h2=document.querySelector("h2")
	h2.style.color='red'
}
</script>
</head>
<body>
  <h1>Hello JavaScript-1</h1>
  <h2>Hello JavaScript-2</h2>
  <h1 id="h1">Hello JavaScript-3</h1>
</body>
</html>