package com.sist.model;

import com.sist.controller.Controller;
import com.sist.controller.RequestMapping;
import com.sist.dao.JjimDAO;
import com.sist.vo.JjimVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class JjimModel {
	private String[] table={"", "cocktail_product"};
	private String[] noName={"", "product_no"};
	private String[] urls={"", "../cocktail_product/cocktail_product_list.do?product_no=", 
			"../cocktail_product../cocktail_product_detail.do?product_no=", 
			""};
	@RequestMapping("jjim/jjim_insert.do")
	public String jjin_insert(HttpServletRequest request, HttpServletResponse response)
	{
		String rno=request.getParameter("rno");
		String type=request.getParameter("type");
		HttpSession session=request.getSession();
		String id=(String)session.getAttribute("id");
		
		JjimVO vo=new JjimVO();
		vo.setId(id);
		vo.setRno(Integer.parseInt(rno));
		vo.setType(Integer.parseInt(type));
		
		JjimDAO.jjimInsert(vo);
		
		return "redirect:../"+urls[Integer.parseInt(type)]+rno;
	}
	@RequestMapping("jjim/jjim_delete.do")
	public String jjim_delete(HttpServletRequest request, HttpServletResponse response)
	{
		String rno=request.getParameter("rno");
		String jno=request.getParameter("jno");
		String type = request.getParameter("type");
		
		JjimDAO.jjimDelete(Integer.parseInt(jno));
		
		return "redirect:../"+urls[Integer.parseInt(type)]+rno;
	}
}
