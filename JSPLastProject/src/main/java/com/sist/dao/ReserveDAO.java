package com.sist.dao;

import java.util.*;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.sist.commons.*;
import com.sist.vo.*;

public class ReserveDAO {
	private static SqlSessionFactory ssf;
	static
	{
		try
		{
			ssf=CreateSqlSessionFactory.getSsf();
		}catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	public static List<FoodVO> reserveFoodData(Map map)
	{
		SqlSession session=ssf.openSession();
		List<FoodVO> list=session.selectList("reserveFoodData", map);
		session.close();
		return list;
	}
	/*
	  <select id="reserveDayTimeInfoData" resultType="string" parameterType="int">
	    SELECT time FROM reserve_day
	    WHERE rno=#{rno}
	  </select>
	  <select id="reserveGetTimeData" resultType="string" parameterType="int">
	    SELECT time FROM reserve_time
	    WHERE tno=#{tno}
	  </select>
	 */
	public static String reserveDayTimeInfoData(int rno)
	{
		SqlSession session=ssf.openSession();
		String times=session.selectOne("reserveDayTimeInfoData", rno);
		session.close();
		return times;
	}
	
	public static String reserveGetTimeData(int tno)
	{
		SqlSession session=ssf.openSession();
		String times=session.selectOne("reserveGetTimeData", tno);
		session.close();
		return times;
	}
	/*
	  <insert id="reserveInsert" parameterType="ReserveVO">
	    INSERT INTO reserve_info VALUES(
	      (SELECT NVL(MAX(rno)+1,1) FROM reserve_info),
	      #{id},#{fno},#{day},#{time},#{inwon},
	      SYSDATE, 'n'
	    )
	  </insert>
	 */
	public static void reserveInsert(ReserveVO vo)
	{
		SqlSession session=ssf.openSession(true);
		session.insert("", vo);
		session.close();
	}
}
