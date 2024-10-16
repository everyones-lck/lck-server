package com.lckback.lckforall.aboutlck.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lckback.lckforall.aboutlck.converter.player.FindPlayerTeamHistoryConverter;
import com.lckback.lckforall.aboutlck.converter.player.FindPlayerWinningHistoryConverter;
import com.lckback.lckforall.aboutlck.dto.player.FindPlayerInformationDto;
import com.lckback.lckforall.aboutlck.dto.player.FindPlayerTeamHistoryDto;
import com.lckback.lckforall.aboutlck.dto.player.FindPlayerWinningHistoryDto;
import com.lckback.lckforall.base.api.error.PlayerErrorCode;
import com.lckback.lckforall.base.api.error.SeasonErrorCode;
import com.lckback.lckforall.player.repository.SeasonTeamPlayerRepository;
import com.lckback.lckforall.team.model.Season;
import com.lckback.lckforall.team.repository.SeasonRepository;
import com.lckback.lckforall.team.repository.SeasonTeamRepository;
import com.lckback.lckforall.base.api.exception.RestApiException;
import com.lckback.lckforall.player.model.Player;
import com.lckback.lckforall.player.model.SeasonTeamPlayer;
import com.lckback.lckforall.player.repository.PlayerRepository;
import com.lckback.lckforall.team.model.SeasonTeam;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AboutLckPlayerService {

	private final PlayerRepository playerRepository;
	private final SeasonRepository seasonRepository;
	private final SeasonTeamRepository seasonTeamRepository;
	private final SeasonTeamPlayerRepository seasonTeamPlayerRepository;

	public FindPlayerInformationDto.Response findPlayerInformation(FindPlayerInformationDto.Parameter param) {
		Player player = findPlayerByPlayerId(param.getPlayerId());

		return FindPlayerInformationDto.Response.builder()
			.nickName(player.getName())
			.realName(player.getRealName())
			.playerProfileImageUrl(player.getProfileImageUrl())
			.position(player.getPosition())
			.birthDate(player.getBirth())
			.build();
	}

	public FindPlayerTeamHistoryDto.Response findPlayerTeamHistory(FindPlayerTeamHistoryDto.Parameter param) {
		Player player = findPlayerByPlayerId(param.getPlayerId());

		List<SeasonTeamPlayer> seasonTeamPlayers = seasonTeamPlayerRepository.findAllByPlayer(player);

		PageRequest pageRequest = createPageRequestSortBySeasonName(param.getPageable());
		
		Page<SeasonTeam> seasonTeams = seasonTeamRepository.findAllBySeasonTeamPlayersIn(seasonTeamPlayers,
			pageRequest);

		return FindPlayerTeamHistoryConverter.toResponse(seasonTeams);
	}

	public FindPlayerWinningHistoryDto.Response findPlayerWinningHistory(
		FindPlayerWinningHistoryDto.Parameter parameter) {
		Player player = findPlayerByPlayerId(parameter.getPlayerId());

		Season currentSeason = seasonRepository.findByName("2024 Summer")
			.orElseThrow(() -> new RestApiException(SeasonErrorCode.NOT_EXIST_SEASON));

		List<SeasonTeamPlayer> seasonTeamPlayers = seasonTeamPlayerRepository.findAllByPlayer(player);

		PageRequest pageRequest = createPageRequestSortBySeasonName(parameter.getPageable());

		Page<SeasonTeam> seasonTeams = seasonTeamRepository.findWinningSeasonTeamBySeasonTeamPlayers(
			seasonTeamPlayers, currentSeason, pageRequest);

		return FindPlayerWinningHistoryConverter.toResponse(seasonTeams);
	}

	private Player findPlayerByPlayerId(Long playerId) {
		return playerRepository.findById(playerId)
			.orElseThrow(() -> new RestApiException(PlayerErrorCode.NOT_EXIST_PLAYER));
	}

	private PageRequest createPageRequestSortBySeasonName(Pageable pageable) {
		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("season.name").descending());
	}
}
