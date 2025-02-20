package com.sist.model;

import com.sist.ann.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

public class GoodsModel {
	@RequestMapping("goods/list.do")
	public String goods_list(HttpServletRequest request)
	{
		request.setAttribute("msg", "상품 목록");
		return "../goods/list.jsp";
	}
}
