package com.hanium.memotion.service.route;

import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.domain.route.Route;
import com.hanium.memotion.domain.route.RouteDetail;
import com.hanium.memotion.domain.route.RouteLike;
import com.hanium.memotion.dto.route.response.RouteResDto;
import com.hanium.memotion.dto.routedetail.RouteDetailDto;
import com.hanium.memotion.repository.RouteDetailRepository;
import com.hanium.memotion.repository.RouteLikeRepository;
import com.hanium.memotion.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class RouteDetailService {

    private final RouteDetailRepository routeDetailRepository;
    private final RouteRepository routeRepository;

    private final RouteLikeRepository routeLikeRepository;


    @Transactional
    public RouteDetail save(RouteDetailDto.Request request){
        Route route = routeRepository.findById(request.getRouteId()).orElseThrow(() -> new IllegalArgumentException("해당 루트 기록이 존재하지 않습니다. id=" + request.getRouteId()));
        return routeDetailRepository.save(request.toEntity(route));
    }
    @Transactional
    public RouteDetail update(RouteDetailDto.Request request, Long id){
        Route route = routeRepository.findById(request.getRouteId()).orElseThrow(() -> new IllegalArgumentException("해당 루트 기록이 존재하지 않습니다. id=" + request.getRouteId()));
        RouteDetail routedetail = routeDetailRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 루트 기록이 존재하지 않습니다. id=" + id));

        return routeDetailRepository.save(routedetail.builder()
                        .recordDetailId(id)
                        .title(request.getTitle())
                        .start_time(request.getStart_time())
                        .end_time(request.getEnd_time())
                        .select_date(request.getSelect_date())
                        .content(request.getContent())
                        .place(request.getPlace())
                        .latitude(request.getLatitude())
                        .longitude(request.getLongitude())
                        .url(request.getUrl())
                        .route(route)
                        .build());
    }
    public List<RouteDetail> findById(Long id){
        return routeDetailRepository.findByRouteId(id);
    }
    public RouteDetail findByDetailId(Long id){
        return routeDetailRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 루트 기록이 존재하지 않습니다. id=" + id));
    }
    public Long delete(Long id){
        routeDetailRepository.delete(routeDetailRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 다이어리가 존재하지 않습니다. id=" + id)));
        return  id;
    }
    public RouteResDto findByRouteId(Long id, Member member){
        Route route = routeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 루트 기록이 존재하지 않습니다. id=" + id));
        boolean isLiked = false;
        Optional<RouteLike> getRouteLike = routeLikeRepository.findByRouteAndMember(route, member);
        Long likeCount = routeLikeRepository.countByRoute(route);

        if(!getRouteLike.isEmpty())
            isLiked = true;

        return new RouteResDto(route, isLiked, likeCount);
    }
}
