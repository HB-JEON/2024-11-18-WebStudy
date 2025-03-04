package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.sist.product.*;

public class product_Crawiling {

    public static void main(String[] args) {
        productDAO dao = productDAO.newInstance();

        try {
            int maxPage = 64; // 화이트 와인의 마지막 페이지

            for (int i = 1; i <= maxPage; i++) {
                System.out.println("🔄 현재 크롤링 중: " + i + " / " + maxPage + " 페이지");

                Document doc = Jsoup.connect("http://www.kajawine.kr/shop/list.php?ca_id=10&type_color=1&it_opt4=&it_opt9=&it_price=&page=" + i).get();
                Elements productList = doc.select("li.item_thumb"); // ✅ 상품 리스트에서 아이템 가져오기

                for (Element product : productList) {
                    try {
                        // ✅ 상품명 (리스트 페이지에서 가져오기)
                        Element nameElement = product.selectFirst("div.sct_txt a.sct_a");
                        String name = (nameElement != null) ? nameElement.text().trim() : "상품명 없음";

                        // ✅ 상품 상세 페이지 URL
                        Element linkElement = product.selectFirst("div.sct_txt a.sct_a");
                        String href = (linkElement != null) ? linkElement.attr("href").trim() : "";
                        String url = href.startsWith("http") ? href : "http://www.kajawine.kr" + href;
                        System.out.println("✅ 상세 페이지 크롤링: " + url);

                        // ✅ 포스터 (리스트 페이지에서 가져오기)
                        Element posterElement = product.selectFirst("div.listImg img");
                        String poster = (posterElement != null) ? posterElement.attr("src").trim() : "";

                        // ✅ 할인된 가격 가져오기 (리스트 페이지 기준)
                        Element priceElementList = product.selectFirst("div.sct_cost");
                        String price = "";
                        if (priceElementList != null) {
                            priceElementList.select("strike").remove(); // ✅ 취소선 가격(정가) 삭제
                            price = priceElementList.text().trim(); // ✅ 할인된 가격만 저장
                        }

                        // ✅ 상세 페이지 크롤링 시작
                        Document doc2 = Jsoup.connect(url).get();

                        // ✅ 상세 페이지에서도 할인된 가격 가져오기 (없으면 리스트 페이지 가격 사용)
                        Element priceElementDetail = doc2.selectFirst("td:contains(판매가) + td");
                        if (priceElementDetail != null && price.isEmpty()) {
                            priceElementDetail.select("strike").remove(); // ✅ 상세 페이지에서도 취소선 가격 제거
                            price = priceElementDetail.text().trim();
                        }

                        if (price.isEmpty()) {
                            price = "가격 정보 없음";
                        }

                        // ✅ 종류
                        Element typeElement = doc2.selectFirst("td:contains(종류) + td");
                        String type = (typeElement != null) ? typeElement.text().trim() : " ";

                        // ✅ 알콜도수
                        Element alcElement = doc2.selectFirst("td:contains(알콜도수) + td");
                        String alc = (alcElement != null) ? alcElement.text().trim() : " ";

                        // ✅ 용량
                        Element volumnElement = doc2.selectFirst("td:contains(용량) + td");
                        String volumn = (volumnElement != null) ? volumnElement.text().trim() : " ";

                        // ✅ 원산지
                        Element locElement = doc2.selectFirst("td:contains(원산지) + td");
                        String loc = (locElement != null) ? locElement.text().trim() : " ";

                        // ✅ 당도
                        Element sugarElement = doc2.selectFirst("td:contains(당도) + td");
                        String sugar = (sugarElement != null) ? sugarElement.text().trim() : " ";

                        // ✅ 바디감
                        Element bodyElement = doc2.selectFirst("td:contains(바디) + td");
                        String body = (bodyElement != null) ? bodyElement.text().trim() : " ";

                        // ✅ 상세설명
                        Element contentElement = doc2.selectFirst("#goods_extit_cont01");
                        String content = (contentElement != null) ? contentElement.text().trim() : " ";

                        // ✅ DB 저장
                        productVO vo = new productVO();
                        vo.setCno(1); // 화이트 와인 (cno = 2)
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

                        dao.productInsert(vo);

                        // ✅ 디버깅용 출력
                        System.out.println("✔️ 저장 완료: " + name);
                        System.out.println("🖼️ 포스터 URL: " + poster);
                        System.out.println("💰 할인된 가격: " + price);
                        System.out.println("========================");
                    } catch (Exception ex) {
                        System.out.println("❌ 오류 발생 (페이지 스킵): " + ex.getMessage());
                    }
                }

                System.out.println("✅ 페이지 크롤링 완료: " + i + " / " + maxPage);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("🎉 모든 화이트 와인 크롤링 완료!");
    }
}
