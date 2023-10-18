package com.hanium.memotion.dto.search.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SearchReqDto {
    private Double latitude;
    private Double longitude;
    @NotNull
    private String filter;
}
