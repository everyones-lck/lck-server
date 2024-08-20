package com.lckback.lckforall.aboutlck.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lckback.lckforall.aboutlck.converter.team.FindTeamPlayerHistoryConverter;
import com.lckback.lckforall.aboutlck.converter.team.FindTeamPlayerInformationConverter;
import com.lckback.lckforall.aboutlck.converter.team.FindTeamRatingBySeasonConverter;
import com.lckback.lckforall.aboutlck.converter.team.FindTeamRatingHistoryConverter;
import com.lckback.lckforall.aboutlck.converter.team.FindTeamWinningHistoryConverter;
import com.lckback.lckforall.aboutlck.dto.team.FindTeamPlayerHistoryDto;
import com.lckback.lckforall.aboutlck.dto.team.FindTeamPlayerInformationDto;
import com.lckback.lckforall.aboutlck.dto.team.FindTeamRatingHistoryDto;
import com.lckback.lckforall.aboutlck.dto.team.FindTeamRatingBySeasonDto;
import com.lckback.lckforall.aboutlck.dto.team.FindTeamWinningHistoryDto;
import com.lckback.lckforall.base.api.error.SeasonErrorCode;
import com.lckback.lckforall.base.api.error.SeasonTeamErrorCode;
import com.lckback.lckforall.base.api.error.TeamErrorCode;
import com.lckback.lckforall.player.model.SeasonTeamPlayer;
import com.lckback.lckforall.player.repository.SeasonTeamPlayerRepository;
import com.lckback.lckforall.team.repository.SeasonRepository;
import com.lckback.lckforall.team.repository.SeasonTeamRepository;
import com.lckback.lckforall.team.repository.TeamRepository;
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
	private final SeasonTeamPlayerRepository seasonTeamPlayerRepository;

	public FindTeamRatingBySeasonDto.Response findTeamRatingBySeason(FindTeamRatingBySeasonDto.Parameter param) {
		Season season = seasonRepository.findByName(param.getSeasonName())
			.orElseThrow(() -> new RestApiException(SeasonErrorCode.NOT_EXIST_SEASON));

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

	public FindTeamPlayerInformationDto.Response findTeamPlayerInformation(
		FindTeamPlayerInformationDto.Parameter param) {
		Team team = findTeamByTeamId(param.getTeamId());
		Season season = findSeasonBySeasonName(param.getSeasonName());

		SeasonTeam seasonTeam = seasonTeamRepository.findBySeasonAndTeam(season, team)
			.orElseThrow(() -> new RestApiException(SeasonTeamErrorCode.NOT_EXIST_SEASON_TEAM));

		List<SeasonTeamPlayer> seasonTeamPlayers = seasonTeamPlayerRepository.findAllBySeasonTeam(seasonTeam)
			.stream()
			.filter(seasonTeamPlayer -> seasonTeamPlayer.getPlayer().getRole().equals(param.getPlayerRole()))
			.toList();

		return FindTeamPlayerInformationConverter.convertToResponse(seasonTeamPlayers);
	}

	private Team findTeamByTeamId(Long teamId) {
		return teamRepository.findById(teamId)
			.orElseThrow(() -> new RestApiException(TeamErrorCode.NOT_EXISTS_TEAM));
	}

	private Season findSeasonBySeasonName(String seasonName) {
		return seasonRepository.findByName(seasonName)
			.orElseThrow(() -> new RestApiException(SeasonErrorCode.NOT_EXIST_SEASON));
	}

	private PageRequest createPageRequestSortBySeasonName(Pageable pageable) {
		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() * 2,
			Sort.by("season.name").descending());
	}
}
