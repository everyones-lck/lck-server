package com.lckback.lckforall.viewing.controller;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.auth.service.AuthService;
import com.lckback.lckforall.viewing.dto.GetViewingPartyDetailDTO;
import com.lckback.lckforall.viewing.dto.ViewingPartyListDTO;
import com.lckback.lckforall.viewing.service.ViewingPartyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/viewing")
public class ViewingController {

    private final ViewingPartyService viewingPartyService;
    private final AuthService authService;

    @GetMapping("/list")
    @Operation(summary = "뷰잉파티 목록 조회 API", description = "뷰잉파티 목록을 조회하는 API이며, 페이징을 포함합니다. query String 으로 page 번호를 주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "VIEWING4001", description = "NOT_FOUND, 뷰잉파티글을 찾을 수 없습니다."),

    })
    @Parameters({
            @Parameter(name = "Authorization", description = "RequestHeader - 로그인한 사용자 토큰"),
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 (0부터 시작)"),
            @Parameter(name = "size", description = "query string(RequestParam) - 몇 개씩 불러올지 개수를 세는 변수 (1 이상 자연수로 설정)")
    })
    public ApiResponse<ViewingPartyListDTO.ResponseList> getViewingPartyList(@RequestHeader(name = "Authorization") String accessToken,
                                                                             @RequestParam(name = "page") Integer page,
                                                                             @RequestParam(name = "size") Integer size) {
        String kakaoUserId = authService.getKakaoUserId(accessToken);
        return ApiResponse.createSuccess(viewingPartyService.getViewingPartyList(kakaoUserId, page, size));
    }

    @GetMapping("/{viewing_party_id}/detail")
    @Operation(summary = "뷰잉파티 상세 조회 API", description = "뷰잉파티 글을 상세 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "VIEWING4001", description = "NOT_FOUND, 뷰잉파티글을 찾을 수 없습니다."),

    })
    @Parameters({
            @Parameter(name = "Authorization", description = "RequestHeader - 로그인한 사용자 토큰"),
            @Parameter(name = "viewing_party_id", description = "query string(RequestParam) - 해당 뷰잉파티 글의 ID"),
    })
    public ApiResponse<GetViewingPartyDetailDTO.Response> getViewingPartyList(@RequestHeader(name = "Authorization") String accessToken,
                                                                              @PathVariable(name = "viewing_party_id") Long viewingId) {
        String kakaoUserId = authService.getKakaoUserId(accessToken);
        return ApiResponse.createSuccess(viewingPartyService.getViewingPartyDetail(kakaoUserId, viewingId));
    }

    @PostMapping("/{viewing_party_id}/detail")
    @Operation(summary = "뷰잉파티 참여하기 API", description = "뷰잉파티에 참여하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "NOT_FOUND, 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "VIEWING4001", description = "NOT_FOUND, 뷰잉파티글을 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "VIEWING4002", description = "NOT_ACCEPTABLE, 뷰잉파티 참여에 실패했습니다."),

    })
    @Parameters({
            @Parameter(name = "Authorization", description = "RequestHeader - 로그인한 사용자 토큰"),
            @Parameter(name = "viewing_party_id", description = "query string(RequestParam) - 해당 뷰잉파티 글의 ID"),
    })
    public ApiResponse<?> createParticipant(@RequestHeader(name = "Authorization") String accessToken,
                                            @PathVariable(name = "viewing_party_id") Long viewingPartyId) {
        String kakaoUserId = authService.getKakaoUserId(accessToken);
        return ApiResponse.createSuccess(viewingPartyService.createParticipant(kakaoUserId, viewingPartyId));
    }

    @GetMapping("/{viewing_party_id}/participants")
    @Operation(summary = "뷰잉파티 참여자 목록 조회 API", description = "뷰잉파티에 참여하는 사용자의 목록을 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "NOT_FOUND, 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "VIEWING4001", description = "NOT_FOUND, 뷰잉파티글을 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "VIEWING4002", description = "NOT_ACCEPTABLE, 뷰잉파티 참여에 실패했습니다."),

    })
    @Parameters({
            @Parameter(name = "Authorization", description = "RequestHeader - 로그인한 사용자 토큰"),
            @Parameter(name = "viewing_party_id", description = "query string(RequestParam) - 해당 뷰잉파티 글의 ID"),
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 (0부터 시작)"),
            @Parameter(name = "size", description = "query string(RequestParam) - 몇 개씩 불러올지 개수를 세는 변수 (1 이상 자연수로 설정)")
    })
    public ApiResponse<?> getParticipantList(@RequestHeader(name = "Authorization") String accessToken,
                                             @PathVariable(name = "viewing_party_id") Long viewingPartyId,
                                             @RequestParam(name = "page") Integer page,
                                             @RequestParam(name = "size") Integer size) {
        String kakaoUserId = authService.getKakaoUserId(accessToken);
        return ApiResponse.createSuccess(viewingPartyService.getParticipantList(kakaoUserId, viewingPartyId, page, size));
    }
}