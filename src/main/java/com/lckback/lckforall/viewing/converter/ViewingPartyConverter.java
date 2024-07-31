package com.lckback.lckforall.viewing.converter;

import com.lckback.lckforall.viewing.dto.ViewingPartyResponseDTO;
import com.lckback.lckforall.viewing.model.ViewingParty;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ViewingPartyConverter {

    public static ViewingPartyResponseDTO.PartyListRes toPartyListRes(Page<ViewingParty> viewingPartyPage) {
        List<ViewingPartyResponseDTO.PartyRes> partyList = viewingPartyPage.stream().map(ViewingPartyConverter::toPartyRes).collect(Collectors.toList());
        return ViewingPartyResponseDTO.PartyListRes.builder()
                .partyList(partyList)
                .build();
    }

    public static ViewingPartyResponseDTO.PartyRes toPartyRes(ViewingParty viewingParty) {
        return ViewingPartyResponseDTO.PartyRes.builder()
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
