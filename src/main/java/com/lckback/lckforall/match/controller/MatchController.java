package com.lckback.lckforall.match.controller;


import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.match.dto.MatchDto;
import com.lckback.lckforall.match.service.MatchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Tag(name = "Match", description = "Today's Match 관련 api")
@RestController
@RequestMapping("/api/match")
public class MatchController {

    private final MatchService matchService;

    @PostMapping("/info")
    public ApiResponse<List<MatchDto.TodayMatchResponse>> getTodayMatches() { // 오늘 경기정보 반환
        List<MatchDto.TodayMatchResponse> response = matchService.todayMatchInfo();
        return ApiResponse.createSuccess(response);
    }
}
