package com.sist.vo;
/*
    JSP => 사용자 요청에 따른 데이터 출력
           <% %> <%= %> 사용하지 않음
           page / taglib
                  ------
           내장 객체
           ------- request / response / session
                   cookie
           HTML / CSS / JavaScript
    DAO => DBCP / MyBatis / JPA
                  -------------
     VO => 자바로 사용
 */
// 데이터를 모아서 한 번에 브라우저로 전송 목적
// replace("\\"File.separator) => getReource(".")
// DESC emp
import java.util.*;
public class EmpVO {
	private int empno,sal;
	private String ename,job;
	private Date hiredate;
	// 사용이 가능한 기능 부여
	public int getEmpno() {
		return empno;
	}
	public void setEmpno(int empno) {
		this.empno = empno;
	}
	public int getSal() {
		return sal;
	}
	public void setSal(int sal) {
		this.sal = sal;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public Date getHiredate() {
		return hiredate;
	}
	public void setHiredate(Date hiredate) {
		this.hiredate = hiredate;
	}
	
}
