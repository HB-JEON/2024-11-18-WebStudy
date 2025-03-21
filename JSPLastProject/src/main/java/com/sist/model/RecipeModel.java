package com.sist.model;
import java.lang.annotation.Repeatable;
import java.net.http.HttpRequest;
import java.util.*;

import com.sist.controller.Controller;
import com.sist.controller.RequestMapping;
import com.sist.dao.*;
import com.sist.vo.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/*
 *   MVC 
 *    => jsp : 링크 (요청)
 *    => 요청 내용 받기 
 *       ----------- request.getParameter()
 *    => 요청 처리 => DAO연동 
 *    => JSP로 결과값 전송 
 *    
 *    <%@ page .... %>
 *    <%
 *        자바 => 출력할 데이터 
 *    %>
 *    
 *    Spring : MVC
 *    
 *    => 오라클 
 *    => 브라우저 전송 
 *    => 자바 핵심 
 */
import java.util.*;
import com.sist.dao.*;
import com.sist.vo.*;
@Controller
public class RecipeModel {
	// if(uri.equals("recipe/recipe_list.do"))
    @RequestMapping("recipe/recipe_list.do") //if추가
    public String recipe_list(HttpServletRequest request, HttpServletResponse response)
    {
    	// 처리 => 주문서 , 처리후=> 어떤 테이블
    	// request       addAttribute() => return 
    	String page=request.getParameter("page");
    	if(page==null)
    		page="1";
    	int curpage=Integer.parseInt(page);
    	Map map=new HashMap();
    	map.put("start", (12*curpage)-11);
    	map.put("end", 12*curpage);
    	List<RecipeVO> list=RecipeDAO.recipeListData(map);
    	int totalpage=RecipeDAO.recipeTotalPage();
    	final int BLOCK=10;
  	    int startPage=((curpage-1)/BLOCK*BLOCK)+1;
  	    int endPage=((curpage-1)/BLOCK*BLOCK)+BLOCK;
  	  
  	    if(endPage>totalpage)
  		  endPage=totalpage;
  	    
  	    
  	    request.setAttribute("list", list);
	    request.setAttribute("curpage", curpage);
	    request.setAttribute("totalpage", totalpage);
	    request.setAttribute("startPage", startPage);
	    request.setAttribute("endPage", endPage);
  	    request.setAttribute("main_jsp", "../recipe/recipe_list.jsp");
  	    return "../main/main.jsp";
    	
    }
    @RequestMapping("recipe/chef_list.do")
    public String chef_list(HttpServletRequest request, HttpServletResponse response)
    {
    	String page=request.getParameter("page");
    	if(page==null)
    		page="1";
    	int curpage=Integer.parseInt(page);
    	Map map=new HashMap();
    	map.put("start", (12*curpage)-11);
    	map.put("end", 12*curpage);
    	List<ChefVO> list=RecipeDAO.recipeChefListData(map);
    	int totalpage=RecipeDAO.recipeChefTotalPage();
    	
    	request.setAttribute("list", list);
    	request.setAttribute("curpage", curpage);
    	request.setAttribute("totalpage", totalpage);
    	
    	request.setAttribute("main_jsp", "../recipe/chef_list.jsp");
    	return "../main/main.jsp";
    }
    @RequestMapping("recipe/recipe_find.do")
    public String recipe_find(HttpServletRequest request, HttpServletResponse response)
    {
    	String[] findArr=request.getParameterValues("fs");
    	if(findArr==null)
    		findArr=new String[] {"T"};
    	String ss=request.getParameter("ss");
    	if(ss==null)
    		ss="만개";
		Map map=new HashMap();
		map.put("findArr", findArr);
		map.put("ss", ss);
		List<RecipeVO> list=RecipeDAO.recipeFindData(map);
		request.setAttribute("list", list);
    	request.setAttribute("main_jsp", "../recipe/recipe_find.jsp");
    	return "../main/main.jsp";
    }
    /*
        1. return => "../main/main.jsp" => 화면 출력
        2. return => "../food/food/jsp" => ajax
        3. return => "redirect:../main/main.do" => insert or update
     */
    @RequestMapping("recipe/recipe_detail.do")
    public String recipe_detail(HttpServletRequest request, HttpServletResponse response)
    {
    	String no=request.getParameter("no");
    	RecipeDetailVO vo=RecipeDAO.recipeDetailData(Integer.parseInt(no));
    	
    	List<String> mList=new ArrayList<String>();
    	List<String> iList=new ArrayList<String>();
    	
    	String[] datas=vo.getFoodmake().split("\n");
    	for(String make:datas)
    	{
    		StringTokenizer st=new StringTokenizer(make,"^");
    	    mList.add(st.nextToken());
    	    iList.add(st.nextToken());
    	}
    	/*
    	 *   <c:forEach var="make" item="${mList}">
    	 *    
    	 */
    	
    	ReplyVO rvo=new ReplyVO();
  	    rvo.setRno(Integer.parseInt(no));
  	    rvo.setType(2);
  	    List<ReplyVO> list=ReplyDAO.replyListData(rvo);
  	    int count=ReplyDAO.replyCount(rvo);
  	    request.setAttribute("count", count);
  	    request.setAttribute("rList", list);
  	    
    	request.setAttribute("vo", vo);
    	request.setAttribute("mList", mList);
    	request.setAttribute("iList", iList);
    	request.setAttribute("main_jsp", "../recipe/recipe_detail.jsp");
    	return "../main/main.jsp";
    }
    @RequestMapping("recipe/chef_make.do")
    public String chef_make(HttpServletRequest request, HttpServletResponse response)
    {
    	String no=request.getParameter("no");
    	String page=request.getParameter("page");
    	if(page==null)
    		page="1";
    	int curpage=Integer.parseInt(page);
    	Map map=new HashMap();
    	map.put("start", (12*curpage)-11);
    	map.put("end", 12*curpage);
    	map.put("no", no);// no=1 no='1'
    	List<RecipeVO> list=RecipeDAO.recipeChefMakeData(map);
    	int totalpage=RecipeDAO.recipeChefMakeTotalPage(Integer.parseInt(no));
    	final int BLOCK=10;
  	    int startPage=((curpage-1)/BLOCK*BLOCK)+1;
  	    int endPage=((curpage-1)/BLOCK*BLOCK)+BLOCK;
  	  
  	    if(endPage>totalpage)
  		  endPage=totalpage;
  	    
  	    
  	    request.setAttribute("list", list);
	    request.setAttribute("curpage", curpage);
	    request.setAttribute("totalpage", totalpage);
	    request.setAttribute("startPage", startPage);
	    request.setAttribute("endPage", endPage);
	    request.setAttribute("no", no);
	    request.setAttribute("chef", list.get(0).getChef());
    	request.setAttribute("main_jsp", "../recipe/chef_make.jsp");
    	return "../main/main.jsp";
    }
}
