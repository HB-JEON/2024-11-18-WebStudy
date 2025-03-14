package com.sist.vo;

import java.util.Date;

import lombok.Data;

@Data
public class ReplyVO {
	private int cno,rno,type,group_id,group_step,group_tab,depth,root,likecount;
	private String id,name,sex,msg,dbday;
	private Date regdate;
}
