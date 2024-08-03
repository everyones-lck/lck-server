package com.lckback.lckforall.vote.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lckback.lckforall.base.api.ApiResponse;
import com.lckback.lckforall.vote.dto.MatchVoteDto;
import com.lckback.lckforall.vote.service.VoteService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Tag(name = "Vote", description = "Today's Match 투표 관련 api")
@RestController
@RequestMapping("/votes")
public class VoteController {
	private final VoteService voteService;

	@GetMapping("/candidates")
	public ResponseEntity<?> getCandidateMatchVote(
		@RequestHeader(name = "userId") Long userId, @RequestParam("match-id") Long matchId) {
		MatchVoteDto.VoteCandidateResponse response = voteService.getCandidate(
			new MatchVoteDto.VoteCandidateDto(userId, matchId));
		return ResponseEntity.ok()
			.body(ApiResponse.createSuccess(response));
	}

	@PostMapping("/making")
	public ResponseEntity<?> makeVote(
		@RequestHeader(name = "userId") Long userId, @RequestBody MatchVoteDto.MakeVoteRequest request) {
		voteService.doVote(request.toDto(userId));
		return ResponseEntity.ok()
			.body(ApiResponse.createSuccessWithNoContent());
	}
}
