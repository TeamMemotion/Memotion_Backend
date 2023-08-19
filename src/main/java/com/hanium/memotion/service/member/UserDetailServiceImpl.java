package com.hanium.memotion.service.member;

import com.hanium.memotion.exception.custom.BadRequestException;

import com.hanium.memotion.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.hanium.memotion.exception.base.ErrorCode.MEMBER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        System.out.println("로그인한 memberId : " + memberId);
        return (UserDetails) memberRepository.findById(Long.parseLong(memberId))
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));
    }
}
