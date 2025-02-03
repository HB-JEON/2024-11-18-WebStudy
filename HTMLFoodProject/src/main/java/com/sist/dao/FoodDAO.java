package com.sist.dao;
import com.sist.vo.*;
import java.util.*;
import java.sql.*;
public class FoodDAO {
	// 연결 => 오라클 연결
	private Connection conn;
	// SQL => 송신, 결과값 수신
	private PreparedStatement ps;
	// 연결 => URL
	private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	// 한사람 당 한개의 DAO 사용 => 싱글턴
	private static FoodDAO dao;
	
	// 1. 드라이버 등록
	public FoodDAO()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception ex) {}
	}
	// 2. 싱글턴 => 반드시 static (static 메모리 공간은 한개)
	public static FoodDAO newInstance()
	{
		if(dao==null)
			dao=new FoodDAO();
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
	
	// 목록 출력 => FoodVO (맛집 한개에 대한 모든 정보)
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
}
