package com.lckback.lckforall.viewing.service;

import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.error.ViewingPartyErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;
import com.lckback.lckforall.viewing.converter.ViewingPartyChangeConverter;
import com.lckback.lckforall.viewing.dto.ChangeViewingPartyDTO;
import com.lckback.lckforall.viewing.model.ViewingParty;
import com.lckback.lckforall.viewing.repository.ViewingPartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ViewingPartyChangeServiceImpl implements ViewingPartyChangeService {

	private final UserRepository userRepository;
	private final ViewingPartyRepository viewingPartyRepository;


    @Override
    @Transactional
    public ChangeViewingPartyDTO.Response createViewingParty(String kakaoUserId, ChangeViewingPartyDTO.CreateViewingPartyRequest request) {
        User user = userRepository.findByKakaoUserId(kakaoUserId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        ViewingParty viewingParty = ViewingPartyChangeConverter.toViewingParty(request);
        viewingParty.setUser(user);
        ViewingParty save = viewingPartyRepository.save(viewingParty);
        return ViewingPartyChangeConverter.toResponse(save);
    }

    @Override
    @Transactional
    public ChangeViewingPartyDTO.Response updateViewingParty(String kakaoUserId, Long viewingPartyId, ChangeViewingPartyDTO.CreateViewingPartyRequest request) {
        User user = userRepository.findByKakaoUserId(kakaoUserId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        // 글 작성자 본인이 아닐경우 에러발생
        ViewingParty viewingParty = viewingPartyRepository.findByIdAndUser(viewingPartyId, user).orElseThrow(() -> new RestApiException(ViewingPartyErrorCode.OWNER_VIEWING_PARTY_NOT_FOUND));
        ViewingParty viewingPartyRequest = ViewingPartyChangeConverter.toViewingParty(request);
        viewingParty.setName(viewingPartyRequest.getName());
        viewingParty.setDate(viewingPartyRequest.getDate());
        viewingParty.setLatitude(viewingPartyRequest.getLatitude());
        viewingParty.setLongitude(viewingPartyRequest.getLongitude());
        viewingParty.setLocation(viewingPartyRequest.getLocation());
        viewingParty.setShortLocation(viewingPartyRequest.getShortLocation());
        viewingParty.setPrice(viewingPartyRequest.getPrice());
        viewingParty.setLowParticipate(viewingPartyRequest.getLowParticipate());
        viewingParty.setHighParticipate(viewingPartyRequest.getHighParticipate());
        viewingParty.setPartyQualify(viewingPartyRequest.getPartyQualify());
        viewingParty.setEtc(viewingPartyRequest.getEtc());
        ViewingParty save = viewingPartyRepository.save(viewingParty);
        return ViewingPartyChangeConverter.toResponse(save);
    }

    @Override
    @Transactional
    public ChangeViewingPartyDTO.Response deleteViewingParty(String kakaoUserId, Long viewingPartyId) {
        User user = userRepository.findByKakaoUserId(kakaoUserId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        ViewingParty viewingParty = viewingPartyRepository.findByIdAndUser(viewingPartyId, user).orElseThrow(() -> new RestApiException(ViewingPartyErrorCode.OWNER_VIEWING_PARTY_NOT_FOUND));
        viewingPartyRepository.delete(viewingParty);
        return ViewingPartyChangeConverter.toResponse(viewingParty);
    }
}
