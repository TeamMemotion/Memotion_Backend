package com.hanium.memotion.dto.member.response;

import com.hanium.memotion.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileResDto {
    private Long memberId;
    private String email;
    private String username;
    private String image;

    @Builder
    public ProfileResDto(Member member) {
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.username = member.getUsername();
        this.image = member.getImage();
    }
}
