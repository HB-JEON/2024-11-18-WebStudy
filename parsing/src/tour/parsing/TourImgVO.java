package tour.parsing;

import lombok.Data;

@Data
public class TourImgVO {
	private int tourNo;
    private String imgOg;     // 원본 이미지
    private String imgName;   // 이미지 이름
    private int serialnum;
}
