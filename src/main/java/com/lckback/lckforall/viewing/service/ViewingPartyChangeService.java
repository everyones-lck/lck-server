package com.lckback.lckforall.viewing.service;

import com.lckback.lckforall.viewing.dto.ChangeViewingPartyDTO;

public interface ViewingPartyChangeService {

    ChangeViewingPartyDTO.Response createViewingParty(String kakaoUserId, ChangeViewingPartyDTO.CreateViewingPartyRequest request);

    ChangeViewingPartyDTO.Response updateViewingParty(String kakaoUserId, Long viewingPartyId, ChangeViewingPartyDTO.CreateViewingPartyRequest request);

    ChangeViewingPartyDTO.Response deleteViewingParty(String kakaoUserId, Long viewingPartyId);
}
