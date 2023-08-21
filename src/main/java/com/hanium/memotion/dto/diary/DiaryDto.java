package com.hanium.memotion.dto.diary;

import com.hanium.memotion.domain.Diary;
import com.hanium.memotion.domain.member.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Date;

public class DiaryDto {
    @NoArgsConstructor
    @Data
    public static class Request{
        private Point place;

        private String emotion;

        private String keyWord;

        private boolean share;

        private Member memberId;

        public Diary toEntity(Member memberId) {
            return Diary.builder()
                    .place(place)
                    .emotion(emotion)
                    .keyWord(keyWord)
                    .share(share)
                    .memberId(memberId)
                    .build();
        }

        public Request(Diary diary) {
            this.place= diary.getPlace();
            this.emotion = diary.getEmotion();
            this.keyWord = diary.getKeyWord();
            this.share = diary.isShare();
            this.memberId = diary.getMemberId();
        }
    }

    @NoArgsConstructor
    @Data
    public static class Response{

        private Long diaryId;
        private Point place;
        private String emotion;
        private String keyWord;
        private Date createdDate;
        private LocalDateTime updatedDate;
        private boolean share;
        private Member memberId;

        public Response(Diary diary) {
            this.diaryId = diary.getDiaryId();
            this.place= diary.getPlace();
            this.emotion = diary.getEmotion();
            this.keyWord = diary.getKeyWord();
            this.createdDate = diary.getCreatedDate();
            this.updatedDate = diary.getUpdatedDate();
            this.share = diary.isShare();
            this.memberId = diary.getMemberId();
        }
    }
}