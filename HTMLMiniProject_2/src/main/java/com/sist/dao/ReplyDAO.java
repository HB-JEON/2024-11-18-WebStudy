package com.sist.dao;

import java.util.*;
import java.sql.*;
import com.sist.vo.*;
/*
    Servlet / JSP
    -------
     보안성 높음 => 배포(.class)
      => 로직, 보안 => 연결용, 라이브러리 => HTML 출력 X
                            --------- 스프링
     HTML 구현 부분이 어렵다.
     HTML 수정할 때마다 => 컴파일 / 실행
     -------------------------------
      JSP
       => 자바 + HTML : 구분이 어려움
          ----------
           자바 ----| (Model)
                   | ---- 서블릿(Controller) => ***MVC***
          HTML ----| (View)
          *** Spring MVC 구조만 사용이 가능
          *** URL => 확장자 => .jsp(Model 1) => 재사용 X
              ------------ 사용자 .do / .never
      => SQL 문장 X (JPA)
      => HTML에 제어문이 가능 (JSP 필요X)
         Veu / React / ThymeLeaf
      => Front / Back => MSA
 */
public class ReplyDAO {
	// 연결 => 오라클 연결
		private Connection conn;
		// SQL => 송신, 결과값 수신
		private PreparedStatement ps;
		// 연결 => URL
		private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
		// 한사람 당 한개의 DAO 사용 => 싱글턴
		private static ReplyDAO dao;
		
		// 1. 드라이버 등록
		public ReplyDAO()
		{
			try
			{
				Class.forName("oracle.jdbc.driver.OracleDriver");
			}catch(Exception ex) {}
		}
		// 2. 싱글턴 => 반드시 static (static 메모리 공간은 한개)
		public static ReplyDAO newInstance()
		{
			if(dao==null)
				dao=new ReplyDAO();
			return dao;
		}
		// 3. 오라클 연결
		public void getConnection()
		{
			try
			{
				conn=DriverManager.getConnection(URL,"hr","happy");
			}catch(Exception ex) {}
		}
		// 4. 오라클 해제
		public void disConnection()
		{
			try
			{
				if(ps!=null) ps.close();
				if(conn!=null) conn.close();
			}catch(Exception ex) {}
		}
		// 기능
		// DML SELECT / UPDATE / DELETE / INSERT
		public void replyInsert(ReplyVO vo)
		{
			try
			{
				getConnection();
				String sql="INSERT INTO reply VALUES(reply_rno_seq.nextval,?,?,?,?, SYSDATE)";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, vo.getFno());
				ps.setString(2, vo.getId());
				ps.setString(3, vo.getName());
				ps.setString(4, vo.getMsg());
				ps.executeUpdate();
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}finally
			{
				disConnection();
			}
		}
		public List<ReplyVO>replyListData(int fno)
		{
			List<ReplyVO> list=new ArrayList<ReplyVO>();
			try
			{
				getConnection();
				String sql="SELECT rno,fno,id,name,msg,"
						+ "TO_CHAR(regdate,'YYYY-MM-DD HH24:MI:SS') "
						+ "FROM reply "
						+ "WHERE fno="+fno
						+ "ORDER BY rno DESC";
				ps=conn.prepareStatement(sql);
				ResultSet rs=ps.executeQuery();
				while(rs.next())
				{
					ReplyVO vo=new ReplyVO();
					vo.setRno(rs.getInt(1));
					vo.setFno(rs.getInt(2));
					vo.setId(rs.getString(3));
					vo.setName(rs.getString(4));
					vo.setMsg(rs.getString(5));
					vo.setDbday(rs.getString(6));
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
		public void replyDelete(int rno)
		{
			try
			{
				getConnection();
				String sql="DELETE FROM reply "
						+ "WHERE rno="+rno;
				ps=conn.prepareStatement(sql);
				ps.executeUpdate();
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}finally
			{
				disConnection();
			}
		}
		public void replyUpdate(int rno, String msg)
		{
			try
			{
				getConnection();
				String sql="UPDATE reply SET "
						+ "msg=? "
						+ "WHERE rno=?";
				ps=conn.prepareStatement(sql);
				ps.setString(1, msg);
				ps.setInt(2, rno);
				ps.executeUpdate();
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}finally
			{
				disConnection();
			}
		}
}
