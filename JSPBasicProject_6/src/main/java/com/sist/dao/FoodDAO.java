package com.sist.dao;
import java.util.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
/*
    JSP
    ---
     => 화면 출력
         <%@ page contentType="" import=""%>
                                 ----------
     => 자바 / HTML 구분
         <% %>
         <%= %>
     => 내장 객체
         => request/response/application/session
         => cookie 사용법
     => jsp 액션 태그
         <jsp:include>
     => JDBC / DBCP
     => JSTL / EL
         <c:forEach>, <c:forTokens>, <c:if>
         <c:set>
         <fmt:formatDate> <fmt:formatNumber>
         TO_CHAR(regdate,'')
     -------------------------------------------
      MVC
 */
public class FoodDAO {
	private Connection conn;
	private PreparedStatement ps;
	
	public void getConnection()
	{
		try
		{
			Context init=new InitialContext();
			Context c=(Context)init.lookup("java://comp/env");
			DataSource ds=(DataSource)c.lookup("jdbc/oracle");
			conn=ds.getConnection();
		}catch(Exception ex) {}
	}
	// POOL 안으로 반환 => 재사용
	public void disConnection()
	{
		try
		{
			if(ps!=null) ps.close();
			if(conn!=null) conn.close();
		}catch(Exception ex) {}
	}
	// 기능
	// => 목록 출력
	// => 상세보기
	public List<FoodVO> foodListData(int page)
	{
		List<FoodVO> list=new ArrayList<FoodVO>();
		try
		{	
			// 1. 연결
			getConnection();
			// 2. SQL 문장 연결
			String sql="SELECT fno, name, poster, num "
					+ "FROM (SELECT fno, name, poster, rownum as num "
					+ "FROM (SELECT /*+ INDEX_ASC(food_menupan fm_fno_pk)*/ fno, name, poster "
					+ "FROM food_menupan)) "
					+ "WHERE num BETWEEN ? AND ?";
			// 3. 오라클로 전송
			ps=conn.prepareStatement(sql);
			// 4. ? 값 채우기
			int rowSize=12;
			int start=(rowSize*page)-(rowSize-1);
			int end=rowSize*page;
			ps.setInt(1, start);
			ps.setInt(2, end);
			/*
			   rownum은 1번부터 시작
			   자바 => 0 오라클 => 1
			 */
			// 5. 실행 결과 읽기
			ResultSet rs=ps.executeQuery();
			// => list 저장
			while(rs.next())
			{
				FoodVO vo=new FoodVO();
				vo.setFno(rs.getInt(1));
				vo.setName(rs.getString(2));
				vo.setPoster("https://www.menupan.com"+rs.getString(3));
				list.add(vo);
			}
			rs.close();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}finally
		{
			disConnection();
		}
		return list;
	}
	// 총 페이지
	public int foodTotalPage()
	{
		int total=0;
		try
		{
			getConnection();
			String sql="SELECT CEIL(COUNT(*)/12.0)"
					+ "FROM food_menupan";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next();
			total=rs.getInt(1);
			rs.close();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}finally
		{
			disConnection();
		}
		return total;
	}
	// 상세보기
	public FoodVO foodDetailData(int fno, int mode)
	{
		FoodVO vo=new FoodVO();
		try
		{
			getConnection();
			if(mode==1)
			{
				String sql="UPDATE food_menupan SET "
						+ "hit=hit+1 "
						+ "WHERE fno="+fno;
				ps=conn.prepareStatement(sql);
				ps.executeUpdate();
			}
			// MainPage => 공동 작업
			String sql="SELECT fno, name, type, phone, address, theme, poster, "
					+ "images, time, parking, content, price, score, hit "
					+ "FROM food_menupan "
					+ "WHERE fno="+fno;
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next();
			vo.setFno(rs.getInt("fno"));
			vo.setName(rs.getString("name"));
			vo.setType(rs.getString("type"));
			vo.setPhone(rs.getString("phone"));
			vo.setAddress(rs.getString("address"));
			vo.setTheme(rs.getString("Theme"));
			vo.setPoster("https://www.menupan.com"+rs.getString("poster"));
			vo.setImages(rs.getString("images"));
			vo.setTime(rs.getString("time"));
			vo.setParking(rs.getString("parking"));
			vo.setContent(rs.getString("content"));
			vo.setPrice(rs.getString("price"));
			vo.setScore(rs.getDouble("score"));
			vo.setHit(rs.getInt("hit"));
			rs.close();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}finally
		{
			disConnection();
		}
		return vo;
	}
}
