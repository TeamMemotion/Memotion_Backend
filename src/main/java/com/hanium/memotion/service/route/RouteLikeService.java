package com.hanium.memotion.service.route;

import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.domain.route.Route;
import com.hanium.memotion.domain.route.RouteDetail;
import com.hanium.memotion.domain.route.RouteLike;
import com.hanium.memotion.dto.route.response.RouteResDto;
import com.hanium.memotion.dto.routedetail.RouteDetailDto;
import com.hanium.memotion.exception.base.BaseException;
import com.hanium.memotion.exception.base.ErrorCode;
import com.hanium.memotion.repository.RouteLikeMapping;
import com.hanium.memotion.repository.RouteLikeRepository;
import com.hanium.memotion.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RouteLikeService {

    private final RouteRepository routeRepository;
    private final RouteLikeRepository routeLikeRepository;

    @Transactional
    public RouteLike savelike(Long id, Member member){
        Route route = routeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 루트 기록이 존재하지 않습니다. id=" + id));
        System.out.println(route.getName());
        System.out.println(member.getUsername());
        try{

        return routeLikeRepository.save(RouteLike.builder()
                .route(route)
                .member(member)
                .build());
        }catch (Exception d){throw new BaseException(ErrorCode.LIKED_ROUTE);}
    }

    @Transactional
    public int delete(Long id, Member member){

        Route route = routeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 루트 기록이 존재하지 않습니다. id=" + id));
        Optional<RouteLike>  routeLike = routeLikeRepository.findByRouteAndMember(route,member);
        if (routeLike.isPresent()) {
            RouteLike delete = routeLike.get();
            routeLikeRepository.delete(delete);
            return 1;
        } else {
            throw new BaseException(ErrorCode.LIKED_DELETE);
        }
    }
    public List<Route> findByUserRouteLike(Member member){
        Long[] routeLikes = routeLikeRepository.findRouteIdByMember(member);
        List<Route> route = routeRepository.getRouteLikeMembers(routeLikes);
        return route;
    }

}
