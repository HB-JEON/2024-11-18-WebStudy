package com.sist.dao;

import java.util.*;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.sist.vo.*;
import com.sist.commons.*;

public class ReplyDAO {
	 private static SqlSessionFactory ssf;
	 static
	 {
		 ssf=CreateSqlSessionFactory.getSsf();
	 }
	 public static List<ReplyVO> replyListData(ReplyVO vo)
	 {
		 SqlSession session=ssf.openSession();
		 List<ReplyVO> list=session.selectList("replyListData", vo);
		 session.close();
		 return list;
	 }
	 public static void replyInsert(ReplyVO vo)
	 {
		 SqlSession session=ssf.openSession(true);
		 session.insert("replyInsert", vo);
		 session.close();
	 }
}
