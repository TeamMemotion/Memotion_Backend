package com.hanium.memotion.service.search;

import com.hanium.memotion.domain.diary.Diary;
import com.hanium.memotion.dto.search.request.SearchReqDto;
import com.hanium.memotion.dto.search.response.SearchResDto;
import com.hanium.memotion.exception.base.BaseException;
import com.hanium.memotion.exception.base.ErrorCode;
import com.hanium.memotion.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final DiaryRepository diaryRepository;

    public List<SearchResDto> getSearchList(Double latitude, Double longitude, String filter) {
        List<Diary> diaryList = null;

        if(filter.equals("latest") && latitude == null && longitude == null) {      // 1. 검색어 없을 때 최신순 조회 결과
            diaryList = diaryRepository.findAllByShareOrderByCreatedDateDesc(true);
        } else if (filter.equals("earliest") && latitude == null && longitude == null) {  // 2. 검색어 없을 때 오래된 순 조회 결과
            diaryList = diaryRepository.findAllByShareOrderByCreatedDate(true);
        } else if(filter.equals("latest")) {            // 3. 검색어 있을 때 최신순 조회 결과
            diaryList = diaryRepository.findAllLatestNearByPlace(latitude, longitude);
        } else if(filter.equals("earliest")) {          // 4. 검색어 있을 때 오래된 순 조회 결과
            diaryList = diaryRepository.findAllEarliestNearByPlace(latitude, longitude);
        }

        if(!diaryList.isEmpty()){
            return diaryList.stream()
                    .map(d -> new SearchResDto(d))
                    .collect(Collectors.toList());
        } else throw new BaseException(ErrorCode.INVALID_SEARCH_KEYWORD);

    }
}
