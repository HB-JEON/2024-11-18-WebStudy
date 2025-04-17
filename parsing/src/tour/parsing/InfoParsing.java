package tour.parsing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class InfoParsing {

    // TODO: 환경에 맞게 변경
    private static final String JDBC_URL    = "jdbc:oracle:thin:@211.238.142.124:1521:XE";
    private static final String JDBC_USER   = "hr_1";
    private static final String JDBC_PASS   = "happy";
    private static final String SERVICE_KEY =
        "ISunMAfs3B9igj01dGnHJUtaa5gD1SQWneL2zpo5TIQGkuLFPSlB%2BcXAm3x2lYmUtwaElqtUlvUmkpPRKsRpDw%3D%3D";

    /**
     * detailInfo1 API 호출 → tour_info 테이블에 INSERT
     * @param contentId     tour.content_id 값
     * @param contentTypeId tour.content_type 값
     * @param tourNo        tour.no (FK)
     */
    public void parseAndSaveInfo(int contentId, int contentTypeId, int tourNo) {
        Connection        con = null;
        PreparedStatement ps  = null;
        BufferedReader    rd  = null;

        try {
            // 1) API URL 생성
            String infoUrl = String.format(
                "https://apis.data.go.kr/B551011/KorService1/detailInfo1?serviceKey=%s&MobileOS=etc&MobileApp=test&_type=json&contentId=%d&contentTypeId=%d",
                SERVICE_KEY, contentId, contentTypeId
            );
            HttpURLConnection connApi = (HttpURLConnection) new URL(infoUrl).openConnection();
            connApi.setRequestMethod("GET");

            // 2) 응답 읽기
            rd = new BufferedReader(new InputStreamReader(connApi.getInputStream(), "UTF-8"));
            JSONParser parser = new JSONParser();
            JSONObject root   = (JSONObject) parser.parse(rd);
            JSONObject body   = (JSONObject) ((JSONObject) root.get("response")).get("body");

            Object itemsObj = ((JSONObject) body.get("items")).get("item");
            java.util.List<JSONObject> infoList = new java.util.ArrayList<>();
            if (itemsObj instanceof JSONArray) {
                for (Object o : (JSONArray) itemsObj) {
                    if (o instanceof JSONObject) infoList.add((JSONObject) o);
                }
            } else if (itemsObj instanceof JSONObject) {
                infoList.add((JSONObject) itemsObj);
            }

            // 3) DB 연결 및 INSERT 준비
            con = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
            String sql = "INSERT INTO tour_info (info_no, tour_no, infoname, infotext, fldgubun, serialnum) " +
                         "VALUES (TOUR_INFO_INFO_NO_SEQ.nextval, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);

            // 4) 반복 INSERT
            for (JSONObject info : infoList) {
                ps.setInt(1, tourNo);
                ps.setString(2, (String) info.get("infoname"));
                ps.setString(3, (String) info.get("infotext"));

                Object fldObj = info.get("fldgubun");
                int fldgubun = fldObj instanceof Number
                    ? ((Number) fldObj).intValue()
                    : Integer.parseInt((String) fldObj);
                ps.setInt(4, fldgubun);

                Object serObj = info.get("serialnum");
                int serialnum = serObj instanceof Number
                    ? ((Number) serObj).intValue()
                    : Integer.parseInt((String) serObj);
                ps.setInt(5, serialnum);

                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rd   != null) rd.close();   } catch (Exception ignored) {}
            try { if (ps   != null) ps.close();   } catch (Exception ignored) {}
            try { if (con  != null) con.close();  } catch (Exception ignored) {}
        }
    }

    public static void main(String[] args) {
        // DB 연결
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
             Statement stmt = conn.createStatement()) {
        	

            InfoParsing parser = new InfoParsing();
            String selectSql = "SELECT no, content_id, content_type FROM tour WHERE content_id IS NOT NULL";
            try (PreparedStatement psSelect = conn.prepareStatement(selectSql);
                 ResultSet rs = psSelect.executeQuery()) {
                while (rs.next()) {
                    int tourNo       = rs.getInt("no");
                    int contentId    = rs.getInt("content_id");
                    int contentType  = rs.getInt("content_type");
                    System.out.printf("▶ 파싱 시작: tour_no=%d, contentId=%d, contentType=%d%n",
                                      tourNo, contentId, contentType);

                    parser.parseAndSaveInfo(contentId, contentType, tourNo);
                }
            }
            System.out.println("▶ content_id가 존재하는 tour만 Info 파싱 완료");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
