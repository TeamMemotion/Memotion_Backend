package com.hanium.memotion.domain;

import com.hanium.memotion.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.awt.*;
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

    @Column(columnDefinition = "GEOMETRY")
    private Point place;

    @Column
    private String emotion;

    @Column
    private String keyWord;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @LastModifiedDate
    @Column
    private LocalDateTime updatedDate;
    @Column
    private boolean share;
    @ManyToOne
    @JoinColumn(name = "member_Id")
    private Member memberId;
}
