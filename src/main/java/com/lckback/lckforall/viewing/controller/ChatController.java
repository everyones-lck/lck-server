package com.lckback.lckforall.viewing.controller;

import com.lckback.lckforall.base.api.ApiResponse;
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

    @PostMapping("/chatroom/{viewing_party_id}")
    @Operation(summary = "뷰잉파티 채팅 생성 API", description = "뷰잉파티 채팅방을 생성하는 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "VIEWING4001", description = "NOT_FOUND, 뷰잉파티글을 찾을 수 없습니다.")

    })
    @Parameters({
            @Parameter(name = "user_id", description = "RequestHeader - 로그인한 사용자 아이디(accessToken으로 변경 예정)"),
    })
    public ApiResponse<ChatDTO.ChatRoomResponse> createChatRoom(@RequestHeader(name = "user_id") Long userId,
                                                                @PathVariable(name = "viewing_party_id") Long viewingPartyId) {
        return ApiResponse.createSuccess(chatService.createChatRoom(userId, viewingPartyId));

    }

    @GetMapping("/chat_log/{room_id}")
    @Operation(summary = "뷰잉파티 채팅 기록 조회 API", description = "뷰잉파티 채팅방의 이전 채팅 기록들을 조회하는 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "VIEWING4001", description = "NOT_FOUND, 뷰잉파티글을 찾을 수 없습니다.")

    })
    @Parameters({
            @Parameter(name = "user_id", description = "RequestHeader - 로그인한 사용자 아이디(accessToken으로 변경 예정)"),
            @Parameter(name = "room_id", description = "query string(RequestParam) - 만들어진 채팅방의 ID"),
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 (0부터 시작)"),
            @Parameter(name = "size", description = "query string(RequestParam) - 몇 개씩 불러올지 개수를 세는 변수 (1 이상 자연수로 설정)"),
    })
    public ApiResponse<ChatDTO.ChatMessageListResponse> getChatMessage(@RequestHeader(name = "user_id") Long userId,
                                                                @RequestParam(name = "room_id") Long roomId,
                                                                @RequestParam(name = "page") Integer page,
                                                                @RequestParam(name = "size") Integer size) {

        return ApiResponse.createSuccess(chatService.getChatMessage(userId, roomId, page, size));

    }
}
