package com.lckback.lckforall.viewing.service;

import com.lckback.lckforall.base.api.error.CommonErrorCode;
import com.lckback.lckforall.base.api.error.UserErrorCode;
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
	public ChangeViewingPartyDTO.Response createViewingParty(Long userId,
		ChangeViewingPartyDTO.CreateViewingPartyRequest request) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(
			UserErrorCode.USER_NOT_FOUND));
		ViewingParty viewingParty = ViewingPartyChangeConverter.toViewingParty(request);
		viewingParty.setUser(user);
		ViewingParty save = viewingPartyRepository.save(viewingParty);
		return ViewingPartyChangeConverter.toResponse(save);
	}
}
