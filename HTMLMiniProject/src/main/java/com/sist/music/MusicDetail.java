package com.sist.music;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.*;
import com.sist.dao.*;
import com.sist.vo.*;

@WebServlet("/MusicDetail")
public class MusicDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		String mno=request.getParameter("mno");
		MusicDAO dao=MusicDAO.newInstance();
		MusicVO vo=dao.musicDetailData(Integer.parseInt(mno));
		
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">");
		out.println("</head>");
		out.println("<body>");
		out.println("<div class=container>");
		out.println("<div class=row>");
		
		out.println("<table class=table>");
		out.println("<tr>");
		out.println("<td width=30% class=text-center rowspan=7>");
		out.println("<img src="+vo.getPoster()+" style=\"width:270px;height:300px\">");
		out.println("</td>");
		out.println("<td>");
		out.println("<td colspan=2>");
		out.println("<h3>"+vo.getTitle()+"</h3>");
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td width=10% style=\"color:gray\">순위</td>");
		out.println("<td width=60%>"+vo.getMno()+"</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td width=10% style=\"color:gray\">가수명</td>");
		out.println("<td width=60%>"+vo.getSinger()+"</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td width=10% style=\"color:gray\">앨범명</td>");
		out.println("<td width=60%>"+vo.getAlbum()+"</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td width=10% style=\"color:gray\">순위 변동</td>");
		out.println("<td width=60%>");
		if (vo.getIdcrement()!=0)
		{
			if(vo.getState().equals("상승"))
				out.println("▲ "+vo.getIdcrement());
			else if(vo.getState().equals("하강"))
				out.println("▼ "+vo.getIdcrement());
		}
		else
		{
			out.println("-");
		}
		out.println("</td>");
		out.println("</tr>");
		
		HttpSession session=request.getSession();
		String id=(String)session.getAttribute("id");
		out.println("<tr>");
		out.println("<td class=text-center>");
		out.println("<td class=text-right>");

		if(id!=null)
		{
		out.println("<a href=# class=\"btn btn-xs btn-danger\">좋아요</a>");
		out.println("<a href=# class=\"btn btn-xs btn-success\">찜하기</a>");
		out.println("<a href=# class=\"btn btn-xs btn-info\">예약하기</a>");
		}
		out.println("<a href=MainServlet?mode=7 class=\"btn btn-xs btn-primary\">목록</a>");
		
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</table>");
		
		out.println("</div>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}

}
