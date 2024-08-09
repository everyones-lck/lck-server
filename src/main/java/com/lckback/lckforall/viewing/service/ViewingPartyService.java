package com.lckback.lckforall.viewing.service;

import com.lckback.lckforall.viewing.dto.GetViewingPartyDetailDTO;
import com.lckback.lckforall.viewing.dto.ParticipantListDTO;
import com.lckback.lckforall.viewing.dto.ViewingPartyListDTO;

public interface ViewingPartyService {
    ViewingPartyListDTO.ResponseList getViewingPartyList(String kakaoUserId, Integer page, Integer size);

    GetViewingPartyDetailDTO.Response getViewingPartyDetail(String kakaoUserId, Long viewingId);

    GetViewingPartyDetailDTO.ParticipateResponse createParticipant(String kakaoUserId, Long viewingPartyId);

    ParticipantListDTO.ResponseList getParticipantList(String kakaoUserId, Long viewingPartyId, Integer page, Integer size);

}
