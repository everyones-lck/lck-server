package com.lckback.lckforall.viewing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ViewingPartyResponseDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PartyListRes{
        List<PartyRes> partyList;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PartyRes{
        String name;
        String userName;
        String photoURL;
        LocalDateTime partyDate;
        Double latitude;
        Double longitude;
        String location;
    }
}
