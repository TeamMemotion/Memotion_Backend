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
        private Long diaryContentId;
        private String title;

        private String content;

        private Long memberId;

        public DiaryContent toEntity(Member memberId) {
            return DiaryContent.builder()
                    .diaryContentId(diaryContentId)
                    .title(title)
                    .content(content)
                    .memberId(memberId)
                    .build();
        }

        public Request(DiaryContent diarycontent) {
            this.diaryContentId=diarycontent.getDiaryContentId();
            this.title= diarycontent.getTitle();
            this.content = diarycontent.getContent();
            this.memberId = diarycontent.getMemberId().getId();
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
