package com.lckback.lckforall.viewing.controller;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.viewing.dto.ChangeViewingPartyDTO;
import com.lckback.lckforall.viewing.service.ViewingPartyChangeServiceImpl;
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

    @PatchMapping("/{viewing_party_id}/update")
    @Operation(summary = "뷰잉파티 수정 API", description = "뷰잉파티 글을 수정하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "VIEWING4003", description = "NOT_ACCEPTABLE, 뷰잉파티 글 수정에 실패했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "VIEWING4004", description = "NOT_FOUND, 개최자가 생성한 뷰잉파티 글을 찾을수 없습니다."),

    })
    @Parameters({
            @Parameter(name = "user_id", description = "RequestHeader - 로그인한 사용자 아이디(accessToken으로 변경 예정)"),
            @Parameter(name = "viewing_party_id", description = "query string(RequestParam) - 해당 뷰잉파티 글의 ID"),
    })
    public ApiResponse<?> updateViewingParty(@RequestHeader(name = "user_id") Long userId,
                                             @RequestParam(name = "viewing_party_id") Long viewingPartyId,
                                             @RequestBody @Valid ChangeViewingPartyDTO.CreateViewingPartyRequest request){
        return ApiResponse.createSuccess(viewingPartyChangeService.updateViewingParty(userId, viewingPartyId, request));
    }

    @DeleteMapping("/{viewing_party_id}/delete")
    @Operation(summary = "뷰잉파티 삭제 API", description = "뷰잉파티 글을 삭제하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "VIEWING4005", description = "NOT_ACCEPTABLE, 뷰잉파티 글 삭제에 실패했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "VIEWING4004", description = "NOT_FOUND, 개최자가 생성한 뷰잉파티 글을 찾을수 없습니다."),

    })
    @Parameters({
            @Parameter(name = "user_id", description = "RequestHeader - 로그인한 사용자 아이디(accessToken으로 변경 예정)"),
            @Parameter(name = "viewing_party_id", description = "query string(RequestParam) - 해당 뷰잉파티 글의 ID"),
    })
    public ApiResponse<?> deleteViewingParty(@RequestHeader(name = "user_id") Long userId,
                                             @RequestParam(name = "viewing_party_id") Long viewingPartyId){
        return ApiResponse.createSuccess(viewingPartyChangeService.deleteViewingParty(userId, viewingPartyId));
    }
}
