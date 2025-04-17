package tour.parsing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ImageParsing {
    // --- API 정보 ---
    private static final String SERVICE_KEY      =
        "ISunMAfs3B9igj01dGnHJUtaa5gD1SQWneL2zpo5TIQGkuLFPSlB%2BcXAm3x2lYmUtwaElqtUlvUmkpPRKsRpDw%3D%3D";
    private static final String DETAIL_IMAGE_URL =
        "https://apis.data.go.kr/B551011/KorService1/detailImage1";

    // --- DB 연결 정보 (환경에 맞게 수정) ---
    private static final String DB_URL      = "jdbc:oracle:thin:@211.238.142.124:1521:XE";
    private static final String DB_USER     = "HR_1";
    private static final String DB_PASSWORD = "happy";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            // tour 테이블에서 처음부터 모든 레코드를 가져오기
            String sqlFetch =
              "SELECT t.no, t.content_id, t.content_type FROM tour t";
            try (PreparedStatement psFetch = conn.prepareStatement(sqlFetch);
                 ResultSet rs = psFetch.executeQuery()) {

                while (rs.next()) {
                    int tourNo        = rs.getInt("no");
                    int contentId     = rs.getInt("content_id");
                    int contentTypeId = rs.getInt("content_type");

                    System.out.printf(
                      "▶ 이미지 파싱 시작: tourNo=%d, contentId=%d, contentTypeId=%d%n",
                      tourNo, contentId, contentTypeId
                    );

                    try {
                        List<String> serialNums =
                          fetchImageSerialNums(contentId, contentTypeId);

                        if (serialNums.isEmpty()) {
                            System.out.printf("▶ 이미지 없음: contentId=%d%n", contentId);
                        } else {
                            saveImageSerialNums(conn, tourNo, serialNums);
                            conn.commit();
                        }
                    } catch (Exception e) {
                        conn.rollback();
                        System.err.printf(
                          "▶ 처리 실패: tourNo=%d contentId=%d → %s%n",
                          tourNo, contentId, e.getMessage()
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * detailImage1 API를 호출해서 반환된 serialnum 목록을 리턴
     */
    public static List<String> fetchImageSerialNums(
        int contentId, int contentTypeId
    ) throws Exception {
        String urlStr = String.format(
          "%s?MobileOS=etc&MobileApp=test&_type=json"
          + "&serviceKey=%s&contentId=%d&contentTypeId=%d",
          DETAIL_IMAGE_URL,
          SERVICE_KEY,
          contentId,
          contentTypeId
        );

        String json;
        try {
            HttpURLConnection conn = (HttpURLConnection)new URL(urlStr).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("HTTP 에러 코드: " + conn.getResponseCode());
            }

            StringBuilder sb = new StringBuilder();
            try (BufferedReader in = new BufferedReader(
                   new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                String line;
                while ((line = in.readLine()) != null) sb.append(line);
            }
            json = sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("API 호출 실패: " + e.getMessage());
        }

        // JSON parsing and null checks
        JSONParser parser = new JSONParser();
        JSONObject root;
        try {
            Object parsed = parser.parse(json);
            if (!(parsed instanceof JSONObject)) {
                throw new RuntimeException("파싱된 JSON이 객체가 아님: " + json);
            }
            root = (JSONObject) parsed;
        } catch (ParseException | ClassCastException e) {
            throw new RuntimeException("JSON 파싱 오류: " + e.getMessage() + " 입력=" + json);
        }

        JSONObject response = (JSONObject) root.get("response");
        if (response == null) {
            throw new RuntimeException("API 응답에 response 노드가 없습니다. JSON=" + json);
        }
        JSONObject header = (JSONObject) response.get("header");
        String     rc     = header != null ? (String)header.get("resultCode") : null;
        if (!"0000".equals(rc)) {
            String msg = header != null ? (String)header.get("resultMsg") : "no header";
            throw new RuntimeException("API 에러: code=" + rc + ", msg=" + msg + " JSON=" + json);
        }

        JSONObject body  = (JSONObject) response.get("body");
        if (body == null) {
            return Collections.emptyList();
        }
        JSONObject items = (JSONObject) body.get("items");
        if (items == null || items.get("item") == null) {
            return Collections.emptyList();
        }

        Object item = items.get("item");
        List<String> serialNums = new ArrayList<>();
        if (item instanceof JSONArray) {
            for (Object o : (JSONArray) item) {
                JSONObject img = (JSONObject) o;
                Object sn = img.get("serialnum");
                if (sn != null) serialNums.add(sn.toString());
            }
        } else if (item instanceof JSONObject) {
            JSONObject img = (JSONObject) item;
            Object sn = img.get("serialnum");
            if (sn != null) serialNums.add(sn.toString());
        }
        return serialNums;
    }

    /**
     * 받아온 serialnum 목록을 tour_img 테이블에 배치 삽입
     */
    private static void saveImageSerialNums(
      Connection conn, int tourNo, List<String> serialNums
    ) throws SQLException {
        String sql =
          "INSERT INTO tour_img"
          + "(img_no, tour_no, img_og, img_name, serialnum) "
          + "VALUES(tour_img_img_no_seq.nextval, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String serial : serialNums) {
                ps.setInt   (1, tourNo);
                ps.setString(2, serial);
                ps.setString(3, serial);
                ps.setString(4, serial);
                ps.setString(5, serial);
                ps.addBatch();
            }
            int[] results = ps.executeBatch();
            System.out.printf("▶ 저장 완료: tourNo=%d, 총 %d건%n", tourNo, results.length);
        }
    }
}
