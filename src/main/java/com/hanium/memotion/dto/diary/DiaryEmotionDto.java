package com.hanium.memotion.dto.diary;

import com.hanium.memotion.domain.diary.Diary;
import com.hanium.memotion.domain.diary.DiaryContent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiaryEmotionDto {

    private String keyword;
    private String createdDate;
    private Long memberId;
    @Builder
    public DiaryEmotionDto(DiaryContent diary) {
        this.keyword = diary.getKeyWord();
        this.createdDate = diary.getCreatedDate();
        this.memberId = diary.getMemberId().getId();
    }


}

