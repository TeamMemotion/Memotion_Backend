package com.hanium.memotion.dto.route.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RouteReqDto {
    @NotNull
    private String name;
    @NotNull
    private String startDate;
    @NotNull
    private String endDate;
    @NotNull
    private String region;
}
