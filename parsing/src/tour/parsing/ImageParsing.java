package tour.parsing;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import java.sql.*;
import java.util.*;

public class ImageParsing {
    // 공공포털 서비스 키를 입력하세요
    private static final String SERVICE_KEY = 
    		"ISunMAfs3B9igj01dGnHJUtaa5gD1SQWneL2zpo5TIQGkuLFPSlB%2BcXAm3x2lYmUtwaElqtUlvUmkpPRKsRpDw%3D%3D";
    private static final String BASE_URL = "http://apis.data.go.kr/B551011/KorService1/detailImage1";
    // DB 접속 정보 (프로젝트 환경에 맞게 수정)
    private static final String DB_URL = "jdbc:oracle:thin:@211.238.142.124:1521:XE";
    private static final String DB_USER = "HR_1";
    private static final String DB_PASSWORD = "happy";

    public static void main(String[] args) {
        List<Integer> contentIds = fetchContentIdsFromTour();
        for (int contentId : contentIds) {
            parseAndSaveImages(contentId);
        }
    }

    private static void parseAndSaveImages(int contentId) {
        String url = String.format(
            "%s?serviceKey=%s&contentId=%d&subImageYN=Y&MobileOS=etc&MobileApp=test&_type=json",
            BASE_URL, SERVICE_KEY, contentId
        );
        System.out.println(">>> Fetching detailImage1 for contentId=" + contentId);
        try (Connection conn = getConnection();
             PreparedStatement selectTourStmt = conn.prepareStatement(
                 "SELECT no FROM tour WHERE content_id = ?");
             PreparedStatement insertImgStmt = conn.prepareStatement(
                 "INSERT INTO tour_img(img_no, tour_no, img_og, img_name, serialnum) " +
                 "VALUES(tour_img_img_no_seq.nextval, ?, ?, ?, ?)") ) {

            String responseBody = Jsoup.connect(url)
                                       .ignoreContentType(true)
                                       .execute()
                                       .body();

            if (responseBody == null || !responseBody.trim().startsWith("{")) {
                System.out.println("[SKIP] Non-JSON or empty for contentId=" + contentId);
                return;
            }

            JSONObject jsonResponse;
            try {
                jsonResponse = (JSONObject) new JSONParser().parse(responseBody);
            } catch (ParseException pe) {
                System.out.println("[ERROR] JSON parse failed for contentId=" + contentId + ": " + pe.getMessage());
                return;
            }

            JSONObject body = (JSONObject)((JSONObject)((JSONObject)jsonResponse.get("response")).get("body"));
            JSONObject items = (JSONObject) body.get("items");
            Object itemObj = items.get("item");

            if (itemObj == null || itemObj instanceof String) {
                System.out.println("[INFO] No usable detailImage1 items for contentId=" + contentId);
                return;
            }

            JSONArray itemArray;
            if (itemObj instanceof JSONArray) {
                itemArray = (JSONArray) itemObj;
            } else {
                itemArray = new JSONArray();
                itemArray.add(itemObj);
            }

            selectTourStmt.setInt(1, contentId);
            ResultSet rs = selectTourStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("[WARN] No tour record for contentId=" + contentId);
                rs.close();
                return;
            }
            int tourNo = rs.getInt("no");
            rs.close();

            for (Object o : itemArray) {
                JSONObject imgObj = (JSONObject) o;
                String originImgUrl = (String) imgObj.get("originimgurl");
                String imgName      = (String) imgObj.get("imgname");
                String serialnum    = imgObj.get("serialnum").toString();

                insertImgStmt.setInt(1, tourNo);
                insertImgStmt.setString(2, originImgUrl);
                insertImgStmt.setString(3, imgName);
                insertImgStmt.setString(4, serialnum);
                insertImgStmt.executeUpdate();
            }
            System.out.println("[SUCCESS] Inserted " + itemArray.size() + " images for tour_no=" + tourNo);

        } catch (SQLException e) {
            System.err.println("[ERROR] DB error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private static List<Integer> fetchContentIdsFromTour() {
        List<Integer> ids = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT content_id FROM tour");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("content_id"));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] fetchContentIds: " + e.getMessage());
        }
        return ids;
    }
}