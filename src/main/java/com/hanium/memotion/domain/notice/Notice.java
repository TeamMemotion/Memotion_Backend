package com.hanium.memotion.domain.notice;

import com.hanium.memotion.domain.core.BaseTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "Notice")
public class Notice extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;

    @Column(name = "name")
    private String name;

    @Column(name = "content")
    @Lob
    private String content;

    @Builder
    public Notice(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public void updateNotice (String name, String content) {
        this.name = name;
        this.content = content;
    }
}
