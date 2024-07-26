package com.lckback.lckforall.match.dto;

import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.model.Set;
import com.lckback.lckforall.player.model.Player;
import com.lckback.lckforall.team.model.Team;
import com.lckback.lckforall.vote.model.MatchPogVote;
import com.lckback.lckforall.vote.model.MatchVote;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MatchDto {
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
    }
}
