package com.hanium.memotion.dto.notice.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NoticeReqDto {
    @Data
    public static class PostRequest {
        @NotNull
        private String name;

        @NotNull
        private String content;
    }

    @Data
    public static class PatchRequest {
        @NotNull
        private Long noticeId;

        @NotNull
        private String name;

        @NotNull
        private String content;
    }
}
