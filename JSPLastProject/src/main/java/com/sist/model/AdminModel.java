package com.sist.model;

import com.sist.controller.Controller;
import com.sist.controller.RequestMapping;
import com.sist.vo.*;
import com.sist.dao.*;
import java.util.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AdminModel {
	@RequestMapping("adminpage/admin_main.do")
	public String adminpage_main(HttpServletRequest request, HttpServletResponse response)
	{
		request.setAttribute("admin_jsp", "../adminpage/admin_home.jsp");
		request.setAttribute("main_jsp", "../adminpage/admin_main.jsp");
		return "../main/main.jsp";
	}
	@RequestMapping("adminpage/admin_reserve.do")
	public String admin_reserve(HttpServletRequest request, HttpServletResponse response)
	{
		List<ReserveVO> list=ReserveDAO.reserveAdminPageData();
		request.setAttribute("list", list);
		request.setAttribute("count", list.size());
		request.setAttribute("admin_jsp", "../adminpage/admin_home.jsp");
		request.setAttribute("main_jsp", "../adminpage/admin_main.jsp");
		return "../main/main.jsp";
	}
	
	@RequestMapping("adminpage/admin_reserve_ok.do")
	public String admin_reserve_ok(HttpServletRequest request, HttpServletResponse response)
	{
		String ren=request.getParameter("rno");
		ReserveDAO.reserveAdminOk(Integer.parseInt(ren));
		return "redirect:../admin/admin_reserve.do";
	}
}
