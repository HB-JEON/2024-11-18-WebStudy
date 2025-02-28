package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.sist.product.*;

public class product_Crawling {
    public static void main(String[] args) {
        String[] url = {
            "http://www.kajawine.kr/shop/list.php?ca_id=10&type_color=1&it_opt4=&it_opt9=&it_price=",
            "http://www.kajawine.kr/shop/list.php?ca_id=10&type_color=2&it_opt4=&it_opt9=&it_price=",
            "http://www.kajawine.kr/shop/list.php?ca_id=10&type_color=3&it_opt4=&it_opt9=&it_price="
        };

        productDAO dao = productDAO.newInstance();

        for (int i = 0; i < url.length; i++) {
            try {
                Document doc = Jsoup.connect(url[i]).get();

                // ✅ 상품명 크롤링
                Elements nameElements = doc.select("div.sct_txt a.sct_a");
                // ✅ 상품 상세 페이지 URL 크롤링
                Elements posterElements = doc.select("div.listImg a.sct_a");
                // ✅ 가격 크롤링
                Elements priceElements = doc.select("div.sct_cost"); 
                

                for (int j = 0; j < nameElements.size(); j++) {
                    String name = nameElements.get(j).text().trim();
                    String poster = posterElements.get(j).attr("href").trim();

                    // ✅ 상세 페이지 접속
                    Document detailDoc = Jsoup.connect(poster).get();

                    // ✅ 정보 가져오기
                    Element typeElement = detailDoc.selectFirst("td:contains(종류) + td");
                    String type = (typeElement != null) ? typeElement.text().trim() : "정보 없음";

                    Element alcElement = detailDoc.selectFirst("td:contains(알콜도수) + td");
                    String alc = (alcElement != null) ? alcElement.text().trim() : "정보 없음"; // ✅ alc를 String으로 저장

                    Element volumnElement = detailDoc.selectFirst("td:contains(용량) + td");
                    String volumn = (volumnElement != null) ? volumnElement.text().trim() : "정보 없음"; // ✅ volumn을 String으로 저장

                    Element locElement = detailDoc.selectFirst("td:contains(원산지) + td");
                    String loc = (locElement != null) ? locElement.text().trim() : "정보 없음";

                    Element sugarElement = detailDoc.selectFirst("td:contains(당도) + td");
                    String sugar = (sugarElement != null) ? sugarElement.text().trim() : "정보 없음";

                    Element bodyElement = detailDoc.selectFirst("td:contains(바디) + td");
                    String body = (bodyElement != null) ? bodyElement.text().trim() : "정보 없음";

                    // ✅ 가격 크롤링 및 변환 (String으로 변경)
                    String price = "정보 없음"; // 기본값 설정
                    if (j < priceElements.size()) {
                        Element strikeElement = priceElements.get(j).selectFirst("strike"); // ✅ 취소선 가격 (할인 전 가격) 선택
                        if (strikeElement != null) {
                            // ✅ 취소선 태그가 있는 경우, 할인된 가격만 가져옴
                            price = priceElements.get(j).ownText().replaceAll("[^0-9]", "").trim() + "원";
                        } else {
                            // ✅ 할인 전 가격이 없으면 현재 가격 그대로 저장
                            price = priceElements.get(j).text().trim();
                        }
                    }
                 // ✅ 상품 상세 정보 (content) 가져오기
                    String content = "";
                    Element detailSection = detailDoc.selectFirst("div#goods_extit_cont01"); // ✅ 상품상세정보 div 가져오기
                    StringBuilder contentBuilder = new StringBuilder();

                    if (detailSection != null) {
                        Elements tbodyElements = detailSection.select("tbody"); // ✅ `goods_extit_cont01` 내부의 `tbody` 선택

                        for (Element tbody : tbodyElements) {
                            Elements rows = tbody.select("tr"); // ✅ 각 행 (tr) 가져오기

                            for (Element row : rows) {
                                Elements columns = row.select("td"); // ✅ 각 행의 td 요소 가져오기

                                for (Element column : columns) {
                                    String text = column.text().trim().replaceAll("(\u00A0|&nbsp;)", " "); // ✅ 공백 정리
                                    if (!text.isEmpty()) {
                                        contentBuilder.append(text).append("\n"); // ✅ 줄바꿈 추가하여 저장
                                    }
                                }
                            }
                        }
                    }

                    // ✅ content 최종 값 정리
                    content = contentBuilder.toString().trim(); 

                    // ✅ content가 비어 있다면 기본값 설정
                    if (content.isEmpty()) {
                        content = "상품 상세 정보 없음";
                    }
                    // ✅ productVO 객체 생성 후 데이터 저장
                    productVO vo = new productVO();
                    vo.setCno(1);
                    vo.setName(name);
                    vo.setPoster(poster);
                    vo.setType(type);
                    vo.setAlc(alc);  // ✅ alc를 String으로 저장
                    vo.setVolumn(volumn); // ✅ volumn을 String으로 저장
                    vo.setLoc(loc);
                    vo.setSugar(sugar);
                    vo.setBody(body);
                    vo.setPrice(price); // ✅ price를 String으로 저장
                    vo.setContent(content);
                    
                    // ✅ DB에 저장
                    dao.productInsert(vo);

                    // ✅ 콘솔 출력
//                    System.out.println(name);
                    System.out.println("상품명 : " + name);
                    System.out.println("포스터 URL : " + poster);
                    System.out.println("종류 : " + type);
                    System.out.println("알콜도수 : " + alc);
                    System.out.println("용량 : " + volumn);
                    System.out.println("원산지 : " + loc);
                    System.out.println("당도 : " + sugar);
                    System.out.println("바디 : " + body);
                    System.out.println("가격 : " + price);
                    System.out.println("상세 내용 : " + content); // ✅ content와 tbody 내용 합쳐 저장
                    System.out.println("========================");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}