package tour.parsing;

import java.sql.*;
import java.util.HashMap;
import java.net.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class TourParsingTest2 {
    public static void main(String[] args) {
    	int[] areaCodes = {1, 39}; // 1: 서울, 39: 제주

        for (int areaCode : areaCodes) {
            System.out.println("\n===== 📍 지역 코드 " + areaCode + " 파싱 시작 =====");

            String listUrl = "https://apis.data.go.kr/B551011/KorService1/areaBasedSyncList1"
                    + "?numOfRows=10&pageNo=1&MobileOS=etc&MobileApp=test&_type=json"
                    + "&contentTypeId=12&cat1=A01&cat2=A0101"
                    + "&areaCode=" + areaCode
                    + "&serviceKey=ISunMAfs3B9igj01dGnHJUtaa5gD1SQWneL2zpo5TIQGkuLFPSlB%2BcXAm3x2lYmUtwaElqtUlvUmkpPRKsRpDw%3D%3D";

            try {
                URL url = new URL(listUrl);
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = br.readLine()) != null) sb.append(line);

                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(sb.toString());
                JSONObject body = (JSONObject) ((JSONObject) obj.get("response")).get("body");
                JSONArray itemArr = (JSONArray) ((JSONObject) body.get("items")).get("item");

                // DB 연결
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "happy");
                String sql = "INSERT INTO tour VALUES(tour_no_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);

                int count = 0; // ✅ 수집 성공 건수 카운터

                for (Object o : itemArr) {
                    JSONObject item = (JSONObject) o;
                    int contentId = Integer.parseInt(item.get("contentid").toString());
                    int contentTypeId = Integer.parseInt(item.get("contenttypeid").toString());
                    int areacode = Integer.parseInt(item.get("areacode").toString());
                    int sigungu = Integer.parseInt(item.get("sigungucode").toString());

                    double mapx = Double.parseDouble(item.get("mapx").toString());
                    double mapy = Double.parseDouble(item.get("mapy").toString());

                    String title = (String) item.get("title");
                    String addr = (String) item.get("addr1");
                    String img = (String) item.get("firstimage");
                    String cat1 = (String) item.get("cat1");
                    String cat2 = (String) item.get("cat2");
                    String cat3 = (String) item.get("cat3");

                    // overview 가져오기
                    String overview = getOverview(contentId, contentTypeId);
                    // 대표 이미지 없을 경우 대체 이미지 가져오기
                    if (img == null || img.isEmpty()) {
                        img = getFirstImage(contentId);
                    }

                    // 개요 또는 이미지 없으면 저장 제외
                    if (overview == null || overview.trim().isEmpty() || img == null || img.trim().isEmpty()) {
                        System.out.println("(contentId=" + contentId + ") → 개요 또는 이미지 없음");
                        continue;
                    }

                    count++; // ✅ 유효한 항목 카운팅

                    // 디버깅 출력
                    System.out.println("⛳ [" + contentId + "] " + title);
                    System.out.println("주소: " + addr);
                    System.out.println("대표이미지: " + (img != null || img != "" ? "✅ 있음" : "❌ 없음"));
                    System.out.println("개요 길이: " + (overview != null ? overview.length() : 0));
                    System.out.println(areacode);
                    System.out.println(sigungu);
                    System.out.println(cat1);
                    System.out.println(cat2);
                    System.out.println(cat3);

                }

                // ✅ 수집 성공한 건수 출력
                System.out.println("수집된 데이터 : " + count);

                conn.close();
                System.out.println("✅ 지역 코드 " + areaCode + " 데이터 저장 완료");

            } catch (Exception e) {
                System.out.println("❌ 오류 발생 (지역 코드: " + areaCode + ")");
                e.printStackTrace();
            }
        }
    }
    // overview 파싱
    private static String getOverview(int contentId, int contentTypeId) {
        try {
            String url = "https://apis.data.go.kr/B551011/KorService1/detailCommon1"
                + "?MobileOS=ETC&MobileApp=test&_type=json&defaultYN=N&overviewYN=Y"
                + "&contentId=" + contentId + "&contentTypeId=" + contentTypeId
                + "&serviceKey=ISunMAfs3B9igj01dGnHJUtaa5gD1SQWneL2zpo5TIQGkuLFPSlB%2BcXAm3x2lYmUtwaElqtUlvUmkpPRKsRpDw%3D%3D";

            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);

            JSONParser parser = new JSONParser();
            JSONObject body = (JSONObject) ((JSONObject) ((JSONObject) parser.parse(sb.toString())).get("response")).get("body");
            Object items = body.get("items");

            if (!(items instanceof JSONObject)) return "";
            Object item = ((JSONObject) items).get("item");

            if (item instanceof JSONArray)
                return (String) ((JSONObject) ((JSONArray) item).get(0)).get("overview");
            else if (item instanceof JSONObject)
                return (String) ((JSONObject) item).get("overview");
        } catch (Exception e) {
            System.out.println("❌ [overview 오류] contentId=" + contentId + " → " + e.getMessage());
        }
        return "";
    }

    // 이미지 대체 가져오기
    private static String getFirstImage(long contentId) {
        try {
            String url = "https://apis.data.go.kr/B551011/KorService1/detailImage1"
                + "?MobileOS=ETC&MobileApp=test&_type=json&imageYN=Y&subImageYN=N"
                + "&contentId=" + contentId
                + "&serviceKey=ISunMAfs3B9igj01dGnHJUtaa5gD1SQWneL2zpo5TIQGkuLFPSlB%2BcXAm3x2lYmUtwaElqtUlvUmkpPRKsRpDw%3D%3D";

            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);

            JSONParser parser = new JSONParser();
            JSONObject body = (JSONObject) ((JSONObject) ((JSONObject) parser.parse(sb.toString())).get("response")).get("body");
            Object items = body.get("items");

            if (!(items instanceof JSONObject)) return "";
            Object item = ((JSONObject) items).get("item");

            if (item instanceof JSONArray)
                return (String) ((JSONObject) ((JSONArray) item).get(0)).get("originimgurl");
            else if (item instanceof JSONObject)
                return (String) ((JSONObject) item).get("originimgurl");
        } catch (Exception e) {
            System.out.println("❌ [이미지 오류] contentId=" + contentId + " → " + e.getMessage());
        }
        return "";
    }
}
