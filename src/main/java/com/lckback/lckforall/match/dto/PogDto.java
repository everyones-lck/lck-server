package com.lckback.lckforall.match.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PogDto {
    @Getter
    @AllArgsConstructor
    public static class MatchPogResponse {
        private Long id;

        private String name;

        private String profileImageUrl;

        public static MatchPogResponse create(Long pogId, String name, String profileImageUrl){
            return new MatchPogResponse(pogId,name,profileImageUrl);
        }
    }
    @Getter
    @AllArgsConstructor
    public static class PogServiceDto{
        private Long matchId;
        public static PogServiceDto create(Long matchId){
            return new PogServiceDto(matchId);
        }
    }
    @Getter
    @AllArgsConstructor
    public static class MatchPogRequest {
        private Long matchId;
        public PogServiceDto toDto(){
            return PogServiceDto.create(matchId);
        }

    }

}
