package com.lckback.lckforall.viewing.service;

import com.lckback.lckforall.base.api.error.CommonErrorCode;
import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.error.ViewingPartyErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;
import com.lckback.lckforall.viewing.converter.ViewingPartyConverter;
import com.lckback.lckforall.viewing.dto.GetViewingPartyDetailDTO;
import com.lckback.lckforall.viewing.dto.ViewingPartyListDTO;
import com.lckback.lckforall.viewing.model.Participate;
import com.lckback.lckforall.viewing.model.ViewingParty;
import com.lckback.lckforall.viewing.repository.ParticipateRepository;
import com.lckback.lckforall.viewing.repository.ViewingPartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViewingPartyServiceImpl implements ViewingPartyService {
    private final UserRepository userRepository;
    private final ViewingPartyRepository viewingPartyRepository;
    private final ParticipateRepository participateRepository;
    @Override
    @Transactional(readOnly = true)
    public ViewingPartyListDTO.ResponseList getViewingPartyList(Long userId, Integer page, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        Page<ViewingParty> viewingPartyList = viewingPartyRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
        return ViewingPartyConverter.toPartyListRes(viewingPartyList);
    }

    @Override
    @Transactional
    public GetViewingPartyDetailDTO.Response getViewingPartyDetail(Long userId, Long viewingId) {
        userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        ViewingParty viewingPartyDetail = viewingPartyRepository.findById(viewingId).orElseThrow(() -> new RuntimeException(ViewingPartyErrorCode.PARTY_NOT_FOUND.getMessage()));
        return ViewingPartyConverter.toResponse(viewingPartyDetail);

    }

    @Override
    @Transactional
    public GetViewingPartyDetailDTO.ParticipateResponse createParticipant(Long userId, Long viewingPartyId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        ViewingParty viewingParty = viewingPartyRepository.findById(viewingPartyId).orElseThrow(() -> new RestApiException(ViewingPartyErrorCode.PARTY_NOT_FOUND));
        Optional<Participate> findParticipate = participateRepository.findByUserAndViewingParty(user, viewingParty);
        if(findParticipate.isEmpty()){
            Participate newParticipate = ViewingPartyConverter.toParticipate(user, viewingParty);
            newParticipate.setUser(user);
            Participate save = participateRepository.save(newParticipate);
            return ViewingPartyConverter.toParticipateResponse(save);
        }
        return ViewingPartyConverter.toParticipateResponse(findParticipate.get());
    }
}
