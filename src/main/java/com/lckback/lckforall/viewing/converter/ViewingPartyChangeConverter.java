package com.lckback.lckforall.viewing.converter;

import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.viewing.dto.ChangeViewingPartyDTO;
import com.lckback.lckforall.viewing.model.ViewingParty;

public class ViewingPartyChangeConverter {
    public static ViewingParty toViewingParty(ChangeViewingPartyDTO.CreateViewingPartyRequest createViewingPartyRequest){
        return ViewingParty.builder()
                .name(createViewingPartyRequest.getName())
                .date(createViewingPartyRequest.getDate())
                .latitude(createViewingPartyRequest.getLatitude())
                .longitude(createViewingPartyRequest.getLongitude())
                .price(createViewingPartyRequest.getPrice())
                .lowParticipate(createViewingPartyRequest.getLowParticipate())
                .highParticipate(createViewingPartyRequest.getHighParticipate())
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
