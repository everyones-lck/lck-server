package com.lckback.lckforall.viewing.service;

import com.lckback.lckforall.viewing.dto.ChangeViewingPartyDTO;

public interface ViewingPartyChangeService {

    ChangeViewingPartyDTO.Response createViewingParty(Long userId, ChangeViewingPartyDTO.CreateViewingPartyRequest request);

    ChangeViewingPartyDTO.Response updateViewingParty(Long userId, Long viewingPartyId, ChangeViewingPartyDTO.CreateViewingPartyRequest request);

    ChangeViewingPartyDTO.Response deleteViewingParty(Long userId, Long viewingPartyId);
}
