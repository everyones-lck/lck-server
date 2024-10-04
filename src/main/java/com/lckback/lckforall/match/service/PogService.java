package com.lckback.lckforall.match.service;

import com.lckback.lckforall.base.api.error.MatchErrorCode;
import com.lckback.lckforall.base.api.error.PlayerErrorCode;
import com.lckback.lckforall.base.api.error.VoteErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.base.model.BaseVote;
import com.lckback.lckforall.base.type.PlayerRole;
import com.lckback.lckforall.match.dto.PogInfoDto;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.model.Set;
import com.lckback.lckforall.match.repository.MatchRepository;
import com.lckback.lckforall.player.model.Player;
import com.lckback.lckforall.player.repository.PlayerRepository;
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
@Transactional
@RequiredArgsConstructor
public class PogService {

	private final MatchRepository matchRepository;

	private final PlayerRepository playerRepository;

	// public PogInfoDto.PogDTO findMatchPog(PogInfoDto.PogServiceDto dto) { // match의 pog 정보를 리턴
	//
	// 	System.out.println("now");
	// 	Match match = matchRepository.findById(dto.getMatchId())
	// 		.orElseThrow(() -> new RestApiException(MatchErrorCode.NOT_EXIST_MATCH));
	//
	// 	Player defaultPlayer = playerRepository.findByRole(PlayerRole.DEFAULT)
	// 		.orElseThrow(() -> new RestApiException(PlayerErrorCode.NOT_EXIST_PLAYER));
	//
	// 	/* 데모데이를 위해 제약 조건 해제*/
	// 	// if (match.getMatchPogVotable()) { //아직 투표가 종료되지 않으면 결과 보여주기 X
	// 	// 	throw new RestApiException(VoteErrorCode.VOTE_NOT_FINISHED_YET);
	// 	// }
	//
	// 	/* 데모데이를 위해 제약 조건 해제*/
	// 	// if (match.getPogPlayer() != defaultPlayer) { // match table에 pogPlayer값이 존재한다면 원래 있던 값 리턴
	// 	// 	Player winner = match.getPogPlayer();
	// 	// 	return PogInfoDto.PogResponse.create(winner.getId(), winner.getName(), winner.getProfileImageUrl(),
	// 	// 		match.getSeason().getName(), match.getMatchNumber(), match.getMatchDate());
	// 	// }
	//
	// 	// match table에 pogPlayer값이 존재하지 않는다면 pogPlayer 계산
	// 	List<MatchPogVote> voteResult = match.getMatchPogVotes();
	// 	if (voteResult.isEmpty()) {
	// 		throw new RestApiException(VoteErrorCode.NOT_EXIST_VOTE);
	// 	}
	//
	// 	Long winnerId = getMatchPog(voteResult);
	// 	Player winner = playerRepository.findById(winnerId)
	// 		.orElseThrow(() -> new RestApiException(PlayerErrorCode.NOT_EXIST_PLAYER));
	// 	match.savePogPlayer(winner); // pog player 저장
	//
	// 	return PogInfoDto.PogDTO.create(winnerId, winner.getName(), winner.getProfileImageUrl(),
	// 		match.getSeason().getName(), match.getMatchNumber(), match.getMatchDate());
	// }

	public PogInfoDto.PogResponse findPog(PogInfoDto.PogServiceDto dto) {
		Match match = matchRepository.findById(dto.getMatchId())
			.orElseThrow(() -> new RestApiException(MatchErrorCode.NOT_EXIST_MATCH));
		List<Set> sets = match.getSets();
		Player noPlayer = playerRepository.findByRole(PlayerRole.DEFAULT)
			.orElseThrow(() -> new RestApiException(PlayerErrorCode.NOT_EXIST_PLAYER));
		PogInfoDto.PogResponse response = new PogInfoDto.PogResponse(match);

		for(Set set:sets){ // setPog 투표 결과 정리
			if (set.getVotable()) { // 투표가 종료되지 않아서 결과 집계 X
				continue;
			}
			if (set.getPogPlayer() != noPlayer) { // set table에 pogPlayer값이 존재한다면 원래 있던 값 리턴
				Player player = set.getPogPlayer();
				response.addSetPog(player,set.getSetIndex());
				continue;
			}
			List<SetPogVote> voteResult = set.getSetPogVotes();

			if (voteResult.isEmpty()) {
				response.addSetPog(noPlayer, set.getSetIndex());// 투표가 없다면 noPlayer 결과 전송
				continue;
			}

			Long winnerId = getSetPog(voteResult);
			Player winner = playerRepository.findById(winnerId)
				.orElseThrow(() -> new RestApiException(PlayerErrorCode.NOT_EXIST_PLAYER));
			set.savePogPlayer(winner);// pog player 저장
			response.addSetPog(winner, set.getSetIndex());
		}

		if(match.getMatchPogVotable()){
			return response;
		}
		if(match.getPogPlayer() != noPlayer){
			Player player = match.getPogPlayer();
			response.setMatchPog(player);
			return response;
		}
		List<MatchPogVote> voteResult = match.getMatchPogVotes();
		if (voteResult.isEmpty()) {
			response.setMatchPog(noPlayer);
			return response;
		}
		Long winnerId = getMatchPog(voteResult);
		Player winner = playerRepository.findById(winnerId)
			.orElseThrow(() -> new RestApiException(PlayerErrorCode.NOT_EXIST_PLAYER));
		match.savePogPlayer(winner);// pog player 저장
		response.setMatchPog(winner);
		return response;

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
