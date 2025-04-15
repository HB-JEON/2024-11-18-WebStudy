package tour.parsing;

import lombok.Data;
import java.util.*;

@Data
public class TourVO {
	private int no;               // 기본키
    private int contentId;        // 고유 콘텐츠 ID
    private int contentType;      // 콘텐츠 타입 ID
    private String title;         // 제목
    private String addr;          // 주소
    private int areacode;         // 지역 코드
    private int sigungucode;        // 시군구 코드
    private String img;           // 대표 이미지 URL
    private String cat1;          // 대분류 코드
    private String cat2;          // 중분류 코드
    private String cat3;          // 소분류 코드
    private double mapx;          // 경도
    private double mapy;          // 위도
    private String overview;      // 소개 내용 (CLOB이지만 Java에서는 String)
    
    // 연관 관계
    private TourIntroVO intro;                        // 1:1
    private List<TourInfoVO> infoList;                // 1:N
    private List<TourImgVO> imgList;                  // 1:N
    private List<TagVO> tagList;                      // N:M
}
