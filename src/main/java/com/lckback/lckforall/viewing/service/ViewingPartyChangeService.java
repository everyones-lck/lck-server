package com.lckback.lckforall.viewing.service;

import com.lckback.lckforall.viewing.dto.ChangeViewingPartyDTO;

public interface ViewingPartyChangeService {

    ChangeViewingPartyDTO.Response createViewingParty(Long userId, ChangeViewingPartyDTO.CreateViewingPartyRequest request);

}
