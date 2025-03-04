<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--
      Jquery / Vue(VueX) / React(React-Query,Redux)
        => 문서 객체(태그 제어) => 태그를 선택 후 처리
        => 태그에 대한 이벤트 처리
        => DOMScript
        => 단점 : 데이터 존재 X (데이터 => 서버)
                                        --- 오라클 연결
        => 출력 (MVC => View)
        => 출력 속도 빠름
       1. CSS 선택자
           $('선택자'), Vue => <button ref="">
                       React => e.target
              선택자
              -----
               1) 태그 => $('태그명').함수()
                                    ----- 값 설정
                                           val("")
                                           text("") / html("") / attr("") / append("")
                                          값 읽기
                                           val() / text() / html() / attr()
                                          이벤트
                                          ----- 모든 태그에 이벤트 가능
                                           $(선택자).click(function(){})
                                           $(선택자).on('click',function(){})
                                           => mouseup / mousedown
                                           => keyup / keydown
                                           => hover
                                           => change
               2) ID(속성값) => $('#아이디명')
               3) CLASS(속성값) => $('.클래스명')
               4) 자손 : $('태그명(id,class) > 태그명(id,class)')
                  후손 : $('태그명(id,class) 태그명(id,class)')
                  => 목록 출력
                      <table>, <ul>, <dl>, => id 혹은 class 부여 X
                  <div>
                    <h1></h1> => 자손
                    <div>
                      <h1></h1> => 후손
                    </div>
                  </div>
               5) 속성선택자 =>
                            $('태그명[속성=값]') => a, img, input
                            $('태그명[속성$=값]') => endsWith
                            $('태그명[속성^=값]') => startWith
                            $('태그명[속성*=값]') => contains
                            xpath = div[0]/div/...(셀레니움)
                            ------------------------------- 파이썬
               6) 가상선택자 =>
                            nth-child => 1 / eq => 0
                            $('태그명:nth-child(odd)'
                            $('태그명:nth-child(even)'
                            $('태그명:nth-child(2n+1)'
                            $('태그명:eq(0)')
                    $('') => 내장 객체, 태그 선택자
                    
       2. 자바스크립트 기본
            JSP / Spring / Spring-Boot : Java
            DJango : Python
            Asp.net : C#
            NextJS / ReactJS / VueJS / Jquery : JavaScript
            
            변수
             let : 지역변수 => 일반변수
             const : 상수 => function / 배열처리, 객체처리
            데이터형
             number : 숫자(정수,실수)
             string : 문자, 문자열
             boolean : true / false
                       숫자 : 0, 0.0 외 전부 true
                       문자 : null 아닌 경우 true
             object
                 => 배열[] => List 호환
                 => 객체{} =>  VO와 호환
             undefined : 데이터 값이 없는 경우
             function : 데이터형으로 취급
                         => 매개변수로 사용 가능(callback)
                         => 이벤트 / react-query / redux
                          map(function(){})
                          forEach(function(){}
                          
                          function a(){}
                          map()
            연산자
              산술연산자 => / (정수/정수=실수)
              비교연산자 => 
                            ==
                            10=='10'==> true
                            ====
                            10==='10' ==> false
              형변환 => 
                       Number, parseInt => 문자열을 정수형 변경
                       => 데이터 읽기 : 문자열로 읽어온다(HTML)
                       문자열 변환 = String(10) => "10"
                       논리형 변경 => Boolean(1) => true
                                    Boolean(0) => false
                                    Boolean('aaa') => true
                                    Boolean(null) => false
            제어문 
              if / if-else
              for / for-each
              => 출력
              일반 for
              for(let i=0;i<=10;i++)
              {
                   처리문장
              }
              for(let i in 배열) => i는 인덱스 번호
                [1,2,3,4,5] => i=0,1,2,3,4
              for(let key in 객체) => key = key 값
                {name:"",sex:""} key=name,sex
                
              for(let vo of 배열)
                      -- 배열의 실제값
              배열.map(function(배열값){})
              배열.forEach(function(배열값){})
            함수 : 필수(요청 처리)
                   function func_name(매개변수){}
                      => return 형 사용 X, 매개변수는 변수명만 사용
                      => 리턴형이 없는 경우
                      function func()
                      {
                          ...
                          ...
                      }
                      => 리턴형이 있는 경우
                      function func()
                      {
                          ...
                          return 값
                      }
                      *** 익명의 함수
                      ---------------------------
                      let 함수명=function(){}
                      let 함수명=()=>{}
                      --------------------------- 함수 안에 함수 추가
                                                   Vue/React => 이벤트
                      1. 자바스크립트의 main
                         window.onload=function(){}
                         => window.onload=()=>{}
                      2. jquery
                         $(function(){})
                         $(document).ready(function(){})
                      3. vue
                         mounted:function(){}
                      4. react
                         componentDidMount()
                         => useEffect()
            내장 함수 : String 
                        substring() / substr() => 문자열 자르기
                        indexOf / lastIndexOf => 문자 위치 찾기
                        length() : 문자 개수
                        split() : 문자 분리
                        trim() : 좌우의 공백 제거
                        replace() : 문자 변환
                      Array 
                        push() : 배열 데이터 추가
                        pop() : 배열 데이터 제거
                        slice() : 데이터를 잘라서 새로운 배열
                        ... : scope 연산자 => 배열 전체 복사
                      Number 
                        => 형변환
                        => NaN : 연산 불가능
                      Date 
                        getFullYear()
                        getMonth() : 월, 0번부터 시작
                        getDate() : 일
                        getDay() : 요일
                      Math
                        ceil() : 올림
                        round() : 반올림
            브라우저 : window : open, close, scrollbar 조절
                     document : 문서 관리(태그)
                                 write / querySelector
                     location : 이동 => href
                     history : 기록 정보 => go,forward,back
             
       3. 서버연결 => 데이터 읽기 => AJax => 직접 제작
          ----------------------------    ------- AJax
           => 1) 로그인 처리 / 회원가입(아이디 찾기, 비밀번호 찾기) / 아이디 중복 체크
              2) 검색
              3) 추천
              4) 예약
           => 입력된 데이터 유지 => AJax
               jsp => 서버 => jsp 초기화 (새로운 jsp 생성)
               login.jsp => 서버 => login.jsp
               ---------            --------
                  |                    |
                  ----------------------
                      메모리 주소가 다르다.
              5) 채팅
             
              Jquery => Ajax, axios
                        $.ajax({
                           url:''
                           data:{}
                           success:{데이터 받기}
                        })
                        axios.get("URL")
                        <a>, <form>, location.href
                        => client => server : URL
                        => 클라이언트에서만 작업
                => 값 읽기 / 값 쓰기
                    val() / text() 
                => 이벤트 => 언제 데이터 읽기 / 언제 데이터 쓰기
                
                1. <input> <select> <textarea>
                2. checkbox
                3. 목록 제어
                4. 스크롤 => 태그(X) => window
                4-1. 약간의 효과
                4-2. show / hide
                4-3. ui => 사용법
                     => 완성 : codepen.io : 템플릿의 구조가 깨진다.
                5. 서버 연결
 --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<style type="text/css">
.container-fluid{
 margin-top: 50px;
}
.row{
  margin: 0px auto;
  width: 350px;
}
</style>
<script type="text/javascript" src="http://code.jquery.com/jquery.js"></script>
<script type="text/javascript">
$(function(){
	// 이벤트 리스너 이용
	// 고전 이벤트
	// $('#login').click(function(){})
	/*
	    change() : 선택이 변경 => <select>
	    mousedown / mouseup
	    hover
	    keyup / keydown
	    
	    => $().함수()
	           ---
	            1. 디자인 변경 : css()
	            2. 속성 변경 : attr()
	            3. 값을 읽기 : val() / text() / html()
	            4. 값 저장 : val("") / text("") / html("")
	    
	*/
	$('#login').on('click',function(){
		// 1. id 읽기
		let id=$('#id').val() // input / select / textarea
		if(id.trim()==="")
		{
			$('#print').html('<font color="red">아이디를 입력하세요.</font>')
			$('#id').focus()
			return
		}else
		{
			$('#print').text("")
		}
		let pwd=$('#pwd').val()
		if(pwd.trim()==="")
		{
			$('#print').html('<font color="red">비밀번호를 입력하세요.</font>')
			$('#pwd').focus()
			return
		}else
		{
			$('#print').text("")
		}
		alert("Ajax를 이용한 로그인 처리 수행")
	})
	
	$('#find').click(function(){
		let fd=$('#keyword').val()
		if(fd.trim()==="")
		{
			$('#keyword').focus()
			return
		}
		alert("검색어 : "+fd)
		$('#keyword').val("")
	})
	// 모든 function에서는 event 정보 받아오기 가능
	$('#keyword').keydown(function(e){
		if(e.keyCode===13)
		{
			let fd=$('#keyword').val()
			if(fd.trim()==="")
			{
				$('#keyword').focus()
				return
			}
			alert("검색어 : "+fd)
			$('#keyword').val("")
		}
	})
})
</script>
</head>
<body>
  <div class="container">
   <div class="row">
     <h3 class="text-center">Login(val()/text())</h3>
     <table class="table">
       <tr>
        <th class="text-center" width="20%">ID</th>
         <td width="80%">
           <input type="text" id="id" class="input-sm">
         </td>
       </tr>
       <tr>
        <th class="text-center" width="20%">PW</th>
         <td width="80%">
           <input type="password" id="pwd" class="input-sm">
         </td>
       </tr>
       <tr>
        <td colspan="2" class="text-center">
          <input type="button" class="btn-sm btn-success" id="login" value="로그인">
        </td>
       </tr>
       <tr>
        <td colspan="2">
          <span id="print"></span>
        </td>
       </tr>
     </table>
     <h3 class="text-center">Key 이벤트</h3>
     <input type="text" id="keyword" size="20">
     <input type="button" id="find" value="검색" class="btn-sm btn-danger">
   </div>
  </div>
</body>
</html>