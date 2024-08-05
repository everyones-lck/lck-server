package com.lckback.lckforall.mypage.dto;

import jakarta.validation.constraints.Positive;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class DeleteViewingPartyDto {

    @NoArgsConstructor
    @Getter
    public static class Request {

        @Positive
        private Long viewingPartyId;
    }
}
