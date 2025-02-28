package com.sist.product;

import java.sql.*;

public class productDAO {
    private Connection conn;
    private PreparedStatement ps;
    private final String URL = "jdbc:oracle:thin:@211.238.142.124:1521:XE";
    private static productDAO dao;

    // ✅ 싱글톤 패턴 적용
    public static productDAO newInstance() {
        if (dao == null) {
            dao = new productDAO();
        }
        return dao;
    }

    // ✅ DB 연결 (오류 발생 시 메시지 출력)
    public void getConnection() {
        try {
            conn = DriverManager.getConnection(URL, "hr_1", "happy");
            System.out.println("✅ DB 연결 성공!");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("❌ DB 연결 실패! URL, 사용자명, 비밀번호를 확인하세요.");
        }
    }

    // ✅ DB 연결 해제 (conn = null 처리)
    public void disConnection() {
        try {
            if (ps != null) ps.close();
            if (conn != null) {
                conn.close();
                conn = null;
                System.out.println("✅ DB 연결 해제 완료!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
    ✅ DB TABLE 구조:
    PRODUCT_NO   NUMBER
    NAME         VARCHAR2(100 BYTE)
    TYPE         VARCHAR2(30 BYTE)
    PRICE        VARCHAR2(200 BYTE)  ✅ String 저장
    POSTER       VARCHAR2(200 BYTE)
    DELIVER      VARCHAR2(200 BYTE)
    ALC          VARCHAR2(200 BYTE)  ✅ String 저장
    VOLUMN       VARCHAR2(200 BYTE)  ✅ String 저장
    LOC          VARCHAR2(100 BYTE)
    SUGAR        VARCHAR2(200 BYTE)
    BODY         VARCHAR2(200 BYTE)
    CONTENT      CLOB
    */

    // ✅ 상품 정보 DB 저장
    public void productInsert(productVO vo) {
        try {
            getConnection();  // ✅ DB 연결
            if (conn == null) {  // 🚨 `conn`이 `null`이면 실행 중지
                System.out.println("❌ DB 연결이 설정되지 않았습니다!");
                return;
            }

            String sql = "INSERT INTO cocktail_product (product_no, cno, name, type, price, poster, alc, volumn, loc, sugar, body, content) "
                       + "VALUES (product_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);

            // ✅ 데이터 저장
            ps.setInt(1, vo.getCno());
            ps.setString(2, vo.getName());
            ps.setString(3, vo.getType());
            ps.setString(4, vo.getPrice());  // ✅ VARCHAR2(200)으로 저장
            ps.setString(5, vo.getPoster());
            ps.setString(6, vo.getAlc());   // ✅ VARCHAR2(200)으로 저장
            ps.setString(7, vo.getVolumn()); // ✅ VARCHAR2(200)으로 저장
            ps.setString(8, vo.getLoc());
            ps.setString(9, vo.getSugar());
            ps.setString(10, vo.getBody());
         // ✅ `content` 값이 `null`이면 빈 문자열로 저장
            String content = (vo.getContent() != null) ? vo.getContent() : "";
            ps.setCharacterStream(11, new java.io.StringReader(content), content.length());
            
            // ✅ SQL 실행
            ps.executeUpdate();
            System.out.println(vo.getName());

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            disConnection(); // ✅ DB 연결 해제
        }
    }
}