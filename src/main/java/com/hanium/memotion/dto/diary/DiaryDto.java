package com.hanium.memotion.dto.diary;

import com.hanium.memotion.domain.diary.Diary;
import com.hanium.memotion.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.awt.*;
import java.time.LocalDate;
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
        private String createdDate;
        private boolean share;

        public Diary toEntity(Member memberId) {
            return Diary.builder()
                    .latitude(latitude)
                    .longitude(longitude)
                    .emotion(emotion)
                    .keyWord(keyWord)
                    .createdDate(createdDate)
                    .share(share)
                    .memberId(memberId)
                    .build();
        }

        public Request(Diary diary) {
            this.latitude= diary.getLatitude();
            this.longitude= diary.getLongitude();
            this.emotion = diary.getEmotion();
            this.keyWord = diary.getKeyWord();
            this.createdDate = diary.getCreatedDate();
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
        private String createdDate;
        private LocalDateTime updatedDate;
        private boolean share;
        private Long memberId;

        @Builder
        public Response(Diary diary,Member member) {
            this.diaryId = diary.getDiaryId();
            this.latitude= diary.getLatitude();
            this.longitude= diary.getLongitude();
            this.emotion = diary.getEmotion();
            this.keyWord = diary.getKeyWord();
            this.createdDate = diary.getCreatedDate();
            this.updatedDate = diary.getUpdatedDate();
            this.share = diary.isShare();
            this.memberId = member.getId();
        }

    }
}
