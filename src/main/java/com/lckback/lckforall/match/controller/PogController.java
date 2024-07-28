package com.lckback.lckforall.match.controller;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.match.dto.PogDto;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.service.PogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "POG", description = "POG 관련 api")
@RestController
@RequestMapping("/api/pog")
public class PogController {

    private final PogService pogService;

    @PostMapping("/match")
    public ApiResponse<PogDto.MatchPogResponse> matchPog( // match의 pog 투표 결과 선정된 player를 반환
            @RequestBody PogDto.MatchPogRequest request){

        return ApiResponse.createSuccess(pogService.matchPog(request.toDto()));
    }
}
