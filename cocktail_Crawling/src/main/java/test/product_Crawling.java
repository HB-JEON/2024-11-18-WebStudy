package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.sist.product.*;

public class product_Crawling {
    public static void main(String[] args) {
        productDAO dao = productDAO.newInstance();

        for (int i = 1; i <= 3; i++) { // ✅ type_color=1,2,3 (레드, 화이트, 로제)
            int page = 1;
            boolean hasNextPage = true;

            while (hasNextPage) {
                try {
                    // ✅ type_color=i를 적용하여 URL 생성
                    String url = "http://www.kajawine.kr/shop/list.php?ca_id=10&type_color=" + i + "&it_opt4=&it_opt9=&it_price=&page=" + page;
                    
                    // ✅ 디버깅용: 현재 크롤링 중인 URL 출력
                    System.out.println("현재 크롤링 중인 URL: " + url);

                    Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                        .get();

                    // ✅ 상품 리스트 크롤링
                    Elements nameElements = doc.select("div.sct_txt a.sct_a"); // 상품명
                    Elements posterElements = doc.select("div.listImg img"); // ✅ 상품 이미지
                    Elements priceElements = doc.select("div.sct_cost"); // 가격

                    if (nameElements.isEmpty()) {
                        // ✅ 더 이상 상품이 없으면 종료
                        hasNextPage = false;
                        break;
                    }

                    for (int j = 0; j < nameElements.size(); j++) {
                        String name = nameElements.get(j).text().trim();
                        String poster = posterElements.get(j).attr("src").trim();

                        // ✅ 포스터 URL이 상대 경로인 경우 절대 URL로 변환
                        if (!poster.startsWith("http")) {
                            poster = "http://www.kajawine.kr" + poster;
                        }

                        // ✅ 상세 페이지 접속
                        String detailUrl = nameElements.get(j).attr("href").trim();
                        Document detailDoc = Jsoup.connect(detailUrl)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                            .get();

                        // ✅ 필요한 데이터 크롤링
                        Element typeElement = detailDoc.selectFirst("td:contains(종류) + td");
                        String type = (typeElement != null) ? typeElement.text().trim() : " ";

                        Element alcElement = detailDoc.selectFirst("td:contains(알콜도수) + td");
                        String alc = (alcElement != null) ? alcElement.text().trim() : " ";

                        Element volumnElement = detailDoc.selectFirst("td:contains(용량) + td");
                        String volumn = (volumnElement != null) ? volumnElement.text().trim() : " ";

                        Element locElement = detailDoc.selectFirst("td:contains(원산지) + td");
                        String loc = (locElement != null) ? locElement.text().trim() : " ";

                        Element sugarElement = detailDoc.selectFirst("td:contains(당도) + td");
                        String sugar = (sugarElement != null) ? sugarElement.text().trim() : " ";

                        Element bodyElement = detailDoc.selectFirst("td:contains(바디) + td");
                        String body = (bodyElement != null) ? bodyElement.text().trim() : " ";

                        // ✅ 가격 크롤링 (할인 후 가격만 가져오기)
                        String price = " "; // 기본값
                        Element priceElement = priceElements.get(j).selectFirst("strike");

                        if (priceElement != null) { // ✅ 할인 전 가격이 있는 경우
                            priceElements.get(j).select("strike").remove(); // ✅ 취소선 가격 삭제
                        }

                        price = priceElements.get(j).text().trim(); // ✅ 할인 후 가격만 저장

                        // ✅ 상세 설명 크롤링
                        Element contentElement = detailDoc.selectFirst("#goods_extit_cont01");
                        String content = (contentElement != null) ? contentElement.text().trim() : " ";

                        // ✅ productVO 객체에 데이터 저장
                        productVO vo = new productVO();
                        vo.setCno(i); // ✅ type_color=1(레드), 2(화이트), 3(로제) 적용
                        vo.setName(name);
                        vo.setPoster(poster);
                        vo.setType(type);
                        vo.setAlc(alc);
                        vo.setVolumn(volumn);
                        vo.setLoc(loc);
                        vo.setSugar(sugar);
                        vo.setBody(body);
                        vo.setPrice(price);
                        vo.setContent(content);

                        // ✅ DB 저장
                        dao.productInsert(vo);

                        // ✅ 디버깅 출력
                        System.out.println("페이지: " + page);
                        System.out.println("상품명: " + name);
                        System.out.println("포스터: " + poster);
                        System.out.println("종류: " + type);
                        System.out.println("알콜도수: " + alc);
                        System.out.println("용량: " + volumn);
                        System.out.println("원산지: " + loc);
                        System.out.println("당도: " + sugar);
                        System.out.println("바디: " + body);
                        System.out.println("가격: " + price);
                        System.out.println("상세 내용: " + content);
                        System.out.println("========================");
                    }

                    page++; // ✅ 다음 페이지로 이동
                } catch (Exception ex) {
                    ex.printStackTrace();
                    break;
                }
            }
        }
    }
}