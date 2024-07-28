package com.lckback.lckforall.match.controller;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.match.dto.PogInfoDto;
import com.lckback.lckforall.match.service.PogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> matchPog( // match의 pog 투표 결과 선정된 player를 반환
                                    @RequestBody PogInfoDto.MatchPogRequest request){

        return ResponseEntity.ok()
                .body(ApiResponse.createSuccess(pogService.matchPog(request.toDto())));
    }
}
