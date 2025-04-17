package tour.parsing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;

public class DebugTest {
    private static final boolean DEBUG = true;

    public static void main(String[] args) {
        if (DEBUG) {
            // 디버그 모드: 원본 JSON만 확인
            debugFetchRaw(1, 1);  // 서울 지역, 첫 페이지
        } else {
            // 실제 파싱 + DB 저장 로직 호출
            // parseAndSaveAllAreas();
        }
    }

    /**
     * 지정된 지역 코드와 페이지 번호로 API 호출 후
     * 원본 JSON 문자열을 콘솔에 출력합니다.
     */
    private static void debugFetchRaw(int areaCode, int pageNo) {
        String listUrl = buildListUrl(areaCode, pageNo);
        System.out.println("[DEBUG_RAW] GET " + listUrl);
        try (BufferedReader br = new BufferedReader(
                 new InputStreamReader(new URL(listUrl).openStream()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            System.out.println("[DEBUG_RAW] JSON:\n" + sb.toString());
        } catch (Exception e) {
            System.err.println("[DEBUG_RAW] Fetch error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * API 호출을 위한 URL을 조립해 반환합니다.
     */
    private static String buildListUrl(int areaCode, int pageNo) {
        return "";

    }
}
