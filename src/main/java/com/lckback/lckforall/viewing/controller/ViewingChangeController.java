package com.lckback.lckforall.viewing.controller;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.viewing.dto.ChangeViewingPartyDTO;
import com.lckback.lckforall.viewing.service.ViewingPartyChangeServiceImpl;
import com.lckback.lckforall.viewing.service.ViewingPartyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/viewing")
public class ViewingChangeController {

    private final ViewingPartyChangeServiceImpl viewingPartyChangeService;
    @PostMapping("/create")
    @Operation(summary = "뷰잉파티 개최 API", description = "뷰잉파티를 개최하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "VIEWING4003", description = "NOT_FOUND, 뷰잉파티 글 생성에 실패했습니다."),

    })
    @Parameters({
            @Parameter(name = "user_id", description = "RequestHeader - 로그인한 사용자 아이디(accessToken으로 변경 예정)"),
    })
    public ApiResponse<?> createViewingParty(@RequestHeader(name = "user_id") Long userId,
                                             @RequestBody @Valid ChangeViewingPartyDTO.CreateViewingPartyRequest request){
        return ApiResponse.createSuccess(viewingPartyChangeService.createViewingParty(userId, request));
    }
}
