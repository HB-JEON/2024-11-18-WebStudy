package com.sist.dao;
/*
    DAO : 오라클 연동
    VO : 데이터를 모아서 한번에 전송
    Model : JSP에서 데이터 관리 => 자바로 변경
     | 데이터를 오라클에서 읽어서 JSP 전송
     <%
        사용자가 전송한 데이터 받기
        오라클 연동
        출력에 필요한 데이터를 가져오기
     %>
     ------ 완전히 자바로 분리 => Controller(Servlet)
     => 브라우저에서 호출 가능한 파일
        ------------------------ JSP / Servlet
    --------------------------------------+Model
    
    Controller : 집중
       | 분리 => MSA => 기능별 서버
     배포(AWS) => 단점 - 수정 시 다시 업로드
                 ---- CI/CD 
 */
import java.util.*;
import java.sql.*;
import javax.naming.*;
import javax.sql.*;
/*
    DBCP
    ---- 톰캣
         --- 미리 연결하여 연결에 소모되는 시간을 단축 
             Connection 객체를 제한
         
         1. Connection 객체 생성 ==> 메모리에 저장 (POOL)
         2. 사용자가 요청시마다 오라클에 연결된 Connection 주소를 가져오기
              => getConnection()
         3. Connection을 이용하여 요청 처리 => 결과값
              => 사용자 정의(기능 처리)
         4. 다시 POOL 안으로 반환 => 재사용
              => disConnection()
     => MyBatis에서 자동화 처리
         <dataSource type="POOLED">
 */

import com.sist.vo.EmpVO;
public class EmpDAO {
	private Connection conn;
	private PreparedStatement ps;
	// POOL => Connection 객체를 얻어온다.
	public void getConnection()
	{
		try
		{
			// => 메모리구조 => 탐색기 형식
			// 1. 탐색기 열기
			Context init=new InitialContext();
			// 2. C 드라에브에 접근
			Context c=(Context)init.lookup("java://comp/env");
			// 3. 원하는 파일 선택
			DataSource ds=(DataSource)c.lookup("jdbc/oracle");
			// DataSource => 데이터베이스에 대한 모든 정보 보유
			// 4. DataSource 이용하여 Connection 연결 정보 읽기
			conn=ds.getConnection();
		}catch(Exception ex) {}
	}
	// 사용 후 POOL 반환
	public void disConnection()
	{
		try
		{
			if(ps!=null) ps.close();
			if(conn!=null) conn.close();
		}catch(Exception ex) {}
	}
	public List<EmpVO> empListData()
	{
		List<EmpVO> list=new ArrayList<EmpVO>();
		try
		{
			getConnection();
			String sql="SELECT empno,ename,job,hiredate,sal "
					+ "FROM emp";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				EmpVO vo=new EmpVO();
				vo.setEmpno(rs.getInt(1));
				vo.setEname(rs.getString(2));
				vo.setJob(rs.getString(3));
				vo.setHiredate(rs.getDate(4));
				vo.setSal(rs.getInt(5));
				list.add(vo);
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}finally
		{
			// 반환 => 재사용
			disConnection();
		}
		return list;
	}
}
