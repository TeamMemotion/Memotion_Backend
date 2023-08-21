package com.hanium.memotion.domain;

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
@Table(name="DiaryContent")
@EntityListeners(AuditingEntityListener.class)
public class DiaryContent {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "diary_content_id")
    private Long diaryContentId;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column
    private String title;
    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_Id")
    private Member memberId;

}
