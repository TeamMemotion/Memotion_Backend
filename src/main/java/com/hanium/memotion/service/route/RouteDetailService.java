package com.hanium.memotion.service.route;

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
    public RouteDetail update(RouteDetail routeDetail){

        return routeDetailRepository.save(routeDetail);
    }


}
