package com.lckback.lckforall.aboutlck.converter.match;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.lckback.lckforall.aboutlck.dto.match.FindMatchesByDateDto;
import com.lckback.lckforall.base.type.MatchResult;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.team.model.Team;

public class AboutMatchConverter {

	public static FindMatchesByDateDto.Response convertToAboutMatchResponse(List<Match> matchList) {
		Map<LocalDate, List<Match>> localDateListMap = matchList.stream()
			.collect(Collectors.groupingBy(match -> match.getMatchDate().toLocalDate()));

		List<FindMatchesByDateDto.MatchByDate> matchByDateList = convertToMatchByDateList(localDateListMap);
		return FindMatchesByDateDto.Response.builder()
			.matchByDateList(matchByDateList)
			.build();
	}

	private static List<FindMatchesByDateDto.MatchByDate> convertToMatchByDateList(
		Map<LocalDate, List<Match>> localDateListMap) {
		return localDateListMap.entrySet()
			.stream()
			.map(entry -> FindMatchesByDateDto.MatchByDate.builder()
				.matchDetailList(convertToMatchDetailList(entry.getValue()))
				.matchDate(entry.getKey())
				.matchDetailSize(entry.getValue().size())
				.build()
			)
			.toList();
	}

	private static List<FindMatchesByDateDto.MatchDetail> convertToMatchDetailList(List<Match> matchList) {
		return matchList.stream().map(match -> {
			MatchResult matchResult = match.getMatchResult();
			return FindMatchesByDateDto.MatchDetail.builder()
				.team1(convertToTeamDetail(match.getTeam1(), matchResult == MatchResult.TEAM1_WIN))
				.team2(convertToTeamDetail(match.getTeam2(), matchResult == MatchResult.TEAM2_WIN))
				.matchFinished(matchResult != MatchResult.NOT_FINISHED)
				.matchNumber(match.getMatchNumber())
				.matchDate(match.getMatchDate().toLocalDate())
				.matchTime(match.getMatchDate().toLocalTime())
				.season(match.getSeason().getName())
				.build();
		}).toList();
	}

	private static FindMatchesByDateDto.TeamDetail convertToTeamDetail(Team team, boolean isWinner) {
		return FindMatchesByDateDto.TeamDetail.builder()
			.teamName(team.getTeamName())
			.teamLogoUrl(team.getTeamLogoUrl())
			.isWinner(isWinner)
			.build();
	}
}
