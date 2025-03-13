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
            int maxPage = 7; 

            for (int i = 1; i <= maxPage; i++) {
                System.out.println("🔄 현재 크롤링 중: " + i + " / " + maxPage + " 페이지");

                Document doc = Jsoup.connect("http://www.kajawine.kr/shop/list.php?ca_id=40&sort=&sortodr=&type_color=&it_price=&it_opt4=&it_opt9=&page=" + i).get();
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

                        // 가격이 비어있거나 0원인 경우 데이터를 받아오지 않도록 처리 (현재 상품을 건너뜁니다)
                        if (price.isEmpty() || price.equals("0원") || price.equals("0 원")) {
                            // 해당 상품은 저장하지 않고 다음 상품으로 넘어감
                            continue;
                        }

                        String type="전통주";
                        
                        // ✅ 알콜도수
                        Element alcElement = doc2.selectFirst("td:contains(알콜도수) + td");
                        String alc = (alcElement != null) ? alcElement.text().trim() : " ";

                        // ✅ 용량
                        Element volumnElement = doc2.selectFirst("td:contains(용량) + td");
                        String volumn = (volumnElement != null) ? volumnElement.text().trim() : " ";

                        // ✅ 원산지
                        Element locElement = doc2.selectFirst("td:contains(원산지) + td");
                        String loc = (locElement != null) ? locElement.text().trim() : " ";

                        // ✅ 상세설명
                        Element contentElement = doc2.selectFirst("#goods_extit_cont01");
                        String content = (contentElement != null) ? contentElement.text().trim() : " ";

                        // ✅ DB 저장
                        productVO vo = new productVO();
                        vo.setCno(18); // 아메리칸 5 / 스카치 6 ... / 브랜디 10 / 리큐르 13 ~ / 민속주 18
                        vo.setName(name);
                        vo.setPoster(poster);
                        vo.setType(type);
                        vo.setAlc(alc);
                        vo.setVolumn(volumn);
                        vo.setLoc(loc);
                        vo.setPrice(price);
                        vo.setContent(content);
                        dao.productInsert(vo);

                        // ✅ 디버깅용 출력
                        System.out.println("✔️ 저장 완료: " + name);
                        System.out.println(type);
                        System.out.println("🖼️ 포스터 : " + poster);
                        System.out.println("💰 가격: " + price);
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
        System.out.println("🎉");
    }
}
