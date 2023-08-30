package com.hanium.memotion.dto.diary;

import com.hanium.memotion.domain.diary.Diary;
import com.hanium.memotion.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Date;
@Builder
public class DiaryDto {
    @NoArgsConstructor
    @Data
    public static class Request{
        private Double longitude;

        private Double latitude;

        private String emotion;

        private String keyWord;

        private boolean share;

        private Long memberId;

        public Diary toEntity(Member memberId) {
            return Diary.builder()
                    .latitude(latitude)
                    .longitude(longitude)
                    .emotion(emotion)
                    .keyWord(keyWord)
                    .share(share)
                    .memberId(memberId)
                    .build();
        }

        public Request(Diary diary) {
            this.latitude= diary.getLatitude();
            this.longitude= diary.getLongitude();
            this.emotion = diary.getEmotion();
            this.keyWord = diary.getKeyWord();
            this.share = diary.isShare();
            //this.memberId = diary.getMemberId();
        }
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Response{

        private Long diaryId;
        private Double longitude;

        private Double latitude;
        private String emotion;
        private String keyWord;
        private Date createdDate;
        private LocalDateTime updatedDate;
        private boolean share;
        private Long memberId;

        @Builder
        public Response(Diary diary) {
            this.diaryId = diary.getDiaryId();
            this.latitude= diary.getLatitude();
            this.longitude= diary.getLongitude();
            this.emotion = diary.getEmotion();
            this.keyWord = diary.getKeyWord();
            this.createdDate = diary.getCreatedDate();
            this.updatedDate = diary.getUpdatedDate();
            this.share = diary.isShare();
            this.memberId = diary.getMemberId().getId();
        }

    }
}
