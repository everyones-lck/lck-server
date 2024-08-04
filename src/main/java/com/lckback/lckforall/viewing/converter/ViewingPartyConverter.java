package com.lckback.lckforall.viewing.converter;

import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.viewing.dto.GetViewingPartyDetailDTO;
import com.lckback.lckforall.viewing.dto.ViewingPartyListDTO;
import com.lckback.lckforall.viewing.model.Participate;
import com.lckback.lckforall.viewing.model.ViewingParty;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ViewingPartyConverter {

    public static ViewingPartyListDTO.ResponseList toPartyListRes(Page<ViewingParty> viewingPartyPage) {
        List<ViewingPartyListDTO.Response> partyList = viewingPartyPage.stream().map(ViewingPartyConverter::toPartyRes).collect(Collectors.toList());
        return ViewingPartyListDTO.ResponseList.builder()
                .partyList(partyList)
                .build();
    }

    public static ViewingPartyListDTO.Response toPartyRes(ViewingParty viewingParty) {
        return ViewingPartyListDTO.Response.builder()
                .name(viewingParty.getName())
                .userName(viewingParty.getUser().getNickname())
                .photoURL(viewingParty.getUser().getProfileImageUrl())
                .partyDate(viewingParty.getDate())
                .latitude(viewingParty.getLatitude())
                .longitude(viewingParty.getLongitude())
                .location(viewingParty.getLocation())
                .build();
    }
    public static GetViewingPartyDetailDTO.Response toResponse(ViewingParty viewingParty) {
        return GetViewingPartyDetailDTO.Response.builder()
                .name(viewingParty.getName())
                .ownerName(viewingParty.getUser().getNickname())
                .ownerTeam(viewingParty.getUser().getTeam().getTeamName())
                .ownerImage(viewingParty.getUser().getProfileImageUrl())
                .qualify(viewingParty.getPartyQualify())
                .partyDate(viewingParty.getDate())
                .location(viewingParty.getLocation())
                .price(viewingParty.getPrice())
                .lowParticipate(viewingParty.getLowParticipate())
                .highParticipate(viewingParty.getHighParticipate())
                .etc(viewingParty.getEtc())
                .build();
    }

    public static Participate toParticipate(User user, ViewingParty viewingParty) {
        return Participate.builder()
                .user(user)
                .viewingParty(viewingParty)
                .build();
    }

    public static GetViewingPartyDetailDTO.ParticipateResponse toParticipateResponse(Participate participate) {
        return GetViewingPartyDetailDTO.ParticipateResponse.builder()
                .participantId(participate.getUser().getId())
                .ownerId(participate.getViewingParty().getUser().getId())
                .build();
    }
}
