package tour.parsing;

import lombok.Data;

@Data
public class TagConnectVO {
	private int tourNo;
    private int tagNo;

    // 확장: 연관 객체도 넣을 수 있음 (선택)
    private TourVO tour;
    private TagVO tag;
}
