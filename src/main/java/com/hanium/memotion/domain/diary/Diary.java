package com.hanium.memotion.domain.diary;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanium.memotion.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name="Diary")
@EntityListeners(AuditingEntityListener.class)
public class Diary {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long diaryId;

    @Column
    private Double longitude;
    @Column
    private Double latitude;
    @Column
    private String emotion;

    @Column
    private String keyWord;

    @Column
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    //@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="Asia/Seoul")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedDate;
    @Column
    private boolean share;
    @ManyToOne
    @JoinColumn(name = "member_Id")
    private Member memberId;

    public void update(Double latitude, Double longitude, String emotion, String keyWord, boolean share){
        this.latitude=latitude;
        this.longitude=longitude;
        this.emotion=emotion;
        this.keyWord=keyWord;
        this.share=share;
    }
}
