package com.lckback.lckforall.viewing.controller;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.viewing.converter.ViewingPartyConverter;
import com.lckback.lckforall.viewing.model.ViewingParty;
import com.lckback.lckforall.viewing.service.ViewingPartyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/viewing")
public class ViewingController {

    private final ViewingPartyService viewingPartyService;


    // 기능 API
    @GetMapping("/alarm")
    @Operation(summary = "예시 API", description = "다음과 같이 작성해주세요. 스웨거 작성 예시입니다. query String 으로 page 번호를 주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "VIEWING4001", description = "NOT_FOUND, 뷰잉파티글을 찾을 수 없습니다."),

    })
    @Parameters({
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 (0부터 시작)"),
            @Parameter(name = "userId", description = "RequestHeader - 로그인한 사용자 아이디(accessToken으로 변경 예정)")
    })
    public ApiResponse<String> testSwagger(@RequestHeader("userId") Long userId,
                                           @RequestParam(name = "page") Integer page){
        return ApiResponse.createSuccess("사용자 아이디 : %d, 페이지 번호 : %d".formatted(userId,page));
    }


    @GetMapping("/list")
    @Operation(summary = "뷰잉파티 목록 조회 API", description = "뷰잉파티 목록을 조회하는 API이며, 페이징을 포함합니다. query String 으로 page 번호를 주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "VIEWING4001", description = "NOT_FOUND, 뷰잉파티글을 찾을 수 없습니다."),

    })
    @Parameters({
            @Parameter(name = "user_id", description = "RequestHeader - 로그인한 사용자 아이디(accessToken으로 변경 예정)"),
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 (0부터 시작)"),
            @Parameter(name = "size", description = "query string(RequestParam) - 몇 개씩 불러올지 개수를 세는 변수 (1 이상 자연수로 설정)")
    })
    public ApiResponse<?> getViewingPartyList(@RequestHeader(name = "user_id") Long userId,
                                              @RequestParam(name = "page") Integer page,
                                              @RequestParam(name = "size") Integer size){
        return ApiResponse.createSuccess(viewingPartyService.getViewingPartyList(userId, page, size));
    }
}