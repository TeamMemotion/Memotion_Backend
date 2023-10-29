package com.hanium.memotion.dto.search.response;

import com.hanium.memotion.domain.diary.Diary;
import lombok.Data;

@Data
public class SearchResDto {
    private Long diaryId;
    private String keyWord;
    private Double latitude;
    private Double longitude;
    private String place;
    private boolean share;
    private String emotion;
    private String createdDate;

    public SearchResDto (Diary d) {
        this.diaryId = d.getDiaryId();
        this.keyWord = d.getKeyWord();
        this.latitude = d.getLatitude();
        this.longitude = d.getLongitude();
        this.place = d.getPlace();
        this.share = d.isShare();
        this.emotion = d.getEmotion();
        this.createdDate = d.getCreatedDate();
    }
}
