package com.hanium.memotion.controller.notice;

import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.dto.notice.request.NoticeReqDto;
import com.hanium.memotion.dto.notice.response.NoticeResDto;
import com.hanium.memotion.exception.base.BaseResponse;
import com.hanium.memotion.exception.custom.UnauthorizedException;
import com.hanium.memotion.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;

    // 공지사항 생성
    @PostMapping("")
    public BaseResponse<Long> postNotice(@AuthenticationPrincipal Member member, @RequestBody @Valid NoticeReqDto.PostRequest noticeReqDto) {
        // 관리자 계정 체크
        if(!noticeService.checkManager(member))
            throw new UnauthorizedException("유효하지 않거나 만료된 토큰입니다.");

        return BaseResponse.onSuccess(noticeService.postNotice(noticeReqDto));
    }

    // 공지사항 수정
    @PatchMapping("")
    public BaseResponse<String> patchNotice(@AuthenticationPrincipal Member member, @RequestBody @Valid NoticeReqDto.PatchRequest noticeReqDto) {
        // 관리자 계정 체크
        if(!noticeService.checkManager(member))
            throw new UnauthorizedException("유효하지 않거나 만료된 토큰입니다.");

        return BaseResponse.onSuccess(noticeService.patchNotice(noticeReqDto));
    }

    // 공지사항 삭제
    @DeleteMapping("")
    public BaseResponse<String> deleteNotice(@AuthenticationPrincipal Member member, @RequestParam Long noticeId) {
        // 관리자 계정 체크
        if(!noticeService.checkManager(member))
            throw new UnauthorizedException("유효하지 않거나 만료된 토큰입니다.");

        return BaseResponse.onSuccess(noticeService.deleteNotice(noticeId));
    }

    // 공지사항 상세 조회
    @GetMapping("/")
    public BaseResponse<NoticeResDto> getNotice(@PathVariable Long noticeId) {
        return BaseResponse.onSuccess(noticeService.getNotice(noticeId));
    }

    // 공지사항 전체 조회
    @GetMapping("")
    public BaseResponse<List<NoticeResDto>> getAllNotice() {
        return BaseResponse.onSuccess(noticeService.getAllNotice());
    }
}
