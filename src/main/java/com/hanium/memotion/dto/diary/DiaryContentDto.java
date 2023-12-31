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
        private String createdDate;
        private String keyword;
        private Long memberId;

        public DiaryContent toEntity(Member memberId) {
            return DiaryContent.builder()
                    .title(title)
                    .content(content)
                    .createdDate(createdDate)
                    .keyWord(keyword)
                    .memberId(memberId)
                    .build();
        }

        public Request(DiaryContent diarycontent) {
            this.title= diarycontent.getTitle();
            this.content = diarycontent.getContent();
            this.createdDate = diarycontent.getCreatedDate();
            this.keyword=diarycontent.getKeyWord();
            this.memberId = diarycontent.getMemberId().getId();
        }
    }

    @NoArgsConstructor
    @Data
    public static class Response{
        private Long diaryContentId;
        private String createdDate;
        private String title;
        private String content;
        private String keyWord;
        private Long memberId;

        public Response(DiaryContent diarycontent, Long member) {
            this.diaryContentId = diarycontent.getDiaryContentId();
            this.createdDate= diarycontent.getCreatedDate();
            this.title=diarycontent.getTitle();
            this.content=diarycontent.getContent();
            this.keyWord=diarycontent.getKeyWord();
            this.memberId = member;
        }
    }
}
