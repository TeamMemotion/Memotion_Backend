package com.hanium.memotion.service.RouteDetail;

import com.hanium.memotion.domain.route.Route;
import com.hanium.memotion.domain.route.RouteDetail;
import com.hanium.memotion.dto.routedetail.RouteDetailDto;
import com.hanium.memotion.repository.RouteDetailRepository;
import com.hanium.memotion.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;


@RequiredArgsConstructor
@Service
public class RouteDetailService {

    private final RouteDetailRepository routeDetailRepository;
    private final RouteRepository routeRepository;

    @Transactional
    public RouteDetail save(RouteDetailDto.Request request){
        Route route = routeRepository.findById(request.getRouteId()).orElseThrow(() -> new IllegalArgumentException("해당 루트 기록이 존재하지 않습니다. id=" + request.getRouteId()));
        return routeDetailRepository.save(request.toEntity(route));
    }
    @Transactional
    public RouteDetail update(RouteDetailDto.Request request, Long id){
        RouteDetail routedetail = routeDetailRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 루트 기록이 존재하지 않습니다. id=" + id));
        return routeDetailRepository.save(routedetail.builder()
                        .title(request.getTitle())
                        .start_time(request.getStart_time())
                        .end_time(request.getEnd_time())
                        .select_date(request.getSelect_date())
                        .content(request.getContent())
                        .place(request.getPlace())
                        .latitude(request.getLatitude())
                        .longitude(request.getLongitude())
                        .url(request.getUrl())
                        .build());
    }


}
