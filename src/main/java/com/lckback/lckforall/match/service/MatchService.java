package com.lckback.lckforall.match.service;

import com.lckback.lckforall.base.api.error.CommonErrorCode;
import com.lckback.lckforall.base.api.error.MatchErrorCode;
import com.lckback.lckforall.base.api.error.VoteErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.match.dto.MatchDto;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.repository.MatchRepository;
import com.lckback.lckforall.vote.model.MatchVote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    public List<MatchDto.TodayMatchResponse> todayMatchInfo(){ // today match의 정보를 List 형식으로 리턴
        List<Match> todayMatches = matchRepository.findMatchesByDate(LocalDateTime.now());
        if(todayMatches.isEmpty()){
            throw new RestApiException(MatchErrorCode.THERE_IS_NO_MATCH_TODAY);
        }
        return todayMatches.stream()
                .map(match -> new MatchDto.TodayMatchResponse(match.getId(), match.getMatchDate(),
                        match.getTeam1().getTeamName(),match.getTeam1().getTeamLogoUrl(),
                        match.getTeam2().getTeamName(),match.getTeam2().getTeamLogoUrl(),
                        calculateMatchVoteResult(match.getMatchVotes(),match.getTeam1().getId()), // team1
                        calculateMatchVoteResult(match.getMatchVotes(),match.getTeam2().getId()))) // team2
                .collect(Collectors.toList());
    }
    public Double calculateMatchVoteResult(List<MatchVote> votes,Long teamId){ // 해당 팀의 승부 예측결과 %단위로 리턴
        if(votes.isEmpty()){
            throw new RestApiException(VoteErrorCode.THERE_IS_NO_VOTE);
        }
        int teamVotes = 0;
        int totalVotes = votes.size();

        // 팀의 투표 수 집계
        for (MatchVote vote : votes) {
            if (vote.getTeam().getId().equals(teamId)) {
                teamVotes++;
            }
        }

        // 득표율 계산
        return (totalVotes > 0) ? (teamVotes / (double) totalVotes) * 100 : 0.0;
    }
    /*  선수 pog 득표율 계산 때 사용
        // 팀별 투표 수와 전체 투표 수 집계
        Map<Long, Integer> teamVoteCounts = new HashMap<>();
        int totalVotes = votes.size();

        for (MatchVote vote : votes) {
            Long teamId = vote.getTeam().getId();
            teamVoteCounts.put(teamId, teamVoteCounts.getOrDefault(teamId, 0) + 1);
        }

        // 팀별 득표율 계산
        Map<Long, Double> teamVotePercentages = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : teamVoteCounts.entrySet()) {
            Long teamId = entry.getKey();
            int teamVotes = entry.getValue();
            double percentage = (totalVotes > 0) ? (teamVotes / (double) totalVotes) * 100 : 0.0;
            teamVotePercentages.put(teamId, percentage);
        }
        return teamVotePercentages;

    */
}
