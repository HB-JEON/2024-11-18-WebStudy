package com.sist.food;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.sist.dao.FoodDAO;
import com.sist.vo.FoodVO;
/*
1. 사용자의 전송 방식
    GET / POST
     | <a>, sendRedirect() => URL? 데이터 전송
     | <form>, ajax, axios.post() 
     
    검색어 입력 / 찾기 컬럼 => 두개를 동시에 전송
    ---------------------------------------<form> => POST
    
    Get => doGet() ==> @GetMapping()
    Post => doPost() ==>PostMapping()
    Get/Post => service() @RequestMapping()
                           => Spring 6.0 : 권장
    JSP => doGet/doPost 존재 X
           _jspService()
*/

@WebServlet("/FoodTypeFind")
public class FoodTypeFind extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 브라우저로 전송 => HTML 전송
		// HTML, XML, JSON => response(HTML, Cookie 전송 가능)
		response.setContentType("text/html;charset=UTF-8");
		// 2. 브라우저 연결
		PrintWriter out=response.getWriter();
		// 사용자가 보내준 값 받기
		String strPage=request.getParameter("page");
		if(strPage==null)
			strPage="1";
		int curpage=Integer.parseInt(strPage);
		
		String type=request.getParameter("type");
		if(type==null)
			type="한식";
		FoodDAO dao=FoodDAO.newInstance();
		List<FoodVO> list=dao.foodTypeFind(curpage, type);
		int totalpage=dao.foodTypeTotalPage(type);
		
		final int BLOCK=10;
		int startPage=((curpage-1)/BLOCK*BLOCK)+1;
		int endPage=((curpage-1)/BLOCK*BLOCK)+BLOCK;
		
		if(endPage>totalpage)
			endPage=totalpage;
		
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">");
		out.println("<link rel=stylesheet href=css/food.css>");
		out.println("</head>");
		out.println("<body>");
		out.println("<div class=container>");
		out.println("<div class=\"row text-center\">");
		out.println("<a href=FoodTypeFind?type=한식 class=\"btn btn-sm btn-danger\">한식</a>");
		out.println("<a href=FoodTypeFind?type=양식 class=\"btn btn-sm btn-success\">양식</a>");
		out.println("<a href=FoodTypeFind?type=중식 class=\"btn btn-sm btn-info\">중식</a>");
		out.println("<a href=FoodTypeFind?type=일식 class=\"btn btn-sm btn-primary\">일식</a>");
		out.println("<a href=FoodTypeFind?type=카페 class=\"btn btn-sm btn-warning\">카페</a>");
		out.println("<a href=FoodTypeFind?type=기타 class=\"btn btn-sm btn-default\">기타</a>");
		out.println("</div>");
		out.println("<div class=row style\"margin-top:20px\">");
		/*
		   response 기능을 한개만 수행 가능
		   ----------------------------
		    1. 쿠키 전송 => Detail 이동 => HTML 전송
		    2. HTML 전송
		 */
		for(FoodVO vo:list)
		{
			out.println("<div class=\"col-md-3\">");
			out.println("<div class=\"thumbnail\">");
			out.println(" <a href=\"FoodBeforeDetail?fno="+vo.getFno()+"\">");
			out.println("<img src="+vo.getPoster()+" style=\"width:230px;height:150px\">");
			out.println("<div class=\"caption\">");
			out.println("<p>"+vo.getName()+"</p>");
			out.println("</div>");
			out.println("</a>");
			out.println("</div>");
			out.println("</div>");
		}
		out.println("</div>");
		out.println("<div class=\"row text-center\">");
		out.println("<ul class=\"pagination\">");
		if(startPage>1)
		{
			out.println("<li><a href=\"FoodTypeFind?type="+type+"&page="+(startPage-1)+"\">&lt;</a></li>");
		}
		for(int i=startPage;i<=endPage;i++)
		{
			if(i==curpage)
				out.println("<li class=active><a href=\"FoodTypeFind?type="+type+"&page="+i+"\">"+i+"</a></li>");
			else
				out.println("<li><a href=\"FoodTypeFind?type="+type+"&page="+i+"\">"+i+"</a></li>");
		}
		
		if(endPage<totalpage)
		{
			out.println("<li><a href=\"FoodTypeFind?type="+type+"&page="+(endPage+1)+"\">&gt;</a></li>");
		}
		out.println("</ul>");
		
		
		out.println("</div>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}

}