package com.lckback.lckforall.vote.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.base.auth.service.AuthService;
import com.lckback.lckforall.vote.dto.MatchVoteDto;
import com.lckback.lckforall.vote.dto.SetVoteDto;
import com.lckback.lckforall.vote.service.VoteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Tag(name = "Vote", description = "Today's Match 투표 관련 api")
@RestController
@RequestMapping("/votes")
public class VoteController {
	private final VoteService voteService;

	private final AuthService authService;

	@Operation(summary = "매치 승부예측 후보 반환 API", description = "매치 승부예측 후보정보를 반환합니다")
	@GetMapping("/match/candidates")
	public ResponseEntity<ApiResponse<MatchVoteDto.MatchPredictionCandidateResponse>> getCandidateMatchVote(
		@RequestHeader("Authorization") String token,
		@RequestParam("match-id") Long matchId) {
		String kakaoUserId = authService.getKakaoUserId(token);
		MatchVoteDto.MatchPredictionCandidateResponse response = voteService.getMatchPredictionCandidate(
			new MatchVoteDto.MatchPredictionCandidateDto(kakaoUserId,matchId));
		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}

	@Operation(summary = "매치 승부예측 투표 API", description = "매치 승부예측 투표를 진행합니다")
	@PostMapping("/match/making")
	public ResponseEntity<ApiResponse<Void>> makeMatchVote(
		@RequestHeader("Authorization") String token,
		@RequestBody MatchVoteDto.MatchPredictionRequest request) {
		String kakaoUserId = authService.getKakaoUserId(token);
		voteService.doMatchPredictionVote(request.toDto(kakaoUserId));
		return ResponseEntity.ok()
			.body(ApiResponse.createSuccessWithNoContent());
	}

	@Operation(summary = "match-pog 투표 후보 반환 API", description = "각 매치의 match-pog 투표 후보를 반환합니다")
	@GetMapping("/match-pog/candidates")
	public ResponseEntity<ApiResponse<MatchVoteDto.MatchPogVoteCandidateResponse>> getCandidateMatchPogVote(
		@RequestHeader("Authorization") String token,
		@RequestParam("match-id") Long matchId) {
		String kakaoUserId = authService.getKakaoUserId(token);
		MatchVoteDto.MatchPogVoteCandidateResponse response = voteService.getMatchPogCandidate(
			new MatchVoteDto.MatchPredictionCandidateDto(kakaoUserId, matchId));
		return ResponseEntity.ok().body(ApiResponse.createSuccess(response));
	}

	@Operation(summary = "match-pog 투표 API", description = "match-pog 투표를 진행합니다")
	@PostMapping("match-pog/making")
	public ResponseEntity<ApiResponse<Void>> makeMatchPogVote(
		@RequestHeader("Authorization") String token,
		@RequestBody MatchVoteDto.MatchPogVoteRequest request) {
		String kakaoUserId = authService.getKakaoUserId(token);
		voteService.doMatchPogVote(request.toDto(kakaoUserId));
		return ResponseEntity.ok()
			.body(ApiResponse.createSuccessWithNoContent());
	}

	@Operation(summary = "set-pog 투표 후보 반환 API", description = "각 세트의 set-pog 투표 후보를 반환합니다")
	@GetMapping("/set-pog/candidates")
	public ResponseEntity<ApiResponse<SetVoteDto.SetPogVoteCandidateResponse>> getCandidateSetPogVote(
		@RequestHeader("Authorization") String token,
		@RequestParam("match-id") Long matchId,
		@RequestParam("set-index") Integer setIndex) {
		String kakaoUserId = authService.getKakaoUserId(token);
		SetVoteDto.SetPogVoteCandidateResponse response = voteService.getSetPogCandidate(
			new SetVoteDto.VoteCandidateDto(kakaoUserId, matchId, setIndex));
		return ResponseEntity.ok().body(ApiResponse.createSuccess(response));
	}

	@Operation(summary = "set-pog 투표 API", description = "set-pog 투표를 진행합니다")
	@PostMapping("set-pog/making")
	public ResponseEntity<ApiResponse<Void>> makeSetPogVote(
		@RequestHeader("Authorization") String token,
		@RequestBody SetVoteDto.SetPogVoteRequest request) {
		String kakaoUserId = authService.getKakaoUserId(token);
		voteService.doSetPogVote(request.toDto(kakaoUserId));
		return ResponseEntity.ok()
			.body(ApiResponse.createSuccessWithNoContent());
	}

}
