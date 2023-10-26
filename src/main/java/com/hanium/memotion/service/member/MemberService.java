package com.hanium.memotion.service.member;

import com.hanium.memotion.domain.member.Member;

import com.hanium.memotion.domain.member.Provider;
import com.hanium.memotion.dto.member.TokenDto;
import com.hanium.memotion.dto.member.request.KakaoLoginDto;
import com.hanium.memotion.dto.member.request.LoginReqDto;
import com.hanium.memotion.dto.member.request.SignupReqDto;
import com.hanium.memotion.dto.member.response.LoginResDto;
import com.hanium.memotion.dto.member.response.ProfileResDto;
import com.hanium.memotion.dto.member.response.SignupResDto;
import com.hanium.memotion.exception.base.BaseException;
import com.hanium.memotion.exception.base.BaseResponse;
import com.hanium.memotion.exception.base.ErrorCode;
import com.hanium.memotion.exception.custom.BadRequestException;
import com.hanium.memotion.exception.custom.InvalidTokenException;

import com.hanium.memotion.exception.custom.UnauthorizedException;
import com.hanium.memotion.repository.MemberRepository;
import com.hanium.memotion.security.JwtProvider;
import com.hanium.memotion.service.global.AWSS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

import static com.hanium.memotion.exception.base.ErrorCode.ALREADY_LOGOUT;
import static com.hanium.memotion.exception.base.ErrorCode.MEMBER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AWSS3Service awss3Service;
    private final JwtProvider jwtService;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public SignupResDto signup(MultipartFile multipartFile, SignupReqDto signupReqDto) throws IOException {
        if(!checkEmail(signupReqDto.getEmail()))
            throw new BaseException(ErrorCode.DUPLICATED_EMAIL);

        String imageUrl = null;

        if(multipartFile != null || !multipartFile.isEmpty())
            imageUrl = awss3Service.uploadFile(multipartFile);

        Member newMember = Member.builder()
                .email(signupReqDto.getEmail())
                .username(signupReqDto.getUsername())
                .password(passwordEncoder.encode(signupReqDto.getPassword()))
                .image(imageUrl)
                .build();

        return new SignupResDto(memberRepository.save(newMember));
    }

    // 이메일 중복 검사
    public boolean checkEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isPresent())
            return false;
        return true;
    }

    // 로그인
    @Transactional
    public LoginResDto login(LoginReqDto loginReqDto) {
        Optional<Member> getMember = memberRepository.findByEmail(loginReqDto.getEmail());

        if(getMember.isEmpty())
            throw new BadRequestException("잘못된 이메일 입니다.");

        Member member = getMember.get();

        // 탈퇴한 회원이 재로그인 하는 경우 -> 프론트에서 처리할 화면 아직 안만들어서 일단 에러 처리
        if(member.getStatus().equals("inactive"))
            throw new BaseException(ErrorCode.INVALID_USER_JWT);

        if(!passwordEncoder.matches(loginReqDto.getPassword(), member.getPassword()))
            throw new BadRequestException("잘못된 비밀번호 입니다.");

        return generateAllToken(member);
    }

    // 액세스 토큰 재발급
    @Transactional
    public LoginResDto regenerateAccessToken(String refreshToken) {
        Long memberId = jwtService.getMemberIdFromJwtToken(refreshToken);
        log.info("memberId : " + memberId);

        Optional<Member> getMember = memberRepository.findById(memberId);
        if(getMember.isEmpty())
            throw new BaseException(MEMBER_NOT_FOUND);

        Member member = getMember.get();
        if(!refreshToken.equals(member.getRefreshToken()))
            throw new InvalidTokenException("유효하지 않은 Refresh Token입니다");

        String newRefreshToken = jwtService.encodeJwtRefreshToken(memberId);
        String newAccessToken = jwtService.encodeJwtToken(new TokenDto(memberId));

        member.renewRefreshToken(newRefreshToken);
        memberRepository.save(member);

        return new LoginResDto(newAccessToken, newRefreshToken);
    }

    // 로그아웃
    @Transactional
    public String logout(String accessToken) {
        if(!jwtService.validateToken(accessToken))
            throw new UnauthorizedException("유효하지 않거나 만료된 토큰입니다.");

        Long memberId = jwtService.getMemberIdFromJwtToken(accessToken);
        Optional<Member> getMember = memberRepository.findById(memberId);
        if(getMember.isEmpty())
            throw new BaseException(MEMBER_NOT_FOUND);

        Member member = getMember.get();
        if(member.getRefreshToken().equals(""))
            throw new BaseException(ALREADY_LOGOUT);

        member.refreshTokenExpires();
        memberRepository.save(member);

        return "로그아웃 성공";
    }

    // 프로필 조회
    public ProfileResDto getProfile(Long memberId) {
        Optional<Member> getMember = memberRepository.findById(memberId);
        if(getMember.isEmpty())
            throw new BaseException(MEMBER_NOT_FOUND);

        Member member = getMember.get();
        return new ProfileResDto(member);
    }

    public Long getMemberId(String username) {
        return getMemberByUsername(username).getId();
    }

    private Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Member username=" + username));
    }

    // 회원탈퇴
    @Transactional
    public String withdraw(Member member) {
        if(!member.getStatus().equals("active"))
            throw new BaseException(ErrorCode.INVALID_USER_JWT);

        member.updateStatus("inactive");
        return "회원탈퇴 성공";
    }

    // 프로필 수정
    @Transactional
    public String patchProfile(Member member, String fileName, String password) {
        member.updateImage(fileName);
        if(password != null)
            member.updatePassword(passwordEncoder.encode(password));
        return "프로필 수정 성공";
    }

    // 카카오 로그인
    @Transactional
    public LoginResDto kakaoLogin(KakaoLoginDto kakaoLoginDto) {
        String email = kakaoLoginDto.getEmail();

        Optional<Member> getMember = memberRepository.findByEmail(email);
        Member member;

        if(getMember.isPresent()) {
            member = getMember.get();
            if(member.getStatus().equals("status") && member.getType().equals(Provider.KAKAO))
                return generateAllToken(member);
            else
                throw new BaseException(MEMBER_NOT_FOUND);
        } else {
            member = memberRepository.save(new Member(email, kakaoLoginDto.getUsername(), kakaoLoginDto.getImage()));
            return generateAllToken(member);
        }
    }

    // 로그인 후 토큰 발급
    @Transactional
    public LoginResDto generateAllToken(Member member) {
        // accessToken, refreshToken 발급
        String newAccessToken = jwtService.encodeJwtToken(new TokenDto(member.getId()));
        String newRefreshToken = jwtService.encodeJwtRefreshToken(member.getId());

        member.renewRefreshToken(newRefreshToken);     // DB에 refreshToken 저장
        memberRepository.save(member);
        return new LoginResDto(newAccessToken, newRefreshToken);
    }
}
