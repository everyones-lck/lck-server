package com.lckback.lckforall.match.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class PogInfoDto {
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
