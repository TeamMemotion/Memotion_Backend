package com.hanium.memotion.dto.diary;

import com.hanium.memotion.domain.diary.Diary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiaryEmotionDto {

    private String emotion;
    private String createdDate;
    private Long memberId;
    @Builder
    public DiaryEmotionDto(Diary diary) {
        this.emotion = diary.getEmotion();
        this.createdDate = diary.getCreatedDate();
        this.memberId = diary.getMemberId().getId();
    }


}

