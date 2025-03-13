package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.sist.product.*;

public class product_Crawiling_2 {

    public static void main(String[] args) {
        productDAO dao=productDAO.newInstance();
        
        try
        {
        	int maxPage=2;
        	
        	for(int i=1;i<=maxPage;i++)
        	{
        		System.out.println("현재 크롤링 중: "+i+"/"+maxPage);
        		
        		String url="https://www.winenara.com/shop/product/product_lists?sh_category1_cd=10000&sh_category2_cd=10100&sh_category3_cd=10103&sh_order_by=1&sh_sort_order_by=&sh_filter_code=&sh_rcd=&page="+i;
        		Document doc=Jsoup.connect(url).get();
        		Elements productList=doc.select("li > div.item");
        		for(Element product:productList)
        		{
        			// 상세 url
        			Element link=product.selectFirst("div.more_info p.prd_name a");
        			String href=(link!=null)?link.attr("href").trim():"";
        			String linkUrl=href.startsWith("http")?href:"https://www.winenara.com"+href;
        			
        			// 상품명 (올바르게 가져오기)
                    String name = (link != null) ? link.text().trim() : "";
                    
                    // 상세 페이지 크롤링
                    Document detailDoc=Jsoup.connect(linkUrl).get();
        			
        			
        			// poster
        			Element posterElement = product.selectFirst("div.main_img img");
                    String poster=(posterElement!=null)?posterElement.attr("src").trim() : "";
                    if (!poster.startsWith("http")) {
                        poster = "https://www.winenara.com" + poster;
                    }
                    // price
                    Element price = product.selectFirst("p.price ins");
                    // type
                    String type="스파클링";
                    
                 // content 추출 부분 (상세 페이지 Document detailDoc 기준)
                    String content = "";
                    // 우선 iframe이 있는지 확인
                    Element productIframe = detailDoc.selectFirst("iframe#productIframe");
                    if(productIframe != null) {
                        content = productIframe.attr("src").trim();
                    } else {
                        // iframe이 없으면, div.tab_con.detail_con.on 내부의 img 태그에서 src를 가져옴
                        Element contentDiv = detailDoc.selectFirst("div.tab_con.detail_con.on");
                        if(contentDiv != null) {
                             Element img = contentDiv.selectFirst("img");
                             if(img != null) {
                                  String src = img.attr("src").trim();
                                  // src가 상대 URL이면 절대 URL로 변환 (도메인: https://www.winenara.com)
                                  if(!src.startsWith("http")) {
                                      src = "https://www.winenara.com" + src;
                                  }
                                  content = src;
                             }
                        }
                    }
                    // DB 저장
                    productVO vo=new productVO();
                    vo.setCno(4); //  1 레드 / 2 화이트 / 3 로제 / 4 스파클링
                    vo.setName(name);
                    vo.setPoster(poster);
                    vo.setType(type);
                    vo.setPrice(price.text());
                    vo.setContent(content);
                    dao.productInsert(vo);
                    
                    // 4. 출력
                    System.out.println("상품명 : " + name);
                    System.out.println("포스터 : " + poster);
                    System.out.println("가격 : " + (price != null ? price.text() : "가격 정보 없음"));
                    System.out.println("타입 : " + type);
                    System.out.println("전체 내용 : " + content);
        		}
        	}
        }catch(Exception ex)
        {
        	ex.printStackTrace();
        }
    }
}
