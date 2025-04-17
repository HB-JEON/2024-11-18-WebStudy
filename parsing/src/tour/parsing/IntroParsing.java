package tour.parsing;

import java.sql.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;

public class IntroParsing {
    // 공공포털 서비스 키 (URL 인코딩된 상태)
    private static final String SERVICE_KEY =
        "ISunMAfs3B9igj01dGnHJUtaa5gD1SQWneL2zpo5TIQGkuLFPSlB%2BcXAm3x2lYmUtwaElqtUlvUmkpPRKsRpDw%3D%3D";
    // DB 접속 정보
    private static final String DB_URL      = "jdbc:oracle:thin:@211.238.142.124:1521:XE";
    private static final String DB_USER     = "hr_1";
    private static final String DB_PASSWORD = "happy";
    // API 필수 파라미터
    private static final String MOBILE_OS   = "etc";
    private static final String MOBILE_APP  = "test";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            // tour 테이블에서 no, content_id, content_type 조회
            String fetchSql = "SELECT no, content_id, content_type FROM tour";
            try (PreparedStatement fetchPs = conn.prepareStatement(fetchSql);
                 ResultSet rs = fetchPs.executeQuery()) {

                while (rs.next()) {
                    int   tourNo        = rs.getInt("no");
                    int   contentIdInt  = rs.getInt("content_id");
                    String contentId    = String.valueOf(contentIdInt);
                    int   contentTypeId = rs.getInt("content_type");

                    // 파싱 전 디버깅
                    System.out.printf("▶ 파싱 시작: contentId=%s, contentTypeId=%d%n",
                                      contentId, contentTypeId);

                    // detailIntro1 호출 URL
                    String introUrl = "https://apis.data.go.kr/B551011/KorService1/detailIntro1"
                                    + "?MobileOS="     + MOBILE_OS
                                    + "&MobileApp="    + MOBILE_APP
                                    + "&_type=json"
                                    + "&contentId="    + contentId
                                    + "&contentTypeId="+ contentTypeId
                                    + "&serviceKey="   + SERVICE_KEY;

                    String introJson = Jsoup.connect(introUrl)
                                            .ignoreContentType(true)
                                            .execute()
                                            .body();

                    // ─── resultCode를 response.header에서 꺼내기 ───
                    JSONObject root     = (JSONObject) JSONValue.parse(introJson);
                    JSONObject response = (JSONObject) root.get("response");
                    JSONObject header   = (JSONObject) response.get("header");
                    String     resultCode = header.get("resultCode").toString();

                    if (!"0000".equals(resultCode)) {
                        // 전체 JSON 찍어 보면 문제 원인 파악에 도움됩니다.
                        System.err.printf("▶ API 오류 (contentId=%s): %s%n%s%n",
                                          contentId,
                                          header.get("resultMsg"),
                                          introJson);
                        continue;
                    }

                    // ─── 정상 응답 파싱(body → items → item) ───
                    JSONObject body    = (JSONObject) response.get("body");
                    Object    itemsObj = ((JSONObject) body.get("items")).get("item");
                    JSONArray itemArr  = itemsObj instanceof JSONArray
                                       ? (JSONArray) itemsObj
                                       : new JSONArray() {{ add(itemsObj); }};
                    JSONObject item    = (JSONObject) itemArr.get(0);

                    // 필드 추출
                    String infoTel  = optString(item, "infocenter");
                    String restDate = optString(item, "restdate");
                    String useTime  = optString(item, "usetime");
                    String parking  = optString(item, "parking");
                    String babyCart = optString(item, "chkbabycarriage");
                    String payCard  = optString(item, "chkcreditcard");
                    int    h1       = optInt(item, "heritage1");
                    int    h2       = optInt(item, "heritage2");
                    int    h3       = optInt(item, "heritage3");

                    // INSERT용 컬럼·값 맵 구성
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

                    // INSERT SQL 조립 및 실행
                    StringJoiner names = new StringJoiner(", "), vals = new StringJoiner(", ");
                    for (String col : cols.keySet()) {
                        names.add(col);
                        vals.add("?");
                    }
                    String insertSql = "INSERT INTO tour_intro(" + names + ") VALUES(" + vals + ")";
                    try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                        int idx = 1;
                        for (Object value : cols.values()) {
                            insertPs.setObject(idx++, value);
                        }
                        int rowCount = insertPs.executeUpdate();
                        // DB 삽입 디버깅
                        System.out.printf("   ▶ DB 삽입 완료: tour_no=%d, rows=%d%n",
                                          tourNo, rowCount);
                    }

                    // 과부하 방지를 위한 잠깐 대기
                    Thread.sleep(100);
                }
            }

            conn.commit();
            System.out.println("▶ 모든 데이터 처리 완료, tour_intro 테이블 업데이트 성공!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** null 또는 "null" 문자열을 빈 문자열로 처리 */
    private static String optString(JSONObject obj, String key) {
        Object v = obj.get(key);
        return (v == null || "null".equals(v.toString())) ? "" : v.toString().trim();
    }

    /** 숫자 변환 실패 시 0 반환 */
    private static int optInt(JSONObject obj, String key) {
        try {
            return Integer.parseInt(obj.get(key).toString());
        } catch (Exception e) {
            return 0;
        }
    }
}
