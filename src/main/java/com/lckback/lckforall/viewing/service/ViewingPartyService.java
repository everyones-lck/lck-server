package com.lckback.lckforall.viewing.service;

import com.lckback.lckforall.viewing.dto.ViewingPartyListDTO;

public interface ViewingPartyService {
    ViewingPartyListDTO.ResponseList getViewingPartyList(Long userId, Integer page, Integer size);
}
