package com.hanium.memotion.controller.route;

import com.hanium.memotion.domain.diary.DiaryContent;
import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.domain.route.Route;
import com.hanium.memotion.domain.route.RouteDetail;
import com.hanium.memotion.dto.diary.DiaryEmotionDto;
import com.hanium.memotion.dto.route.response.RouteResDto;
import com.hanium.memotion.dto.routedetail.RouteDetailDto;
import com.hanium.memotion.dto.routedetail.RouteDetailUserDto;
import com.hanium.memotion.exception.base.BaseException;
import com.hanium.memotion.exception.base.BaseResponse;
import com.hanium.memotion.exception.base.ErrorCode;
import com.hanium.memotion.exception.custom.BadRequestException;
import com.hanium.memotion.service.global.AWSS3Service;
import com.hanium.memotion.service.route.RouteDetailService;
import com.hanium.memotion.service.route.RouteService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/route")
@RequiredArgsConstructor
public class RouteDetailController {

    private final RouteDetailService routeDetailService;
    private final RouteService routeService;

    private final ModelMapper modelMapper;

    private final AWSS3Service awsS3Service;
    //TODO
    // - 루트기록 상세
    //    - 제목, 좋아요, 여행 기간 및 날짜들 조회 api 1개 O
    //    - 각 날짜별 첫 등록 사진, 제목, 장소, 시작시간, 종료 시간 조회 리스트 api 1개 O
    // - 루트 일정 추가 및 조회 (루트 기록 상세 안에 있는 리스트 한 개 누르거나 추가할때)
    //    - 저장 api 1개(날짜, 제목, 시작시간, 종료시간, 메모, 장소, 사진) O
    //    - 수정 api 1개 O
    //    - 삭제 api 1개 O
    @ApiOperation(
            value = "루트 디테일 저장 api"
            , notes = "날짜, 제목, 시작시간, 종료시간, 메모, 장소, 사진 저장.")
    @PostMapping("/save")
    public BaseResponse<RouteDetailDto.Response> save(@RequestPart MultipartFile multipartFile, @RequestPart RouteDetailDto.Request request,  @AuthenticationPrincipal Member member){
          String fileUrl = request.getUrl();

//        if(multipartFile == null || multipartFile.isEmpty())
//            throw new BadRequestException("이미지가 첨부되지 않았습니다.");
        try {
            if (fileUrl != null) {
                String[] url = fileUrl.split("/");
                awsS3Service.deleteImage(url[3]);   // https~ 경로 뺴고 파일명으로 삭제
            }
            String fileName = awsS3Service.uploadFile(multipartFile);
            request.setUrl(fileName);
            RouteDetail routeDetail = routeDetailService.save(request);
            return BaseResponse.onSuccess(new RouteDetailDto.Response(routeDetail));
        }catch(Exception e) {
                throw new BaseException(ErrorCode.AWS_S3_ERROR);
        }
    }
    @ApiOperation(
            value = "루트 디테일 수정 api"
            , notes = "날짜, 제목, 시작시간, 종료시간, 메모, 장소, 사진 수정, routeDetailId를 PathVariable로 보내줘야행")
    @PatchMapping("/update/{routeDetailId}")
    public BaseResponse<RouteDetailDto.Response> update(@PathVariable("routeDetailId") Long routeDetailId, @RequestBody RouteDetailDto.Request request, @AuthenticationPrincipal Member member){
        RouteDetail routeDetail = routeDetailService.update(request,routeDetailId);
        return BaseResponse.onSuccess(new RouteDetailDto.Response(routeDetail));
    }

    @ApiOperation(
            value = "제목, 좋아요, 여행 기간 및 날짜들 조회 api"
            , notes = "상세 루트 리스트 보내줘. PathVariabl 루트Id 작성해야돼")
    @GetMapping("/route/list/{routeId}")
    public BaseResponse<List<RouteDetailDto.Response>> RouteDetailList (@PathVariable("routeId") Long id, @AuthenticationPrincipal Member member) throws ParseException {
        List<RouteDetail> routeDetail =routeDetailService.findById(id);
        List<RouteDetailDto.Response> resultDto = routeDetail.stream()
                .map(data-> modelMapper.map(data, RouteDetailDto.Response.class))
                .collect(Collectors.toList());
        return BaseResponse.onSuccess(resultDto);
    }
    @ApiOperation(
            value = "각 날짜별 첫 등록 사진, 제목, 장소, 시작시간, 종료 시간 조회 리스트"
            , notes = "각 리스트 세부 내용 가져와")
    @GetMapping("/route-detail/list/{detailId}")
    public BaseResponse<RouteDetailUserDto> RouteDetail (@PathVariable("detailId") Long id, @AuthenticationPrincipal Member member) throws ParseException {
        RouteDetail routeDetail =routeDetailService.findByDetailId(id);
        Long memberId = routeService.findByRoute(routeDetail.getRoute().getRouteId());
        return BaseResponse.onSuccess(new RouteDetailUserDto(routeDetail,memberId));
    }
    @DeleteMapping("/route-detail/{route-detailId}")
    public BaseResponse<Long> delete (@PathVariable("route-detailId") Long id) throws ParseException {
        return BaseResponse.onSuccess(routeDetailService.delete(id));
    }

    @ApiOperation(
            value = "route 세부정보"
            , notes = "route 세부 내용 가져와")
    @GetMapping("/route-detail/{routeId}")
    public BaseResponse<RouteResDto> RouteDetailInfo (@PathVariable("routeId") Long id, @AuthenticationPrincipal Member member) throws ParseException {
        RouteResDto route =routeDetailService.findByRouteId(id,member);
        return BaseResponse.onSuccess(route);
    }
    @ApiOperation(
            value = "채원이의 API"
            , notes = "route 세부 내용 가져오는데 selecr-date도 준당")
    @GetMapping("/route-detail/{routeId}/{select-date}")
    public BaseResponse<List<RouteDetailDto.Response>> selectDateRouteId(@PathVariable("routeId") Long id, @PathVariable("select-date") String selectDate , @AuthenticationPrincipal Member member){
        List<RouteDetail> routeDetail =routeDetailService.findById(id);
        List<RouteDetailDto.Response> resultDto = routeDetail.stream()
                .map(data-> modelMapper.map(data, RouteDetailDto.Response.class))
                .collect(Collectors.toList());
        return BaseResponse.onSuccess(resultDto);
    }

}
