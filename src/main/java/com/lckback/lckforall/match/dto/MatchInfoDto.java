package com.lckback.lckforall.match.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MatchInfoDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TodayMatchResponse{ // today's match response
        private Long id;

        private LocalDateTime matchDate;

        private String team1Name;

        private String team1LogoUrl;

        private String team2Name;

        private String team2LogoUrl;

        private Double team1VoteRate;

        private Double team2VoteRate;

        private String seasonInfo;
    }
}
