package com.lckback.lckforall.viewing.service;

import com.lckback.lckforall.viewing.dto.GetViewingPartyDetailDTO;
import com.lckback.lckforall.viewing.dto.ParticipantListDTO;
import com.lckback.lckforall.viewing.dto.ViewingPartyListDTO;

public interface ViewingPartyService {
    ViewingPartyListDTO.ResponseList getViewingPartyList(Long userId, Integer page, Integer size);

    GetViewingPartyDetailDTO.Response getViewingPartyDetail(Long userId, Long viewingId);

    GetViewingPartyDetailDTO.ParticipateResponse createParticipant(Long userId, Long viewingPartyId);

    ParticipantListDTO.ResponseList getParticipantList(Long userId, Long viewingPartyId, Integer page, Integer size);

}
