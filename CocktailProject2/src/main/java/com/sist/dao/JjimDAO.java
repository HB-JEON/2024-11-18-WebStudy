package com.sist.dao;

import java.util.*;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.sist.commons.CreateSqlSessionFactory;
import com.sist.vo.Cocktail_ProductVO;
import com.sist.vo.JjimVO;

public class JjimDAO {
	private static SqlSessionFactory ssf;
	static
	{
		ssf=CreateSqlSessionFactory.getSsf();
	}
	
	public static int jjimCheckCount(JjimVO vo)
	{
		SqlSession session=null;
		int count=0;
		try
		{
			session=ssf.openSession();
			count=session.selectOne("jjimCheckCount", vo);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}finally
		{
			if(session!=null)
				session.close();
		}
		return count;
	}
	
	public static void jjimInsert(JjimVO vo)
	{
		SqlSession session=null;
		try
		{
			session=ssf.openSession(true);
			session.insert("jjimInsert", vo);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}finally
		{
			if(session!=null)
				session.close();
		}
	}
	
	public static List<JjimVO> jjimCocktail_productListData(String id)
	{
		SqlSession session=null;
		List<JjimVO> list=null;
		try
		{
			session=ssf.openSession();
			list=session.selectList("jjimCocktail_productListData", id);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}finally
		{
			if(session!=null)
				session.close();
		}
		return list;
	}
	
	public static void jjimDelete(int jno)
	{
		SqlSession session=null;
		try
		{
			session=ssf.openSession(true);
			session.delete("jjimDelete", jno);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}finally
		{
			if(session!=null)
				session.close();
		}
	}
	
}
