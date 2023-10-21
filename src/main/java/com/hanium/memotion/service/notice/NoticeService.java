package com.hanium.memotion.service.notice;

import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.domain.notice.Notice;
import com.hanium.memotion.dto.notice.request.NoticeReqDto;
import com.hanium.memotion.dto.notice.response.NoticeResDto;
import com.hanium.memotion.exception.custom.BadRequestException;
import com.hanium.memotion.repository.MemberRepository;
import com.hanium.memotion.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    // 관리자 계정 체크
    public boolean checkManager(Member member) {
        return member.getStatus().equals("manager");
    }

    @Transactional
    public Long postNotice(NoticeReqDto.PostRequest noticeReqDto) {
        Notice notice = Notice.builder()
                .name(noticeReqDto.getName())
                .content(noticeReqDto.getContent())
                .build();

        return noticeRepository.save(notice).getNoticeId();
    }

    @Transactional
    public String patchNotice(NoticeReqDto.PatchRequest noticeReqDto) {
        Optional<Notice> getNotice = noticeRepository.findById(noticeReqDto.getNoticeId());
        if(getNotice.isEmpty())
            throw new BadRequestException("해당되는 공지사항을 찾을 수 없습니다.");

        Notice notice = getNotice.get();
        notice.updateNotice(noticeReqDto.getName(), noticeReqDto.getContent());
        return "공지사항 수정 성공";
    }

    @Transactional
    public String deleteNotice(Long noticeId) {
        Optional<Notice> getNotice = noticeRepository.findById(noticeId);
        if(getNotice.isEmpty())
            throw new BadRequestException("해당되는 공지사항을 찾을 수 없습니다.");

        noticeRepository.deleteById(noticeId);
        return "공지사항 삭제 성공";
    }

    public NoticeResDto getNotice(Long noticeId) {
        Optional<Notice> getNotice = noticeRepository.findById(noticeId);
        if(getNotice.isEmpty())
            throw new BadRequestException("해당되는 공지사항을 찾을 수 없습니다.");

        return new NoticeResDto(getNotice.get());
    }

    public List<NoticeResDto> getAllNotice() {
        List<Notice> getNotice = noticeRepository.findAllByOrderByCreatedAtDesc();
        if(getNotice.isEmpty())
            throw new BadRequestException("해당되는 공지사항을 찾을 수 없습니다.");

        return getNotice.stream()
                .map(n -> new NoticeResDto(n))
                .collect(Collectors.toList());
    }
}
