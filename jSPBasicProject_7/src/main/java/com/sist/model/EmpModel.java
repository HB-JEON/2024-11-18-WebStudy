package com.sist.model;

import com.sist.dao.EmpDAO;
import com.sist.vo.EmpVO;

import java.util.*;
import jakarta.servlet.http.HttpServletRequest;

// => JSP 출력 / 자바에서 처리
// request / response
// ------- 요청 값 보유 => 데이터를 추가 => JSP 전송
public class EmpModel {
	// Call by Reference : 배열 / 클래스
	public void empListData(HttpServletRequest request)
	{
		// 1. DataBase 연동
		EmpDAO dao=new EmpDAO();
		List<EmpVO> list=dao.empListData();
		request.setAttribute("list", list);
		// JSP에서 받은 request 값을 채우기
		// request.setAttribute("curpage", curpage)
	}
}
