package com.lckback.lckforall.viewing.converter;

import com.lckback.lckforall.user.model.User;
import com.lckback.lckforall.viewing.dto.GetViewingPartyDetailDTO;
import com.lckback.lckforall.viewing.dto.ParticipantListDTO;
import com.lckback.lckforall.viewing.dto.ViewingPartyListDTO;
import com.lckback.lckforall.viewing.model.Participate;
import com.lckback.lckforall.viewing.model.ViewingParty;
import org.springframework.data.domain.Page;

import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ViewingPartyConverter {

    public static ViewingPartyListDTO.ResponseList toPartyListResponse(Page<ViewingParty> viewingPartyPage, Boolean isLast, Integer totalPage) {
        List<ViewingPartyListDTO.Response> partyList = viewingPartyPage.stream().map(ViewingPartyConverter::toPartyResponse).collect(Collectors.toList());
        return ViewingPartyListDTO.ResponseList.builder()
                .isLast(isLast)
                .totalPage(totalPage)
                .partyList(partyList)
                .build();
    }


//    // 정규식을 사용하여 "~시 ~동" 형식의 주소를 추출하는 메서드
//    public static String extractCityAndDistrict(String fullAddress) {
//        // 정규식 패턴: ~시와 ~동 사이의 문자를 매칭
//        String regex = "(\\S+시)\\s(\\S+동)";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(fullAddress);
//
//        if (matcher.find()) {
//            // 첫 번째 그룹은 "~시", 두 번째 그룹은 "~동"
//            return matcher.group(1) + " " + matcher.group(2);
//        }
//
//        // 매칭되지 않는 경우 원본 주소 반환
//        return fullAddress;
//    }

    public static ViewingPartyListDTO.Response toPartyResponse(ViewingParty viewingParty) {

        return ViewingPartyListDTO.Response.builder()
                .id(viewingParty.getId())
                .name(viewingParty.getName())
                .userName(viewingParty.getUser().getNickname())
                .teamName(viewingParty.getUser().getTeam().getTeamName())
                .photoURL(viewingParty.getUser().getProfileImageUrl())
                .partyDate(viewingParty.getDate().toString())
                .latitude(viewingParty.getLatitude())
                .longitude(viewingParty.getLongitude())
                .location(viewingParty.getLocation())
                .shortLocation(viewingParty.getShortLocation())
                .build();
    }
    public static GetViewingPartyDetailDTO.Response toResponse(ViewingParty viewingParty, Boolean participated) {
        // Response 포맷팅
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        return GetViewingPartyDetailDTO.Response.builder()
                .participated(participated)
                .name(viewingParty.getName())
                .ownerName(viewingParty.getUser().getNickname())
                .ownerTeam(viewingParty.getUser().getTeam().getTeamName())
                .ownerImage(viewingParty.getUser().getProfileImageUrl())
                .qualify(viewingParty.getPartyQualify())
                .partyDate(viewingParty.getDate().toString())
                .latitude(viewingParty.getLatitude())
                .longitude(viewingParty.getLongitude())
                .location(viewingParty.getLocation())
                .price(decimalFormat.format(viewingParty.getPrice()))
                .lowParticipate(decimalFormat.format(viewingParty.getLowParticipate()))
                .highParticipate(decimalFormat.format(viewingParty.getHighParticipate()))
                .etc(viewingParty.getEtc())
                .build();
    }

    public static Participate toParticipate(User user, ViewingParty viewingParty) {
        return Participate.builder()
                .user(user)
                .viewingParty(viewingParty)
                .build();
    }

    public static GetViewingPartyDetailDTO.ParticipateResponse toParticipateResponse(Participate participate) {
        return GetViewingPartyDetailDTO.ParticipateResponse.builder()
                .participantId(participate.getUser().getId())
                .ownerId(participate.getViewingParty().getUser().getId())
                .build();
    }

    public static ParticipantListDTO.ResponseList toParticipantListResponse(Page<User> users, ViewingParty viewingParty, Boolean isLast, Integer totalPage) {
        List<ParticipantListDTO.Response> userList = users.stream().map(ViewingPartyConverter::toParticipantResponse).toList();
        return ParticipantListDTO.ResponseList.builder()
                .isLast(isLast)
                .totalPage(totalPage)
                .viewingPartyName(viewingParty.getName())
                .ownerName(viewingParty.getUser().getNickname())
                .ownerTeam(viewingParty.getUser().getTeam().getTeamName())
                .ownerImage(viewingParty.getUser().getProfileImageUrl())
                .participantList(userList)
                .build();
    }

    public static ParticipantListDTO.Response toParticipantResponse(User user) {
        return ParticipantListDTO.Response.builder()
                .id(user.getId())
                .name(user.getNickname())
                .team(user.getTeam().getTeamName())
                .image(user.getProfileImageUrl())
                .build();
    }

}
