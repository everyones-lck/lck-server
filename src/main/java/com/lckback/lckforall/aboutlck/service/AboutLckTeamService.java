package com.lckback.lckforall.aboutlck.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lckback.lckforall.aboutlck.converter.team.FindTeamPlayerHistoryConverter;
import com.lckback.lckforall.aboutlck.converter.team.FindTeamRatingBySeasonConverter;
import com.lckback.lckforall.aboutlck.converter.team.FindTeamRatingHistoryConverter;
import com.lckback.lckforall.aboutlck.converter.team.FindTeamWinningHistoryConverter;
import com.lckback.lckforall.aboutlck.dto.team.FindTeamPlayerHistoryDto;
import com.lckback.lckforall.aboutlck.dto.team.FindTeamRatingHistoryDto;
import com.lckback.lckforall.aboutlck.dto.team.FindTeamRatingBySeasonDto;
import com.lckback.lckforall.aboutlck.dto.team.FindTeamWinningHistoryDto;
import com.lckback.lckforall.team.repository.SeasonRepository;
import com.lckback.lckforall.team.repository.SeasonTeamRepository;
import com.lckback.lckforall.team.repository.TeamRepository;
import com.lckback.lckforall.base.api.error.CommonErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.team.model.Season;
import com.lckback.lckforall.team.model.SeasonTeam;
import com.lckback.lckforall.team.model.Team;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AboutLckTeamService {

	private final TeamRepository teamRepository;
	private final SeasonRepository seasonRepository;
	private final SeasonTeamRepository seasonTeamRepository;

	public FindTeamRatingBySeasonDto.Response findTeamRatingBySeason(FindTeamRatingBySeasonDto.Parameter param) {
		Season season = seasonRepository.findByName(param.getSeasonName())
			.orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));

		Page<SeasonTeam> seasonTeamList = seasonTeamRepository.findAllBySeasonOrderByRatingAsc(season,
			param.getPageable());

		return FindTeamRatingBySeasonConverter.convertToResponse(seasonTeamList);
	}


	public FindTeamWinningHistoryDto.Response findTeamWinningHistory(FindTeamWinningHistoryDto.Parameter param) {
		Team team = findTeamByTeamId(param.getTeamId());

		PageRequest pageRequest = createPageRequestSortBySeasonName(param.getPageable());

		Page<SeasonTeam> seasonTeams = seasonTeamRepository.findAllByTeamAndRating(team, 1,
			pageRequest);

		return FindTeamWinningHistoryConverter.convertToResponse(seasonTeams);
	}

	public FindTeamRatingHistoryDto.Response findTeamRatingHistory(FindTeamRatingHistoryDto.Parameter param) {
		Team team = findTeamByTeamId(param.getTeamId());

		PageRequest pageRequest = createPageRequestSortBySeasonName(param.getPageable());
		Page<SeasonTeam> seasonTeams = seasonTeamRepository.findAllByTeam(team, pageRequest);

		return FindTeamRatingHistoryConverter.convertToResponse(seasonTeams);
	}

	public FindTeamPlayerHistoryDto.Response findTeamPlayerHistory(FindTeamPlayerHistoryDto.Parameter param) {
		Team team = findTeamByTeamId(param.getTeamId());

		PageRequest pageRequest = createPageRequestSortBySeasonName(param.getPageable());
		Page<SeasonTeam> seasonTeams = seasonTeamRepository.findAllByTeam(team, pageRequest);

		return FindTeamPlayerHistoryConverter.convertToResponse(seasonTeams);
	}

	// 에러코드 구체화 필요
	private Team findTeamByTeamId(Long teamId) {
		return teamRepository.findById(teamId)
			.orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));
	}

	private PageRequest createPageRequestSortBySeasonName(Pageable pageable) {
		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("season.name").descending());
	}
}
