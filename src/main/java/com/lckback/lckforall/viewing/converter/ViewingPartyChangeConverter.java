package com.lckback.lckforall.viewing.converter;

import com.lckback.lckforall.viewing.dto.ChangeViewingPartyDTO;
import com.lckback.lckforall.viewing.model.ViewingParty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ViewingPartyChangeConverter {
    public static ViewingParty toViewingParty(ChangeViewingPartyDTO.CreateViewingPartyRequest createViewingPartyRequest){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return ViewingParty.builder()
                .name(createViewingPartyRequest.getName())
                .date(LocalDateTime.parse(createViewingPartyRequest.getDate(), formatter))
                .latitude(createViewingPartyRequest.getLatitude())
                .longitude(createViewingPartyRequest.getLongitude())
                .location(createViewingPartyRequest.getLocation())
                .shortLocation(createViewingPartyRequest.getShortLocation())
                .price(Integer.parseInt(createViewingPartyRequest.getPrice().replace(",", "")))
                .lowParticipate(Integer.parseInt(createViewingPartyRequest.getLowParticipate().replace(",", "")))
                .highParticipate(Integer.parseInt(createViewingPartyRequest.getHighParticipate().replace(",", "")))
                .partyQualify(createViewingPartyRequest.getQualify())
                .etc(createViewingPartyRequest.getEtc())
                .build();
    }
    public static ChangeViewingPartyDTO.Response toResponse(ViewingParty viewingParty){
        return ChangeViewingPartyDTO.Response.builder()
                .userId(viewingParty.getUser().getId())
                .viewingPartyId(viewingParty.getId())
                .build();
    }
}
