package com.sist.model;

import java.text.SimpleDateFormat;
import java.util.*;
import com.sist.vo.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.sist.controller.Controller;
import com.sist.controller.RequestMapping;
import com.sist.dao.*;

@Controller
public class QnABoardModel {
	@RequestMapping("qna/qna_list.do")
	public String qna_list(HttpServletRequest request, HttpServletResponse response)
	{
		String page=request.getParameter("page");
		if(page==null)
			page="1";
		   
		int curpage=Integer.parseInt(page);
		Map map=new HashMap();
		map.put("start", (10*curpage)-9);
		map.put("end",10*curpage);
		List<QnABoardVO> list=QnABoardDAO.qnaListData(map);
		int count=QnABoardDAO.qnaRowCount();
		int totalpage=(int)(Math.ceil(count/10.0));
		count=count-((10*curpage)-10);
		   
		request.setAttribute("list", list);
		request.setAttribute("totalpage", totalpage);
		request.setAttribute("curpage", curpage);
		request.setAttribute("count", count);
		request.setAttribute("today", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		   
		request.setAttribute("main_jsp", "../qna/qna_list.jsp");
		return "../main/main.jsp";
	}
	@RequestMapping("qna/qna_insert.do")
	public String qna_insert(HttpServletRequest request, HttpServletResponse response)
	{
		request.setAttribute("main_jsp", "../qna/qna_insert.jsp");
		return "../main/main.jsp";
	}
	@RequestMapping("qna/qna_insert_ok.do")
	public String qna_insert_ok(HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session=request.getSession();
		String subject=request.getParameter("subject");
		String content=request.getParameter("content");
		String pwd=request.getParameter("pwd");
		String id=(String)session.getAttribute("id");
		String name=(String)session.getAttribute("name");
		
		// 데이터 유지 => 서버에 저장
		QnABoardVO vo=new QnABoardVO();
		vo.setId(id);
		vo.setName(name);
		vo.setSubject(subject);
		vo.setContent(content);
		vo.setPwd(pwd);
		
		QnABoardDAO.qnaInsert(vo);
		return "redirect:/qna/qna_list.do";
	}
	/*
	 * @RequestMapping("qna/qna_insert_ok.do")
public void qna_insert_ok(HttpServletRequest request, HttpServletResponse response) {
    try {
        String subject = request.getParameter("subject");
        String content = request.getParameter("content");
        String pwd = request.getParameter("pwd");

        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("id");
        String name = (String) session.getAttribute("name");

        System.out.println("===== QnA INSERT OK 실행 =====");
        System.out.println("Session ID: " + id);
        System.out.println("Session Name: " + name);

        if (id == null || name == null) {
            System.out.println("세션 정보 없음, 로그인 필요");
            response.sendRedirect("../member/login.do");
            return;
        }

        QnABoardVO vo = new QnABoardVO();
        vo.setId(id);
        vo.setName(name);
        vo.setSubject(subject);
        vo.setContent(content);
        vo.setPwd(pwd);

        boolean isInserted = QnABoardDAO.qnaInsert(vo);
        if (!isInserted) {
            System.out.println("QnA 게시글 등록 실패");
            response.sendRedirect("../qna/qna_insert.do");
            return;
        }

        System.out.println("===== QnA 게시글 등록 성공! 리다이렉트 실행 =====");
        response.sendRedirect("../qna/qna_list.do");

    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("리다이렉트 중 오류 발생: " + e.getMessage());
    }
}

	 */
	@RequestMapping("qna/qna_admin_list.do")
	public String qna_admin_list(HttpServletRequest request, HttpServletResponse response)
	{
		String page=request.getParameter("page");
		if(page==null)
			page="1";
		   
		int curpage=Integer.parseInt(page);
		Map map=new HashMap();
		map.put("start", (10*curpage)-9);
		map.put("end",10*curpage);
		List<QnABoardVO> list=QnABoardDAO.qnaAdminListData(map);
		int count=QnABoardDAO.qnaAdminRowCount();
		int totalpage=(int)(Math.ceil(count/10.0));
		count=count-((10*curpage)-10);
		request.setAttribute("list", list);
		request.setAttribute("totalpage", totalpage);
		request.setAttribute("curpage", curpage);
		request.setAttribute("count", count);
		request.setAttribute("today", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		
		request.setAttribute("admin_jsp", "../qna/qna_admin_list.jsp");
		request.setAttribute("main_jsp", "../adminpage/admin_main.jsp");
		return "../main/main.jsp";
	}
	@RequestMapping("qna/qna_admin_insert.do")
	public String qna_admin_insert(HttpServletRequest request, HttpServletResponse response)
	{
		String gi=request.getParameter("gi");
		// VO 읽기
		QnABoardVO vo=QnABoardDAO.qnaAdminDetailData(Integer.parseInt(gi));
		request.setAttribute("vo", vo);
		
		request.setAttribute("admin_jsp", "../qna/qna_admin_insert.jsp");
		request.setAttribute("main_jsp", "../adminpage/admin_main.jsp");
		return "../main/main.jsp";
	}
	@RequestMapping("qna/qna_admin_insert_ok.do")
	public String qna_admin_insert_ok(HttpServletRequest request, HttpServletResponse response)
	{
		String subject=request.getParameter("subject");
		String content=request.getParameter("content");
		String pwd=request.getParameter("pwd");
		String group_id=request.getParameter("group_id");
		HttpSession session=request.getSession();
		String id=(String)session.getAttribute("id");
		// 데이터 유지 => 서버에 저장
		QnABoardVO vo=new QnABoardVO();
		vo.setId(id);
		vo.setGroup_id(Integer.parseInt(group_id));
		vo.setSubject(subject);
		vo.setContent(content);
		vo.setPwd(pwd);
		
		QnABoardDAO.qnaAdminInsert(vo);
		return "redirect:../qna/qna_admin_list.do";
	}
	@RequestMapping("qna/qna_detail.do")
	public String qna_detail(HttpServletRequest request, HttpServletResponse response)
	{
		String no=request.getParameter("no");
		QnABoardVO vo=QnABoardDAO.qnaDetailData(Integer.parseInt(no));
		request.setAttribute("vo", vo);
		request.setAttribute("main_jsp", "../qna/qna_detail.jsp");
		return "../main/main.jsp";
	}
	@RequestMapping("qna/qna_delete.do")
	public String qna_delete(HttpServletRequest request, HttpServletResponse response)
	{
		String gi=request.getParameter("group_id");
		QnABoardDAO.qnaDelete(Integer.parseInt(gi));
		return "redirect:../qna/qna_list.do";
	}
	@RequestMapping("qna/qna_admin_delete.do")
	public String qna_admin_delete(HttpServletRequest request, HttpServletResponse response)
	{
		String gi=request.getParameter("gi");
		QnABoardDAO.qnaAdminDelete(Integer.parseInt(gi));
		return "redirect:../qna/qna_admin_list.do";
	}
}
