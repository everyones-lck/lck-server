package com.lckback.lckforall.viewing.controller;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.auth.service.AuthService;
import com.lckback.lckforall.viewing.dto.ChatDTO;
import com.lckback.lckforall.viewing.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/viewing")
public class ChatController {

    private final ChatService chatService;
    private final AuthService authService;

    @PostMapping("/{viewing_party_id}/chatroom")
    @Operation(summary = "뷰잉파티 채팅 생성 API", description = "뷰잉파티 채팅방을 생성하는 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "VIEWING4001", description = "NOT_FOUND, 뷰잉파티글을 찾을 수 없습니다.")

    })
    @Parameters({
            @Parameter(name = "Authorization", description = "RequestHeader - 로그인한 사용자 토큰"),
    })
    public ApiResponse<ChatDTO.ChatRoomResponse> createChatRoom(@RequestHeader(name = "Authorization") String accessToken,
                                                                @PathVariable(name = "viewing_party_id") Long viewingPartyId) {
        String kakaoUserId = authService.getKakaoUserId(accessToken);
        return ApiResponse.createSuccess(chatService.createChatRoom(kakaoUserId, viewingPartyId));

    }

    @GetMapping("/chat_log/{room_id}")
    @Operation(summary = "뷰잉파티 채팅 기록 조회 API", description = "뷰잉파티 채팅방의 이전 채팅 기록들을 조회하는 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "VIEWING4001", description = "NOT_FOUND, 뷰잉파티글을 찾을 수 없습니다.")

    })
    @Parameters({
            @Parameter(name = "Authorization", description = "RequestHeader - 로그인한 사용자 토큰"),
            @Parameter(name = "room_id", description = "query string(RequestParam) - 만들어진 채팅방의 ID"),
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 (0부터 시작)"),
            @Parameter(name = "size", description = "query string(RequestParam) - 몇 개씩 불러올지 개수를 세는 변수 (1 이상 자연수로 설정)"),
    })
    public ApiResponse<ChatDTO.ChatMessageListResponse> getChatMessage(@RequestHeader(name = "Authorization") String accessToken,
                                                                @PathVariable(name = "room_id") Long roomId,
                                                                @RequestParam(name = "page") Integer page,
                                                                @RequestParam(name = "size") Integer size) {
        String kakaoUserId = authService.getKakaoUserId(accessToken);
        return ApiResponse.createSuccess(chatService.getChatMessage(kakaoUserId, roomId, page, size));

    }
}
