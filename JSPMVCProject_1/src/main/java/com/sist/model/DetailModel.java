package com.sist.model;

import org.apache.ibatis.session.SqlSession;
import com.sist.dao.*;
import com.sist.vo.*;
import com.sist.vo.BoardVO;
import jakarta.servlet.http.HttpServletRequest;

/*
    인터페이스 / 상속
     => 클래스의 영향력이 높음 => 결합성이 높은 프로그램
     => 독립적인 클래스
        ------------- POJO
         => 인터페이스를 사용하지 않을 예정 => 어노테이션
     => 기능별 클래스를 설정
        -----------
 */
public class DetailModel implements Model {

	@Override
	public String handlerRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub\
		String no=request.getParameter("no");
		BoardVO vo=BoardDAO.boardDetailData(Integer.parseInt(no));
		// 전송
		request.setAttribute("vo", vo);
		return "board/detail.jsp";
	}
}
