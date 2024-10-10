package com.lckback.lckforall.viewing.service;

import com.lckback.lckforall.base.api.error.UserErrorCode;
import com.lckback.lckforall.base.api.error.ViewingPartyErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.user.respository.UserRepository;
import com.lckback.lckforall.viewing.converter.ViewingPartyConverter;
import com.lckback.lckforall.viewing.dto.GetViewingPartyDetailDTO;
import com.lckback.lckforall.viewing.dto.ParticipantListDTO;
import com.lckback.lckforall.viewing.dto.ViewingPartyListDTO;
import com.lckback.lckforall.viewing.model.Participate;
import com.lckback.lckforall.viewing.model.ViewingParty;
import com.lckback.lckforall.viewing.repository.ChatRoomRepository;
import com.lckback.lckforall.viewing.repository.ParticipateRepository;
import com.lckback.lckforall.viewing.repository.ViewingPartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViewingPartyServiceImpl implements ViewingPartyService {

    private final UserRepository userRepository;
    private final ViewingPartyRepository viewingPartyRepository;
    private final ParticipateRepository participateRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional(readOnly = true)
    public ViewingPartyListDTO.ResponseList getViewingPartyList(String kakaoUserId, Integer page, Integer size) {
        userRepository.findByKakaoUserId(kakaoUserId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        Page<ViewingParty> viewingPartyList = viewingPartyRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
        int totalPage = viewingPartyList.getTotalPages();
        boolean isLast = false;
        if(page == totalPage - 1){
            isLast = true;
        }
        return ViewingPartyConverter.toPartyListResponse(viewingPartyList, isLast, totalPage);
    }

    @Override
    @Transactional
    public GetViewingPartyDetailDTO.Response getViewingPartyDetail(String kakaoUserId, Long viewingId) {
        User user = userRepository.findByKakaoUserId(kakaoUserId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        ViewingParty viewingPartyDetail = viewingPartyRepository.findById(viewingId).orElseThrow(() -> new RestApiException(ViewingPartyErrorCode.PARTY_NOT_FOUND));
        boolean participated = participateRepository.findByViewingPartyAndUser(viewingPartyDetail, user).isPresent();
        return ViewingPartyConverter.toResponse(viewingPartyDetail, participated);
    }

    @Override
    @Transactional
    public GetViewingPartyDetailDTO.ParticipateResponse createParticipate(String kakaoUserId, Long viewingPartyId) {
        User user = userRepository.findByKakaoUserId(kakaoUserId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        ViewingParty viewingParty = viewingPartyRepository.findById(viewingPartyId).orElseThrow(() -> new RestApiException(ViewingPartyErrorCode.PARTY_NOT_FOUND));
        Optional<Participate> findParticipate = participateRepository.findByUserAndViewingParty(user, viewingParty);
        if(findParticipate.isEmpty()){
            Participate newParticipate = ViewingPartyConverter.toParticipate(user, viewingParty);
            newParticipate.setUser(user);
            Participate save = participateRepository.save(newParticipate);
            return ViewingPartyConverter.toParticipateResponse(save);
        }
        else if (user == viewingParty.getUser()) {
            throw new RestApiException(ViewingPartyErrorCode.PARTICIPATE_FORBIDDEN_OWNER);
        }
        return ViewingPartyConverter.toParticipateResponse(findParticipate.get());
    }

    @Override
    @Transactional
    public ParticipantListDTO.ResponseList getParticipantList(String kakaoUserId, Long viewingPartyId, Integer page, Integer size) {
        userRepository.findByKakaoUserId(kakaoUserId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        ViewingParty viewingParty = viewingPartyRepository.findById(viewingPartyId).orElseThrow(() -> new RestApiException(ViewingPartyErrorCode.PARTY_NOT_FOUND));
        List<User> userList = viewingParty.getParticipates().stream().map(Participate::getUser).toList();
        List<User> chatList = chatRoomRepository.findAllByViewingPartyId(viewingPartyId).stream().map(chatRoom -> userRepository.findById(chatRoom.getUserId()).get()).toList();
        PageRequest pageRequest = PageRequest.of(page, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), userList.size());
        int endChat = Math.min((start + pageRequest.getPageSize()), chatList.size());
        Page<User> userPage = new PageImpl<>(userList.subList(start, end), pageRequest, userList.size());
        Page<User> chatPage = new PageImpl<>(chatList.subList(start, endChat), pageRequest, chatList.size());

        return ViewingPartyConverter.toParticipantListResponse(userPage, chatPage, viewingParty, page, size);
    }
}
