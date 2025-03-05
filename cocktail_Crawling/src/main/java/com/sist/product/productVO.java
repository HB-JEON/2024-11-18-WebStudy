package com.sist.product;
/*
PRODUCT_NO			NUMBER
NAME				VARCHAR2(100 BYTE)
TYPE				VARCHAR2(30 BYTE)
PRICE				NUMBER
POSTER				VARCHAR2(200 BYTE)
DELIVER				VARCHAR2(200 BYTE)
ALC					NUMBER
VOLUMN				NUMBER
LOC					VARCHAR2(100 BYTE)
SUGAR				VARCHAR2(200 BYTE)
BODY				VARCHAR2(200 BYTE)
CONTENT				CLOB
 */
public class productVO {
	private int product_no, cno, hit;
	private String name, type, poster, loc, sugar, body, content, alc, price, volumn;
	public int getProduct_no() {
		return product_no;
	}
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	public int getCno() {
		return cno;
	}
	public void setCno(int cno) {
		this.cno = cno;
	}
	public void setProduct_no(int product_no) {
		this.product_no = product_no;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getAlc() {
		return alc;
	}
	public void setAlc(String alc) {
		this.alc = alc;
	}
	public String getVolumn() {
		return volumn;
	}
	public void setVolumn(String volumn) {
		this.volumn = volumn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPoster() {
		return poster;
	}
	public void setPoster(String poster) {
		this.poster = poster;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public String getSugar() {
		return sugar;
	}
	public void setSugar(String sugar) {
		this.sugar = sugar;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getContent() {
        return (content != null) ? content : ""; // ✅ Null 방지
    }

    public void setContent(String content) {
        this.content = content;
    }
	
}
