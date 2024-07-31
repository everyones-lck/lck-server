package com.lckback.lckforall.viewing.service;

import ch.qos.logback.core.status.ErrorStatus;
import com.lckback.lckforall.base.api.error.CommonErrorCode;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;
import com.lckback.lckforall.viewing.converter.ViewingPartyConverter;
import com.lckback.lckforall.viewing.dto.ViewingPartyResponseDTO;
import com.lckback.lckforall.viewing.model.ViewingParty;
import com.lckback.lckforall.viewing.repository.ViewingPartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ViewingPartyServiceImpl implements ViewingPartyService {
    private final UserRepository userRepository;
    private final ViewingPartyRepository viewingPartyRepository;
    @Override
    @Transactional(readOnly = true)
    public ViewingPartyResponseDTO.PartyListRes getViewingPartyList(Long userId, Integer page, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException(CommonErrorCode.PARTY_NOT_FOUND.getMessage()));
        Page<ViewingParty> viewingPartyList = viewingPartyRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
        return ViewingPartyConverter.toPartyListRes(viewingPartyList);
    }
}
