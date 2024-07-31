package com.lckback.lckforall.match.service;

import com.lckback.lckforall.base.api.error.MatchErrorCode;
import com.lckback.lckforall.base.api.error.PlayerErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.match.dto.PogInfoDto;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.repository.MatchRepository;
import com.lckback.lckforall.player.model.Player;
import com.lckback.lckforall.player.model.repository.PlayerRepository;
import com.lckback.lckforall.vote.model.MatchPogVote;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PogService {

	private final MatchRepository matchRepository;

	private final PlayerRepository playerRepository;

	public PogInfoDto.MatchPogResponse matchPog(PogInfoDto.PogServiceDto dto) { // match의 pog 정보를 리턴
		Match match = matchRepository.findById(dto.getMatchId())
			.orElseThrow(() -> new RestApiException(MatchErrorCode.THERE_IS_NO_SUCH_MATCH));
		List<MatchPogVote> voteResult = match.getMatchPogVotes();
		Long winnerId = findMatchPog(voteResult);
		Player winner = playerRepository.findById(winnerId)
			.orElseThrow(() -> new RestApiException(PlayerErrorCode.THERE_IS_NO_SUCH_PLAYER));

		return PogInfoDto.MatchPogResponse.create(winnerId, winner.getName(), winner.getProfileImageUrl());
	}

	public Long findMatchPog(List<MatchPogVote> votes) { // 투표 결과를 계산하는 함수
		Map<Long, Integer> playerVoteCounts = new HashMap<>();

		for (MatchPogVote vote : votes) {
			Long playerId = vote.getPlayer().getId();
			playerVoteCounts.put(playerId, playerVoteCounts.getOrDefault(playerId, 0) + 1); // 투표 결과 반영
		}
		// 1등 플레이어 찾기
		Long winnerId = null;
		int maxVotes = 0;

		for (Map.Entry<Long, Integer> entry : playerVoteCounts.entrySet()) {
			Long playerId = entry.getKey();
			int voteCount = entry.getValue();

			// 최대 투표 수를 가진 플레이어 찾기
			if (voteCount > maxVotes) {
				maxVotes = voteCount;
				winnerId = playerId;
			}
		}

		return winnerId; // 1등 플레이어의 ID 반환

	}
}
