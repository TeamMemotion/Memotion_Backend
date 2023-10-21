package com.hanium.memotion.domain.diary;

import com.hanium.memotion.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.awt.*;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name="diary_content")
@EntityListeners(AuditingEntityListener.class)
public class DiaryContent {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "diary_content_id")
    private Long diaryContentId;

    @Column
    private String createdDate;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private String keyWord;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member memberId;

    public void update(Long diaryContentId, String createdDate, String title,String content, String keyWord, Long MemberId){
        this.diaryContentId=diaryContentId;
        this.createdDate=createdDate;
        this.title=title;
        this.content=content;
        this.keyWord=keyWord;
        this.memberId=memberId;
    }


}
