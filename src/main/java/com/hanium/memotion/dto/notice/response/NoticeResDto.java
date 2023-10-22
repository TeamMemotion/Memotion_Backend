package com.hanium.memotion.dto.notice.response;

import com.hanium.memotion.domain.notice.Notice;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class NoticeResDto {
    private Long noticeId;
    private String name;
    private String content;
    private String createdAt;   // 프론트 요청으로 LocalDateTime -> String으로 변환

    @Builder
    public NoticeResDto(Notice n) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = n.getCreatedAt().format(formatter);

        this.noticeId = n.getNoticeId();
        this.name = n.getName();
        this.content = n.getContent();
        this.createdAt = formattedDate;
    }
}
