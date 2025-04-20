package hotel.parsing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.sql.Statement;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class hotelParsing {

    // 공공포털 서비스 키 (본인의 키로 교체)
    private static final String SERVICE_KEY = "ISunMAfs3B9igj01dGnHJUtaa5gD1SQWneL2zpo5TIQGkuLFPSlB+cXAm3x2lYmUtwaElqtUlvUmkpPRKsRpDw==";
    private static final String BASE_URL     = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1";

    // JSON 숫자 파싱 헬퍼
    private static int toInt(Object o) {
        if (o instanceof Number) return ((Number) o).intValue();
        if (o instanceof String) return Integer.parseInt((String) o);
        throw new IllegalArgumentException("Cannot convert to int: " + o);
    }

    // 파싱할 지역 및 소분류 매핑
    private static final Map<Integer, String[]> AREA_CAT3_MAP = new HashMap<>();
    static {
        AREA_CAT3_MAP.put(1,  new String[]{"B02010100"});                  // 서울 호텔
        AREA_CAT3_MAP.put(39, new String[]{"B02010100", "B02010700"});     // 제주 호텔+리조트
    }

    // 지역별·시군구별 평균 가격 매핑
    private static final Map<Integer, Map<Integer, Integer>> PRICE_BY_AREA = new HashMap<>();
    static {
        Map<Integer, Integer> seoul = new HashMap<>();
        seoul.put(1, 201672); seoul.put(2, 110000); seoul.put(3,  86000); seoul.put(4, 114522);
        seoul.put(5,  61179); seoul.put(6,  94477); seoul.put(7,  74589); seoul.put(8,  70694);
        seoul.put(9,  69000); seoul.put(10, 67000); seoul.put(11, 88000); seoul.put(12, 80000);
        seoul.put(13,110000); seoul.put(14, 90000); seoul.put(15,130000); seoul.put(16, 95000);
        seoul.put(17, 78000); seoul.put(18,125000); seoul.put(19, 82000); seoul.put(20,112000);
        seoul.put(21,120000); seoul.put(22, 70000); seoul.put(23,115000); seoul.put(24,120000);
        seoul.put(25, 72000);
        PRICE_BY_AREA.put(1, seoul);

        Map<Integer, Integer> jeju = new HashMap<>();
        jeju.put(3,  92000);
        jeju.put(4, 118000);
        PRICE_BY_AREA.put(39, jeju);
    }

    // 대표 이미지 기본값 목록
    private static final String[] DEFAULT_REP_IMAGES = {
        "http://tong.visitkorea.or.kr/cms/resource/67/2475067_image2_1.jpg",
        "http://tong.visitkorea.or.kr/cms/resource/42/1378142_image2_1.jpg",
        "http://tong.visitkorea.or.kr/cms/resource/15/3475015_image2_1.jpg",
        "http://tong.visitkorea.or.kr/cms/resource/70/1867970_image2_1.jpg",
        "http://tong.visitkorea.or.kr/cms/resource/56/2790856_image2_1.jpg",
        "http://tong.visitkorea.or.kr/cms/resource/88/3073488_image2_1.jpg"
    };

    // 객실 이미지 기본값 목록
    private static final String[] DEFAULT_ROOM_IMAGES = {
        "http://tong.visitkorea.or.kr/cms/resource/64/2548464_image2_1.jpg",
        "http://tong.visitkorea.or.kr/cms/resource/67/2548467_image2_1.jpg",
        "http://tong.visitkorea.or.kr/cms/resource/06/2992006_image2_1.jpg",
        "http://tong.visitkorea.or.kr/cms/resource/20/2707720_image2_1.jpg",
        "http://tong.visitkorea.or.kr/cms/resource/07/3303807_image2_1.jpg"
    };

    private static final Random RANDOM = new Random();
    private static PreparedStatement seqPs;  // 시퀀스 재사용용 PreparedStatement

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:oracle:thin:@211.238.142.124:1521:XE";
        String username = "hr_1";
        String password = "happy";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
            conn.setAutoCommit(false);

            // 시퀀스 NEXTVAL 전용 PreparedStatement 준비
            seqPs = conn.prepareStatement("SELECT hotel_img_img_no_seq.NEXTVAL FROM DUAL");

            for (Map.Entry<Integer, String[]> entry : AREA_CAT3_MAP.entrySet()) {
                int areaCode = entry.getKey();
                for (String cat3 : entry.getValue()) {
                    System.out.println("[DEBUG] Parsing areaCode=" + areaCode + ", cat3=" + cat3);

                    JSONArray items = fetchList(areaCode, cat3);
                    System.out.println("[DEBUG] Fetched items count=" + items.size());

                    for (Object o : items) {
                        JSONObject item = (JSONObject) o;
                        int contentId   = toInt(item.get("contentid"));
                        int sigunguCode = toInt(item.get("sigungucode"));
                        int contentTypeId = toInt(item.get("contenttypeid"));  // ★ 여기서 꺼내서
                        System.out.println("[DEBUG] contentId=" + contentId + ", sigunguCode=" + sigunguCode);

                        // 1) overview 가져오기
                        JSONObject common = fetchDetailCommon(contentId);
                        String overview = common.containsKey("overview") && !((String)common.get("overview")).isEmpty()
                            ? (String) common.get("overview")
                            : (String) item.get("overview");
                        System.out.println("[DEBUG] overview=" + overview);

//                         2) 호텔 기본 정보 INSERT
                        int hotelNo = insertHotel(conn, item, overview);
                        System.out.println("[DEBUG] Inserted hotelNo=" + hotelNo);

                        // 3) 소개정보 조회 및 삽입
                        JSONObject intro = fetchDetailIntro(contentId);
                        insertHotelIntro(conn, hotelNo, intro);
                        System.out.println("[DEBUG] Inserted intro for hotelNo=" + hotelNo);

                        // 4) 기본 객실 삽입
                        insertDefaultRoom(conn, hotelNo, contentId, areaCode, sigunguCode);
                        System.out.println("[DEBUG] Inserted room for hotelNo=" + hotelNo);

                        // 5) 상세 이미지 조회 및 삽입
                        JSONArray images = fetchDetailImages(contentId, contentTypeId);
                        insertHotelImages(conn, hotelNo, images);
                        System.out.println("[DEBUG] Inserted " + images.size()
                            + " images for hotelNo=" + hotelNo);
                    }
                }
            }

            // 시퀀스 전용 PreparedStatement close
            seqPs.close();

            conn.commit();
            System.out.println("[INFO] Parsing and insert complete");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JSONArray fetchList(int areaCode, String cat3) throws Exception {
        String url = BASE_URL
                   + "?serviceKey=" + java.net.URLEncoder.encode(SERVICE_KEY, "UTF-8")
                   + "&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=TEST&_type=json"
                   + "&contentTypeId=32&areaCode=" + areaCode
                   + "&cat1=B02&cat2=B0201&cat3=" + cat3;
        System.out.println("[DEBUG] fetchList URL=" + url);
        JSONObject resp = callApi(url);
        JSONObject body = (JSONObject)((JSONObject)resp.get("response")).get("body");
        JSONObject items= (JSONObject) body.get("items");
        return (JSONArray) items.get("item");
    }

    private static JSONObject fetchDetailCommon(int contentId) throws Exception {
        String url = "https://apis.data.go.kr/B551011/KorService1/detailCommon1"
                   + "?serviceKey=" + java.net.URLEncoder.encode(SERVICE_KEY, "UTF-8")
                   + "&MobileOS=ETC&MobileApp=TEST&_type=json"
                   + "&contentTypeId=32&contentId=" + contentId
                   + "&defaultYN=Y&firstImageYN=N&overviewYN=Y";
        System.out.println("[DEBUG] fetchDetailCommon URL=" + url);
        JSONObject resp = callApi(url);
        JSONObject body = (JSONObject)((JSONObject)resp.get("response")).get("body");
        JSONArray arr   = (JSONArray)((JSONObject)body.get("items")).get("item");
        return (JSONObject) arr.get(0);
    }

    private static JSONObject fetchDetailIntro(int contentId) throws Exception {
        String url = "https://apis.data.go.kr/B551011/KorService1/detailIntro1"
                   + "?serviceKey=" + java.net.URLEncoder.encode(SERVICE_KEY, "UTF-8")
                   + "&MobileOS=ETC&MobileApp=TEST&_type=json"
                   + "&contentTypeId=32&contentId=" + contentId;
        System.out.println("[DEBUG] fetchDetailIntro URL=" + url);
        JSONObject resp = callApi(url);
        JSONObject body = (JSONObject)((JSONObject)resp.get("response")).get("body");
        JSONArray arr   = (JSONArray)((JSONObject)body.get("items")).get("item");
        return (JSONObject) arr.get(0);
    }

    private static JSONArray fetchDetailImages(int contentId, int contentTypeId) throws Exception {
        String url = "https://apis.data.go.kr/B551011/KorService1/detailImage1?"
                   + "serviceKey="  + URLEncoder.encode(SERVICE_KEY, "UTF-8")
                   + "&MobileOS=ETC"
                   + "&MobileApp=TEST"
                   + "&_type=json"
                   + "&contentId="   + contentId
                   + "&contentTypeId=" + contentTypeId
                   + "&numOfRows=10";

        System.out.println("[DEBUG] fetchDetailImages URL=" + url);
        JSONObject root = callApi(url);
        if (root == null || !root.containsKey("response")) {
            return new JSONArray();
        }

        JSONObject body = (JSONObject) ((JSONObject) root.get("response")).get("body");
        JSONObject itemsWrapper = (JSONObject) body.get("items");
        if (itemsWrapper == null || !itemsWrapper.containsKey("item")) {
            return new JSONArray();
        }
        Object itemsObj = itemsWrapper.get("item");

        // 1) JSONArray, 2) 단일 객체/String 모두 List로 정규화
        List<Object> elems = new ArrayList<>();
        if (itemsObj instanceof JSONArray) {
            elems.addAll((JSONArray) itemsObj);
        } else if (itemsObj != null) {
            elems.add(itemsObj);
        }

        JSONArray result = new JSONArray();
        for (Object elem : elems) {
            JSONObject jo = new JSONObject();
            if (elem instanceof JSONObject) {
                // 원래 속성 모두 복사
                jo.putAll((JSONObject) elem);
            } else {
                // 문자열 등 기타 타입일 때 serialnum만 설정
                jo.put("serialnum", elem.toString());
            }
            // 공통 필드로 contentId, contentTypeId 추가
            jo.put("contentid", contentId);
            jo.put("contenttypeid", contentTypeId);

            result.add(jo);
        }

        System.out.println("[DEBUG] detailImage1 returned " + result.size() + " records");
        return result;
    }

    private static JSONObject callApi(String urlStr) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestMethod("GET");
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) sb.append(line);
                String resp = sb.toString().trim();
                System.out.println("[DEBUG] API response preview=" + resp.substring(0, Math.min(resp.length(), 100)));
                if (resp.startsWith("<")) {
                    System.out.println("[WARN] Not JSON response for URL: " + urlStr);
                    return new JSONObject();
                }
                return (JSONObject) new JSONParser().parse(resp);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] callApi failed: " + e.getMessage());
            return new JSONObject();
        }
    }
    
    private static int nextSeq(Connection conn, String seqName) throws SQLException {
        String sql = "SELECT " + seqName + ".NEXTVAL FROM DUAL";
        try (
            Statement stmt = conn.createStatement();
            ResultSet rs   = stmt.executeQuery(sql)
        ) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("시퀀스 NEXTVAL을 가져오지 못했습니다: " + seqName);
            }
        }
    }

    private static int insertHotel(Connection conn, JSONObject item, String overview) throws SQLException {
        int hotelNo = nextSeq(conn, "hotel_no_seq");
        String img  = (String) item.get("firstimage");
        if (img == null || img.isEmpty()) img = DEFAULT_REP_IMAGES[RANDOM.nextInt(DEFAULT_REP_IMAGES.length)];

        String sql = "INSERT INTO hotel(no, content_id, content_type, title, addr, areacode, sigungucode, img, cat1, cat2, cat3, overview) "
                   + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt   (1, hotelNo);
            ps.setInt   (2, toInt(item.get("contentid")));
            ps.setInt   (3, toInt(item.get("contenttypeid")));
            ps.setString(4, (String)item.get("title"));
            ps.setString(5, (String)item.get("addr1"));
            ps.setInt   (6, toInt(item.get("areacode")));
            ps.setInt   (7, toInt(item.get("sigungucode")));
            ps.setString(8, img);
            ps.setString(9, (String)item.get("cat1"));
            ps.setString(10,(String)item.get("cat2"));
            ps.setString(11,(String)item.get("cat3"));
            ps.setString(12, overview);
            ps.executeUpdate();
        }
        return hotelNo;
    }

    private static void insertHotelIntro(Connection conn, int hotelNo, JSONObject intro) throws SQLException {
        String sql = "INSERT INTO hotel_intro(hotel_no, content_id, contenttypeid, roomcount, roomtype, checkintime, checkouttime," +
                     " chkcooking, food, pickup, info_tel, parking, scale, room_totalcount, seminar, sports, sauna, beauty, beverage," +
                     " karaoke, barbecue, campfire, bicycle, fitness, publicpc, publicbath, subfacility)" +
                     " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt   (1, hotelNo);
            ps.setInt   (2, toInt(intro.get("contentid")));
            ps.setInt   (3, toInt(intro.get("contenttypeid")));
            ps.setString(4, (String)intro.get("roomcount"));
            ps.setString(5, (String)intro.get("roomtype"));
            ps.setString(6, (String)intro.get("checkintime"));
            ps.setString(7, (String)intro.get("checkouttime"));
            ps.setString(8, (String)intro.get("chkcooking"));
            ps.setString(9, (String)intro.get("food"));
            ps.setString(10,(String)intro.get("pickup"));
            ps.setString(11,(String)intro.get("infotext"));
            ps.setString(12,(String)intro.get("parking"));
            ps.setString(13,(String)intro.get("scale"));
            ps.setString(14,(String)intro.get("room_totalcount"));
            for (int i = 15; i <= 26; i++) ps.setInt(i, 0);
            ps.setString(27, null);
            ps.executeUpdate();
        }
    }

    private static void insertDefaultRoom(Connection conn, int hotelNo, int contentId, int areaCode, int sigunguCode) throws SQLException {
        int roomNo = nextSeq(conn, "hotel_room_room_no_seq");
        int price  = PRICE_BY_AREA.getOrDefault(areaCode, Collections.emptyMap())
                                  .getOrDefault(sigunguCode, 0);

        String roomImg      = DEFAULT_ROOM_IMAGES[RANDOM.nextInt(DEFAULT_ROOM_IMAGES.length)];
        String roomImgsJson = "[\"" + roomImg + "\"]";

        String sql = "INSERT INTO hotel_room(room_no, hotel_no, room_id, content_id, room_code, title," +
                     " room_size, room_count, person, person_max, price, room_imgs) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt   (1, roomNo);
            ps.setInt   (2, hotelNo);
            ps.setInt   (3, 1);
            ps.setInt   (4, contentId);
            ps.setString(5, "DEFAULT_CODE");
            ps.setString(6, "기본 객실");
            ps.setInt   (7, 0); ps.setInt(8, 0);
            ps.setInt   (9, 0); ps.setInt(10,0);
            ps.setInt   (11, price);
            ps.setString(12, roomImgsJson);
            ps.executeUpdate();
        }
    }

    // 시퀀스 NEXTVAL 전용 헬퍼 (PreparedStatement 재사용)
    private static int nextHotelImgSeq() throws SQLException {
        try (ResultSet rs = seqPs.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
            else throw new SQLException("hotel_img_img_no_seq NEXTVAL을 가져오지 못했습니다");
        }
    }
    
    private static void insertHotelImages(Connection conn, int hotelNo, JSONArray images) throws SQLException {
        String sql = "INSERT INTO hotel_img(img_no, hotel_no, content_id, serial_num, serial_order, image_url, image_alt) "
                   + "VALUES(?,?,?,?,?,?,?)";

        for (Object o : images) {
            JSONObject imgObj = (JSONObject) o;
            int imgNo      = nextHotelImgSeq();
            String serial  = (String) imgObj.get("serialnum");
            int  contentId = toInt(imgObj.get("contentid"));

            // URL과 ALT는 사용하지 않습니다.
            String imageUrl = "";
            String imageAlt = "";

            System.out.println("[DEBUG] Inserting image #" + imgNo +
                               ", contentid=" + contentId +
                               ", serialnum=" + serial);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt   (1, imgNo);
                ps.setInt   (2, hotelNo);
                ps.setInt   (3, contentId);
                ps.setString(4, serial);
                ps.setString(5, serial);
                ps.setString(6, imageUrl);
                ps.setString(7, imageAlt);
                ps.executeUpdate();
            }
        }
    }
}
