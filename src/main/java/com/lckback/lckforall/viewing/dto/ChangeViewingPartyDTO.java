package com.lckback.lckforall.viewing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ChangeViewingPartyDTO {

    @Getter
    public static class CreateViewingPartyRequest {
        @NotBlank(message = "이름을 생략할수 없습니다.")
        @Size(min = 1, max = 20, message = "이름은 1자이상 20자이하여야 합니다.")
        String name;
        LocalDateTime date;
        @NotNull(message = "장소를 생략할수 없습니다.")
        Double latitude;
        @NotNull(message = "장소를 생략할수 없습니다.")
        Double longitude;
        String location;
        @NotBlank(message = "비용을 생략할수 없습니다.")
        Integer price;
        @NotBlank(message = "최소인원을 생략할수 없습니다.")
        Integer lowParticipate;
        @NotBlank(message = "최대인원을 생략할수 없습니다.")
        Integer highParticipate;
        @NotBlank(message = "참여자격 및 조건을 생략할수 없습니다.")
        @Size(min = 1, max = 100, message = "참여자격 및 조건은 1자이상 100자이하여야 합니다.")
        String qualify;
        @Size(min = 0, max = 1000, message = "기타 메세지는 최대 1000자까지 작성할수 있습니다.")
        String etc;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        Long userId;
        Long viewingPartyId;
    }
}
