package com.hanium.memotion.dto.diary;

import com.hanium.memotion.domain.diary.DiaryContent;
import com.hanium.memotion.domain.member.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

public class DiaryContentDto {
    @NoArgsConstructor
    @Data
    public static class Request{

        private String title;

        private String content;

        private Member memberId;

        public DiaryContent toEntity(Member memberId) {
            return DiaryContent.builder()
                    .title(title)
                    .content(content)
                    .memberId(memberId)
                    .build();
        }

        public Request(DiaryContent diarycontent) {
            this.title= diarycontent.getTitle();
            this.content = diarycontent.getContent();
            this.memberId = diarycontent.getMemberId();
        }
    }

    @NoArgsConstructor
    @Data
    public static class Response{

        private Long diaryContentId;

        private Date createdDate;

        private String title;

        private String content;

        private Member memberId;

        public Response(DiaryContent diarycontent) {
            this.diaryContentId = diarycontent.getDiaryContentId();
            this.createdDate= diarycontent.getCreatedDate();
            this.title=diarycontent.getTitle();
            this.content=diarycontent.getContent();
            this.memberId = diarycontent.getMemberId();
        }
    }
}
