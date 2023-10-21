package com.hanium.memotion.service.route;

import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.domain.route.Route;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;
    private final RouteLikeRepository routeLikeRepository;

    public List<LocalGuideResDto> getLocalGuideList() {
        List<Route> routeList = routeRepository.findTop8ByOrderByCreatedAtDesc();
        if(routeList.isEmpty())
            throw new BaseException(ErrorCode.EMPTY_ROUTE);

        return routeList.stream()
                .map(r -> new LocalGuideResDto(r))
                .collect(Collectors.toList());
    }

    public List<LocalGuideResDto> getLocalGuideListByRegion(String region) {
        List<Route> routeList = routeRepository.findAllByRegion(region);
        if(routeList.isEmpty())
            throw new BaseException(ErrorCode.EMPTY_ROUTE);

        return routeList.stream()
                .map(r -> new LocalGuideResDto(r))
                .collect(Collectors.toList());
    }

    public List<RouteResDto> getRouteList(Member member) {
        List<Route> routeList = routeRepository.findAllByMember(member);
        if(routeList.isEmpty())
            throw new BaseException(ErrorCode.EMPTY_ROUTE);

        return routeList.stream()
                .map(r -> {
                    Long likeCount = routeLikeRepository.countByRoute(r);
                    return new RouteResDto(r, likeCount);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Long postRoute(Member member, RouteReqDto routeReqDto) {
        Route route = Route.builder()
                        .name(routeReqDto.getName())
                        .startDate(routeReqDto.getStartDate())
                        .endDate(routeReqDto.getEndDate())
                        .region(routeReqDto.getRegion())
                        .build();

        return routeRepository.save(route).getRouteId();
    }
}
