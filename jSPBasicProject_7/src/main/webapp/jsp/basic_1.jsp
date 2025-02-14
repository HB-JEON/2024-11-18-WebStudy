<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--
      MV 구조
      ======> MVC
      M : Model => 자바
      V : View => JSP
      -----------------> JSTL / EL
                         ----   --
                          | 자바의 제어문 / 메소드 호출 => 태그
                            태그형 프로그램(실무)
       => 최대한 JSP에서 자바를 사용 X
          ----------------
           확장성 X => 재사용 불가능 => Model1 방식
           보안 취약 => JSP 컴파일 방식 X
           ---         => 소스 자체가 노출
            | 자바파일을 생성
              ------------ .class
                확장성 => extends(상속)
                보안성 강화
       => request.setAttribute() : 오라클 데이터를 추가
       => 주력 => Java / HTML
                 -----  -----
 --%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>