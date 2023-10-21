package com.hanium.memotion.dto.notice.response;

import com.hanium.memotion.domain.notice.Notice;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeResDto {
    private Long noticeId;
    private String name;
    private String content;
    private LocalDateTime createdAt;

    @Builder
    public NoticeResDto(Notice n) {
        this.noticeId = n.getNoticeId();
        this.name = n.getName();
        this.content = n.getContent();
        this.createdAt = n.getCreatedAt();
    }
}
