package com.lckback.lckforall.base.api.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ViewingPartyErrorCode implements ErrorCode {

    PARTY_NOT_FOUND(HttpStatus.NOT_FOUND, "뷰잉파티를 찾을수 없습니다."),
    FAILED_PARTICIPATE_PARTY(HttpStatus.NOT_ACCEPTABLE, "뷰잉파티 참여에 실패했습니다."),
    FAILED_UPDATE_VIEWING_PARTY(HttpStatus.NOT_ACCEPTABLE, "뷰잉파티 글 수정에 실패했습니다."),
    OWNER_VIEWING_PARTY_NOT_FOUND(HttpStatus.NOT_FOUND, "개최자가 생성한 뷰잉파티 글을 찾을수 없습니다."),
    USER_IS_NOT_HOST(HttpStatus.BAD_REQUEST, "호스트가 아니라 뷰잉 파티 개최 취소 불가합니다."),
    PARTICIPATE_NOT_FOUND(HttpStatus.NOT_FOUND,"뷰잉파티의 개최자와 참여자간의 매칭이 없습니다."),
    PARTICIPATE_FORBIDDEN_OWNER(HttpStatus.FORBIDDEN, "뷰잉파티의 개최자는 자기자신의 뷰잉파티에 참여할수 없습니다.");
    private final HttpStatus httpStatus;
    private final String message;

}
