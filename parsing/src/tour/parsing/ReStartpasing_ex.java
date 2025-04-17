package tour.parsing;

import java.sql.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
/**
      *****************************************  중간 시작 예시 *****************************************
 */
public class ReStartpasing_ex {
    // 공공포털 서비스 키 (URL 인코딩된 상태)
    private static final String SERVICE_KEY =
        "ISunMAfs3B9igj01dGnHJUtaa5gD1SQWneL2zpo5TIQGkuLFPSlB%2BcXAm3x2lYmUtwaElqtUlvUmkpPRKsRpDw%3D%3D";
    // DB 접속 정보
    private static final String DB_URL      = "jdbc:oracle:thin:@211.238.142.124:1521:XE";
    private static final String DB_USER     = "hr_1";
    private static final String DB_PASSWORD = "happy";
    // API 필수 파라미터
    private static final String MOBILE_OS  = "etc";
    private static final String MOBILE_APP = "test";

    // 재시작할 content_id (파싱을 이 값부터 다시 시작)
    private static final int RESUME_FROM = 126535;

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            // 아직 처리되지 않은 레코드 중, content_id >= RESUME_FROM부터 조회
            String fetchSql =
                "SELECT t.no, t.content_id, t.content_type " +
                "FROM tour t " +
                "LEFT JOIN tour_intro ti ON t.no = ti.tour_no " +
                "WHERE ti.tour_no IS NULL AND t.content_id >= ?";

            try (PreparedStatement fetchPs = conn.prepareStatement(fetchSql)) {
                fetchPs.setInt(1, RESUME_FROM);
                try (ResultSet rs = fetchPs.executeQuery()) {
                    while (rs.next()) {
                        int tourNo        = rs.getInt("no");
                        int contentIdInt  = rs.getInt("content_id");
                        String contentId  = String.valueOf(contentIdInt);
                        int contentTypeId = rs.getInt("content_type");

                        // 파싱 전 디버깅
                        System.out.printf("▶ 파싱 시작: contentId=%s, contentTypeId=%d%n", contentId, contentTypeId);

                        String introUrl =
                            "https://apis.data.go.kr/B551011/KorService1/detailIntro1" +
                            "?MobileOS="     + MOBILE_OS +
                            "&MobileApp="    + MOBILE_APP +
                            "&_type=json" +
                            "&contentId="    + contentId +
                            "&contentTypeId="+ contentTypeId +
                            "&serviceKey="   + SERVICE_KEY;

                        String introJson = Jsoup.connect(introUrl)
                                                .ignoreContentType(true)
                                                .execute()
                                                .body();

                        JSONObject root     = (JSONObject) JSONValue.parse(introJson);
                        JSONObject response = (JSONObject) root.get("response");
                        JSONObject header   = (JSONObject) response.get("header");
                        String     resultCode = header.get("resultCode").toString();

                        if (!"0000".equals(resultCode)) {
                            System.err.printf("▶ API 오류 (contentId=%s): %s%n%s%n",
                                              contentId,
                                              header.get("resultMsg"),
                                              introJson);
                            continue;
                        }

                        JSONObject body    = (JSONObject) response.get("body");
                        Object    itemsObj = ((JSONObject) body.get("items")).get("item");
                        JSONArray itemArr = itemsObj instanceof JSONArray
                                          ? (JSONArray) itemsObj
                                          : new JSONArray() {{ add(itemsObj); }};
                        JSONObject item    = (JSONObject) itemArr.get(0);

                        String infoTel  = optString(item, "infocenter");
                        String restDate = optString(item, "restdate");
                        String useTime  = optString(item, "usetime");
                        String parking  = optString(item, "parking");
                        String babyCart = optString(item, "chkbabycarriage");
                        String payCard  = optString(item, "chkcreditcard");
                        int    h1       = optInt(item, "heritage1");
                        int    h2       = optInt(item, "heritage2");
                        int    h3       = optInt(item, "heritage3");

                        Map<String,Object> cols = new LinkedHashMap<>();
                        cols.put("tour_no", tourNo);
                        if (!infoTel .isEmpty()) cols.put("info_tel", infoTel);
                        if (!restDate.isEmpty()) cols.put("restdate", restDate);
                        if (!useTime .isEmpty()) cols.put("usetime", useTime);
                        if (!parking .isEmpty()) cols.put("parking", parking);
                        if (!babyCart.isEmpty()) cols.put("babycart", babyCart);
                        if (!payCard .isEmpty()) cols.put("pay_card", payCard);
                        cols.put("heritage1", h1);
                        cols.put("heritage2", h2);
                        cols.put("heritage3", h3);

                        StringJoiner names = new StringJoiner(", ");
                        StringJoiner vals  = new StringJoiner(", ");
                        cols.keySet().forEach(c -> { names.add(c); vals.add("?"); });
                        String insertSql = "INSERT INTO tour_intro(" + names + ") VALUES(" + vals + ")";

                        try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                            int idx = 1;
                            for (Object v : cols.values()) insertPs.setObject(idx++, v);
                            int rowCount = insertPs.executeUpdate();
                            System.out.printf("   ▶ DB 삽입 완료: tour_no=%d, rows=%d%n", tourNo, rowCount);
                        }

                        Thread.sleep(100);
                    }
                }
            }

            conn.commit();
            System.out.println("▶ 모든 데이터 처리 완료, tour_intro 테이블 업데이트 성공!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String optString(JSONObject obj, String key) {
        Object v = obj.get(key);
        return (v == null || "null".equals(v.toString())) ? "" : v.toString().trim();
    }

    private static int optInt(JSONObject obj, String key) {
        try {
            return Integer.parseInt(obj.get(key).toString());
        } catch (Exception e) {
            return 0;
        }
    }
}
