package com.hanium.memotion.dto.diary;

import lombok.Builder;
import lombok.Data;

@Data
public class ARResDto {
    private String keyword;
    private Long count;

    @Builder
    public ARResDto(String keyword, Long count) {
        this.keyword = keyword;
        this.count = count;
    }
}
