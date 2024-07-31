package com.lckback.lckforall.viewing.service;

import com.lckback.lckforall.viewing.dto.ViewingPartyResponseDTO;
import com.lckback.lckforall.viewing.model.ViewingParty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ViewingPartyService {
    ViewingPartyResponseDTO.PartyListRes getViewingPartyList(Long userId, Integer page, Integer size);
}
