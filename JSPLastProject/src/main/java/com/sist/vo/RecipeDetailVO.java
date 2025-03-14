package com.sist.vo;

import lombok.Data;

/*
NO           NOT NULL	NUMBER         
POSTER       NOT NULL 	VARCHAR2(4000) 
TITLE        NOT NULL 	VARCHAR2(1000) 
CHEF         NOT NULL 	VARCHAR2(1000) 
CHEF_POSTER  NOT NULL 	VARCHAR2(1000) 
CHEF_PROFILE NOT NULL 	VARCHAR2(1000) 
INFO1        NOT NULL	VARCHAR2(100)  
INFO2        NOT NULL	VARCHAR2(100)  
INFO3        NOT NULL 	VARCHAR2(100)  
CONTENT      NOT NULL 	VARCHAR2(4000) 
FOODMAKE     NOT NULL 	CLOB           
DATA                  	CLOB 

http://localhost/JSPLastProject/qna/qna_update.do?no=1
http://localhost/JSPLastProject/qna/qna_detail.do?no=1
http://localhost/JSPLastProject/board/board_update.do?no=16&page=1
http://localhost/JSPLastProject/board/board_detail.do?no=16&page=1
 */
@Data
public class RecipeDetailVO {
	private int no;
	private String poster, title, chef, chef_poster,
	        chef_profile,info1,info2,info3,content,foodmake,data;
}
