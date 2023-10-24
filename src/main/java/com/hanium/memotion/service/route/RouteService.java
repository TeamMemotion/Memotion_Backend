package com.hanium.memotion.service.route;

import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.domain.route.Route;
import com.hanium.memotion.domain.route.RouteLike;
import com.hanium.memotion.dto.route.request.RouteReqDto;
import com.hanium.memotion.dto.route.response.LocalGuideResDto;
import com.hanium.memotion.dto.route.response.RouteResDto;
import com.hanium.memotion.exception.base.BaseException;
import com.hanium.memotion.exception.base.ErrorCode;
import com.hanium.memotion.repository.RouteLikeRepository;
import com.hanium.memotion.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;
    private final RouteLikeRepository routeLikeRepository;

    public List<LocalGuideResDto> getLocalGuideList(Member member) {
        List<Route> routeList = routeRepository.findAllByOrderByCreatedAtDesc();
        if(routeList.isEmpty())
            throw new BaseException(ErrorCode.EMPTY_ROUTE);

        return routeList.stream()
                .map(r -> {
                    boolean isLiked = false;
                    Optional<RouteLike> getRouteLike = routeLikeRepository.findByRouteAndMember(r, member);
                    Long likeCount = routeLikeRepository.countByRoute(r);

                    if(!getRouteLike.isEmpty())
                        isLiked = true;

                    return new LocalGuideResDto(r, isLiked, likeCount);
                })
                .collect(Collectors.toList());
    }

    // 인기 지역으로 로컬 가이드 조회
    public List<LocalGuideResDto> getLocalGuideListByPopularRegion(Member member, Double latitude, Double longitude) {
        List<Route> routeList = routeRepository.findAllByPopularRegion(latitude, longitude);
        if(routeList.isEmpty())
            throw new BaseException(ErrorCode.EMPTY_ROUTE);

        return routeList.stream()
                .map(r -> {
                    boolean isLiked = false;
                    Optional<RouteLike> getRouteLike = routeLikeRepository.findByRouteAndMember(r, member);
                    Long likeCount = routeLikeRepository.countByRoute(r);

                    if(!getRouteLike.isEmpty())
                        isLiked = true;

                    return new LocalGuideResDto(r, isLiked, likeCount);
                })
                .collect(Collectors.toList());
    }

    // 검색어로 로컬 가이드 조회 + 정렬 : 인기순
    public List<LocalGuideResDto> getLocalGuideListByRegion(Member member, Double latitude, Double longitude) {
        List<Route> routeList = routeRepository.findAllByRegion(latitude, longitude);
        if(routeList.isEmpty())
            throw new BaseException(ErrorCode.EMPTY_ROUTE);

        return routeList.stream()
                .map(r -> {
                    boolean isLiked = false;
                    Optional<RouteLike> getRouteLike = routeLikeRepository.findByRouteAndMember(r, member);
                    Long likeCount = routeLikeRepository.countByRoute(r);

                    if(!getRouteLike.isEmpty())
                        isLiked = true;

                    return new LocalGuideResDto(r, isLiked, likeCount);
                })
                .collect(Collectors.toList());
    }

    public List<RouteResDto> getRouteList(Member member) {
        List<Route> routeList = routeRepository.findAllByMemberOrderByCreatedAtDesc(member);
        if(routeList.isEmpty())
            throw new BaseException(ErrorCode.EMPTY_ROUTE);

        return routeList.stream()
                .map(r -> {
                    boolean isLiked = false;
                    Optional<RouteLike> getRouteLike = routeLikeRepository.findByRouteAndMember(r, member);
                    Long likeCount = routeLikeRepository.countByRoute(r);

                    if(!getRouteLike.isEmpty())
                        isLiked = true;

                    return new RouteResDto(r, isLiked, likeCount);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Long postRoute(Member member, RouteReqDto routeReqDto) {
        Route route = Route.builder()
                        .name(routeReqDto.getName())
                        .startDate(routeReqDto.getStartDate())
                        .endDate(routeReqDto.getEndDate())
                        .member(member)
                        .build();

        return routeRepository.save(route).getRouteId();
    }

    public Long findByRoute(Long id){
        Route route=routeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 루트 기록이 존재하지 않습니다. id=" + id));
        return route.getMember().getId();
    }
}
