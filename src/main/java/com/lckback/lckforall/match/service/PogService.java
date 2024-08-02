package com.lckback.lckforall.match.service;

import com.lckback.lckforall.base.api.error.MatchErrorCode;
import com.lckback.lckforall.base.api.error.PlayerErrorCode;
import com.lckback.lckforall.base.api.error.SetErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.base.model.BaseVote;
import com.lckback.lckforall.match.dto.PogInfoDto;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.model.Set;
import com.lckback.lckforall.match.repository.MatchRepository;
import com.lckback.lckforall.player.model.Player;
import com.lckback.lckforall.player.model.repository.PlayerRepository;
import com.lckback.lckforall.vote.model.MatchPogVote;
import com.lckback.lckforall.vote.model.SetPogVote;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PogService {

	private final MatchRepository matchRepository;

	private final PlayerRepository playerRepository;

	public PogInfoDto.PogResponse findMatchPog(PogInfoDto.PogServiceDto dto) { // match의 pog 정보를 리턴
		Match match = matchRepository.findById(dto.getMatchId())
			.orElseThrow(() -> new RestApiException(MatchErrorCode.NOT_EXIST_MATCH));
		if (match.getPogPlayer() != null) { // match table에 pogPlayer값이 존재한다면 원래 있던 값 리턴
			Player winner = match.getPogPlayer();
			return PogInfoDto.PogResponse.create(winner.getId(), winner.getName(), winner.getProfileImageUrl(),
				match.getSeason().getName(), match.getMatchNumber(), match.getMatchDate());
		}
		// match table에 pogPlayer값이 존재하지 않는다면 pogPlayer 계산
		List<MatchPogVote> voteResult = match.getMatchPogVotes();
		Long winnerId = getMatchPog(voteResult);
		Player winner = playerRepository.findById(winnerId)
			.orElseThrow(() -> new RestApiException(PlayerErrorCode.NOT_EXIST_PLAYER));
		match.savePogPlayer(winner); // pog player 저장

		return PogInfoDto.PogResponse.create(winnerId, winner.getName(), winner.getProfileImageUrl(),
			match.getSeason().getName(), match.getMatchNumber(), match.getMatchDate());
	}

	public PogInfoDto.PogResponse findSetPog(PogInfoDto.PogServiceDto dto, Integer setIndex) { // set의 pog 정보를 리턴
		Match match = matchRepository.findById(dto.getMatchId())
			.orElseThrow(() -> new RestApiException(MatchErrorCode.NOT_EXIST_MATCH));
		List<Set> sets = match.getSets();
		Set nowSet = sets.get(setIndex - 1); // 해당 세트 탐색
		if (!nowSet.getSetIndex().equals(setIndex))
			throw new RestApiException(SetErrorCode.NOT_EXIST_SET);
		if (nowSet.getPogPlayer() != null) { // set table에 pogPlayer값이 존재한다면 원래 있던 값 리턴
			Player winner = nowSet.getPogPlayer();
			return PogInfoDto.PogResponse.create(winner.getId(), winner.getName(), winner.getProfileImageUrl(),
				match.getSeason().getName(), match.getMatchNumber(), match.getMatchDate());
		}
		// set table에 pogPlayer값이 존재하지 않는다면 pogPlayer 계산
		List<SetPogVote> voteResult = nowSet.getSetPogVotes();
		Long winnerId = getSetPog(voteResult);
		Player winner = playerRepository.findById(winnerId)
			.orElseThrow(() -> new RestApiException(PlayerErrorCode.NOT_EXIST_PLAYER));
		nowSet.savePogPlayer(winner);// pog player 저장

		return PogInfoDto.PogResponse.create(winnerId, winner.getName(), winner.getProfileImageUrl(),
			match.getSeason().getName(), match.getMatchNumber(), match.getMatchDate());
	}

	public <T extends BaseVote> Long findWinner(List<T> votes, Function<T, Long> getPlayerId) {
		Map<Long, Integer> playerVoteCounts = new HashMap<>();

		for (T vote : votes) {
			Long playerId = getPlayerId.apply(vote);
			playerVoteCounts.put(playerId, playerVoteCounts.getOrDefault(playerId, 0) + 1);
		}

		Long winnerId = null;
		int maxVotes = 0;

		for (Map.Entry<Long, Integer> entry : playerVoteCounts.entrySet()) {
			Long playerId = entry.getKey();
			int voteCount = entry.getValue();

			if (voteCount > maxVotes) {
				maxVotes = voteCount;
				winnerId = playerId;
			}
		}

		return winnerId;
	}

	public Long getSetPog(List<SetPogVote> votes) {
		return findWinner(votes, SetPogVote::getPlayerId);
	}

	public Long getMatchPog(List<MatchPogVote> votes) {
		return findWinner(votes, MatchPogVote::getPlayerId);
	}
}
