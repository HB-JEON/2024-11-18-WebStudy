package hotel.parsing;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class test1 {
    public static void main(String[] args) {
        // DB나 코드에서 가져온 JSON 배열 문자열 예시
        String roomImgsJson = "[\"http://tong.visitkorea.or.kr/cms/resource/20/2707720_image2_1.jpg\"]";

        try {
            JSONParser parser = new JSONParser();
            JSONArray arr = (JSONArray) parser.parse(roomImgsJson);

            System.out.println("파싱된 배열 크기: " + arr.size());
            for (int i = 0; i < arr.size(); i++) {
                System.out.println("이미지 URL[" + i + "]: " + arr.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
