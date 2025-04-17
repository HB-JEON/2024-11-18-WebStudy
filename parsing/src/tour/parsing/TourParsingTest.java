package tour.parsing;

import java.sql.*;
import java.util.*;
import java.net.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class TourParsingTest {
    // 태그 매핑 정보
    private static final Map<String, String[]> TAG_MAP = new HashMap<>();
    static {
        TAG_MAP.put("A01010100", new String[]{"국립공원", "공원", "자연", "산책"});
        TAG_MAP.put("A01010200", new String[]{"도립공원", "공원", "자연", "산책"});
        TAG_MAP.put("A01010400", new String[]{"산", "등산", "자연", "풍경", "오름", "명소"});
        TAG_MAP.put("A01010500", new String[]{"공원", "휴식", "자연", "산책"});
        TAG_MAP.put("A01010700", new String[]{"식물원", "공원", "자연", "수목원", "휴양림", "포토존", "인생샷"});
        TAG_MAP.put("A01010800", new String[]{"폭포", "자연", "명소"});
        TAG_MAP.put("A01010900", new String[]{"계곡", "물놀이", "자연", "피서"});
        TAG_MAP.put("A01011100", new String[]{"해안절경", "해안", "바다산책"});
        TAG_MAP.put("A01011200", new String[]{"해수욕장", "해변", "해안", "바다산책", "인생샷", "명소", "자연", "바다"});
        TAG_MAP.put("A01011300", new String[]{"섬", "풍경", "자연"});
        TAG_MAP.put("A01011900", new String[]{"굴", "천연동굴", "동굴"});
        // 추가 카테고리용 태그 매핑 A02
        TAG_MAP.put("A02010100", new String[]{"한옥", "문화", "역사", "궁", "성"});
        TAG_MAP.put("A02010300", new String[]{"문", "문화", "역사"});
        TAG_MAP.put("A02010400", new String[]{"가옥", "문화", "역사", "전통가옥", "한옥"});
        TAG_MAP.put("A02010600", new String[]{"한옥", "문화", "역사", "가옥", "한옥마을", "포토존"});
        TAG_MAP.put("A02010800", new String[]{"절", "문화", "역사", "불교", "종교"});
        TAG_MAP.put("A02010900", new String[]{"교회", "문화", "역사", "성당", "종교", "교회"});
        TAG_MAP.put("A02050100", new String[]{"대교", "다리", "야경"});
        TAG_MAP.put("A02050200", new String[]{"기념비", "추모비", "기념탑"});
        TAG_MAP.put("A02050400", new String[]{"동상"});
    }

    // 파싱할 카테고리 목록: cat1 -> cat2 목록
    private static final List<Category> CATEGORIES = List.of(
        new Category("A01", new String[]{"A0101"}),
        new Category("A02", new String[]{"A0201", "A0205"})
    );

    public static void main(String[] args) {
        int[] areaCodes = {1, 39}; // 서울, 제주
        boolean debug = Arrays.asList(args).contains("debug");

        String url       = "jdbc:oracle:thin:@211.238.142.124:1521:XE";
        String user      = "hr_1";
        String pass      = "happy";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn.setAutoCommit(false);

            Map<String, Integer> tagCache = loadExistingTags(conn);

            String insertTourSQL =
                "INSERT INTO tour (no, content_id, content_type, title, addr, areacode, sigungucode, img, cat1, cat2, cat3, mapx, mapy, overview) " +
                "VALUES (tour_no_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertTourPs = conn.prepareStatement(insertTourSQL, new String[]{"no"});
                 PreparedStatement selectTagPs   = conn.prepareStatement("SELECT no FROM tag WHERE tag_name = ?");
                 PreparedStatement insertTagPs   = conn.prepareStatement(
                     "INSERT INTO tag (no, tag_name, keyword_synonyms) VALUES (tag_no_seq.nextval, ?, ?)");
                 PreparedStatement mergeConnectPs = conn.prepareStatement(
                     "MERGE INTO tag_connect tc USING DUAL " +
                     "ON (tc.tour_no = ? AND tc.tag_no = ?) " +
                     "WHEN NOT MATCHED THEN INSERT (tour_no, tag_no, areacode) VALUES (?, ?, ?)")
            ) {
                for (int areaCode : areaCodes) {
                    for (Category cat : CATEGORIES) {
                        for (String cat2 : cat.subCats) {
                            int pageNo = 1, totalCount;
                            do {
                                String listUrl = buildListUrl(areaCode, pageNo, cat.type, cat2);
                                JSONArray items = fetchItems(listUrl);
                                totalCount = items.size();

                                for (Object o : items) {
                                    JSONObject item = (JSONObject) o;
                                    int contentId      = Integer.parseInt(item.get("contentid").toString());
                                    int contentTypeId  = Integer.parseInt(item.get("contenttypeid").toString());
                                    String title       = (String) item.get("title");
                                    String addr        = (String) item.get("addr1");
                                    double mapx        = Double.parseDouble(item.getOrDefault("mapx","0").toString());
                                    double mapy        = Double.parseDouble(item.getOrDefault("mapy","0").toString());
                                    String img         = (String) item.get("firstimage");
                                    String cat1        = cat.type;
                                    String catLevel2   = cat2;
                                    String cat3        = (String) item.get("cat3");

                                    System.out.println("⛳ [" + contentId + "] " + title);
                                    System.out.println("주소: " + addr);
                                    System.out.println("대표이미지: " + ((img != null && !img.isEmpty()) ? "✅ 있음" : "❌ 없음"));
                                    String overview = getOverview(contentId, contentTypeId);
                                    System.out.println("개요 길이: " + (overview != null ? overview.length() : 0));
                                    System.out.println("지역 코드: " + areaCode + ", 시군구 코드: " + item.get("sigungucode"));
                                    System.out.println("카테고리 1: " + cat1 + ", 2: " + catLevel2 + ", 3: " + cat3);

                                    if (img == null || img.isEmpty()) {
                                        img = getFirstImage(contentId);
                                    }
                                    if (overview.isEmpty() || img.isEmpty()) {
                                        continue;
                                    }

                                    if (debug) {
                                        System.out.println("[DEBUG] Skipping DB insert for contentId=" + contentId);
                                    } else {
                                        // tour 삽입
                                        insertTourPs.setInt(1, contentId);
                                        insertTourPs.setInt(2, contentTypeId);
                                        insertTourPs.setString(3, title);
                                        insertTourPs.setString(4, addr);
                                        insertTourPs.setInt(5, areaCode);
                                        insertTourPs.setInt(6, Integer.parseInt(item.get("sigungucode").toString()));
                                        insertTourPs.setString(7, img);
                                        insertTourPs.setString(8, cat1);
                                        insertTourPs.setString(9, catLevel2);
                                        insertTourPs.setString(10, cat3);
                                        insertTourPs.setDouble(11, mapx);
                                        insertTourPs.setDouble(12, mapy);
                                        insertTourPs.setString(13, overview);
                                        insertTourPs.executeUpdate();

                                        int tourNo;
                                        try (ResultSet rs = insertTourPs.getGeneratedKeys()) {
                                            rs.next();
                                            tourNo = rs.getInt(1);
                                        }

                                        // 태그 연결: 첫 요소를 tag_name, 나머지를 keyword_synonyms
                                        String[] tagList = TAG_MAP.getOrDefault(cat3, new String[0]);
                                        if (tagList.length > 0) {
                                            String mainTag = tagList[0];
                                            String syns = (tagList.length > 1)
                                                ? String.join(",", Arrays.copyOfRange(tagList, 1, tagList.length))
                                                : "";

                                            int tagId = tagCache.computeIfAbsent(mainTag, k -> {
                                                try {
                                                    insertTagPs.setString(1, k);
                                                    insertTagPs.setString(2, syns);
                                                    insertTagPs.executeUpdate();
                                                    selectTagPs.setString(1, k);
                                                    try (ResultSet r = selectTagPs.executeQuery()) {
                                                        r.next();
                                                        return r.getInt(1);
                                                    }
                                                } catch (Exception e) {
                                                    throw new RuntimeException(e);
                                                }
                                            });

                                            // MERGE into tag_connect
                                            mergeConnectPs.setInt(1, tourNo);
                                            mergeConnectPs.setInt(2, tagId);
                                            mergeConnectPs.setInt(3, tourNo);
                                            mergeConnectPs.setInt(4, tagId);
                                            mergeConnectPs.setInt(5, areaCode);
                                            mergeConnectPs.executeUpdate();
                                        }
                                    }
                                }

                                pageNo++;
                            } while (pageNo <= (totalCount + 299) / 300);
                        }
                    }
                }

                conn.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class Category {
        String type;
        String[] subCats;
        Category(String t, String[] s) { type = t; subCats = s; }
    }

    private static Map<String, Integer> loadExistingTags(Connection conn) throws SQLException {
        Map<String, Integer> cache = new HashMap<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT no, tag_name FROM tag")) {
            while (rs.next()) {
                cache.put(rs.getString("tag_name"), rs.getInt("no"));
            }
        }
        return cache;
    }

    private static JSONArray fetchItems(String listUrl) throws IOException, ParseException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new URL(listUrl).openStream()))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
        }

        JSONParser p = new JSONParser();
        Object parsed = p.parse(sb.toString());
        if (!(parsed instanceof JSONObject)) return new JSONArray();

        JSONObject body = (JSONObject)((JSONObject)((JSONObject)parsed).get("response")).get("body");
        Object its = ((JSONObject)body.get("items")).get("item");
        JSONArray arr = new JSONArray();
        if (its instanceof JSONArray)      return (JSONArray) its;
        if (its instanceof JSONObject)     arr.add(its);
        return arr;
    }

    private static String buildListUrl(int area, int page, String cat1, String cat2) {
        return "https://apis.data.go.kr/B551011/KorService1/areaBasedSyncList1"
             + "?numOfRows=300&pageNo=" + page
             + "&MobileOS=ETC&MobileApp=test&_type=json"
             + "&contentTypeId=12&cat1=" + cat1
             + "&cat2=" + cat2
             + "&areaCode=" + area
             + "&serviceKey=ISunMAfs3B9igj01dGnHJUtaa5gD1SQWneL2zpo5TIQGkuLFPSlB%2BcXAm3x2lYmUtwaElqtUlvUmkpPRKsRpDw%3D%3D";
    }

    private static String getOverview(int contentId, int contentTypeId) {
        try {
            StringBuilder sb = new StringBuilder();
            String url = "https://apis.data.go.kr/B551011/KorService1/detailCommon1"
                       + "?MobileOS=ETC&MobileApp=test&_type=json&defaultYN=N&overviewYN=Y"
                       + "&contentId=" + contentId + "&contentTypeId=" + contentTypeId
                       + "&serviceKey=ISunMAfs3B9igj01dGnHJUtaa5gD1SQWneL2zpo5TIQGkuLFPSlB%2BcXAm3x2lYmUtwaElqtUlvUmkpPRKsRpDw%3D%3D";
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
            }

            JSONParser parser = new JSONParser();
            Object parsed = parser.parse(sb.toString());
            if (!(parsed instanceof JSONObject)) return "";

            JSONObject response = (JSONObject)((JSONObject)parsed).get("response");
            JSONObject body     = response != null ? (JSONObject)response.get("body") : null;
            if (body == null) return "";

            Object itemsObj = ((JSONObject)body.get("items")).get("item");
            if (itemsObj instanceof JSONObject) {
                Object overview = ((JSONObject)itemsObj).get("overview");
                return overview != null ? overview.toString() : "";
            }
            if (itemsObj instanceof JSONArray) {
                for (Object elem : (JSONArray)itemsObj) {
                    if (elem instanceof JSONObject) {
                        Object overview = ((JSONObject)elem).get("overview");
                        if (overview != null) return overview.toString();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[overview 오류] contentId=" + contentId + " → " + e.getMessage());
        }
        return "";
    }

    private static String getFirstImage(int contentId) {
        try {
            StringBuilder sb = new StringBuilder();
            String url = "https://apis.data.go.kr/B551011/KorService1/detailImage1"
                       + "?MobileOS=ETC&MobileApp=test&_type=json&imageYN=Y&subImageYN=N"
                       + "&contentId=" + contentId
                       + "&serviceKey=ISunMAfs3B9igj01dGnHJUtaa5gD1SQWneL2zpo5TIQGkuLFPSlB%2BcXAm3x2lYmUtwaElqtUlvUmkpPRKsRpDw%3D%3D";
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
            }

            JSONParser parser = new JSONParser();
            Object parsed = parser.parse(sb.toString());
            if (!(parsed instanceof JSONObject)) return "";

            JSONObject response = (JSONObject)((JSONObject)parsed).get("response");
            JSONObject body     = response != null ? (JSONObject)response.get("body") : null;
            if (body == null) return "";

            Object itemsObj = ((JSONObject)body.get("items")).get("item");
            if (itemsObj instanceof JSONObject) {
                Object originUrl = ((JSONObject)itemsObj).get("originimgurl");
                return originUrl != null ? originUrl.toString() : "";
            }
            if (itemsObj instanceof JSONArray) {
                for (Object elem : (JSONArray)itemsObj) {
                    if (elem instanceof JSONObject) {
                        Object originUrl = ((JSONObject)elem).get("originimgurl");
                        if (originUrl != null) return originUrl.toString();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[이미지 오류] contentId=" + contentId + " → " + e.getMessage());
        }
        return "";
    }
}