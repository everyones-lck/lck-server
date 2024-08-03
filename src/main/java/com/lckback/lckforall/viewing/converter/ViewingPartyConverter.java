package com.lckback.lckforall.viewing.converter;

import com.lckback.lckforall.viewing.dto.ViewingPartyListDTO;
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
}
