package com.hanium.memotion.service.search;

import com.hanium.memotion.domain.diary.Diary;
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

        if(filter.equals("default")) {      // 1. 검색어 없을 때 default 결과
            // 공개 설정되어있는 것중에 최신순으로 orderby한 것들
            diaryList = diaryRepository.findAllByShareOrderByCreatedDateDesc(true);
        } else if(filter.equals("latest")) {            // 2. 검색어 있을 때 최신순 조회 결과
            diaryList = diaryRepository.findAllLatestNearByPlace(latitude, longitude);
        } else if(filter.equals("earliest")) {          // 3. 검색어 있을 때 오래된 순 조회 결과
            diaryList = diaryRepository.findAllEarliestNearByPlace(latitude, longitude);
        }

        if(!diaryList.isEmpty()){
            return diaryList.stream()
                    .map(d -> new SearchResDto(d))
                    .collect(Collectors.toList());
        } else throw new BaseException(ErrorCode.INVALID_SEARCH_KEYWORD);

    }
}
