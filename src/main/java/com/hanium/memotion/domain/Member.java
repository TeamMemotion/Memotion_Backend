package com.hanium.memotion.domain;

import io.jsonwebtoken.lang.Assert;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "member")
public class Member extends BaseTime implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "provider", nullable = false)
    @ColumnDefault("'APP'")
    @Enumerated(EnumType.STRING)
    private Provider type;

    @Column(name = "image", length = 255, nullable = true)
    private String image;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "refresh_token_expires_at", nullable = false)
    private LocalDateTime refreshTokenExpiresAt;

    @Builder
    public Member(String email, String username, String password, String phone, String image) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.image = image;
        this.refreshToken = "";
        this.refreshTokenExpiresAt = LocalDateTime.now();
    }

    // refreshToken 재발급
    public void renewRefreshToken(String refreshToken) {
        Assert.notNull(Member.this.refreshToken, "refreshToken 은 null 일 수 없습니다.");
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresAt = LocalDateTime.now();
    }

    // 로그아웃 시 토큰 만료
    public void refreshTokenExpires() {
        this.refreshToken = "";
        this.refreshTokenExpiresAt = LocalDateTime.now();
    }

    // 비밀번호 수정
    public void updatePassword(String password) {
        this.password = password;
    }


    /* implements UserDetails 시 구현해야하는 메소드 */
    // UserDetails = 사용자 정보를 담는 인터페이스
    
    // 계정 권한 목록 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    // 계정의 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return false; // 만료
    }

    // 계정의 잠김 여부
    @Override
    public boolean isAccountNonLocked() {
        return false;    // 잠김
    }

    // 비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return false;    // 만료
    }

    // 계정 활성화 여부
    @Override
    public boolean isEnabled() {
        return false;    // 비활성화
    }
}
