package com.lckback.lckforall.aboutlck.converter;

import java.util.List;

import com.lckback.lckforall.aboutlck.dto.AboutMatchControllerDto;
import com.lckback.lckforall.base.type.MatchResult;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.team.model.Team;

public class AboutMatchConverter {

	public static AboutMatchControllerDto.Response convertToAboutMatchResponse(List<Match> matchList) {
		List<AboutMatchControllerDto.MatchDetail> matchDetailList = convertToMatchDetailList(matchList);
		return AboutMatchControllerDto.Response.builder()
			.matchDetailList(matchDetailList)
			.listSize(matchDetailList.size())
			.build();
	}

	private static List<AboutMatchControllerDto.MatchDetail> convertToMatchDetailList(List<Match> matchList) {
		return matchList.stream().map(match -> {
			MatchResult matchResult = match.getMatchResult();
			return AboutMatchControllerDto.MatchDetail.builder()
				.team1(convertToTeamDetail(match.getTeam1(), matchResult == MatchResult.TEAM1_WIN))
				.team2(convertToTeamDetail(match.getTeam2(), matchResult == MatchResult.TEAM2_WIN))
				.matchFinished(matchResult != MatchResult.NOT_FINISHED)
				.matchNumber(match.getMatchNumber())
				.matchDate(match.getMatchDate().toLocalDate())
				.season(match.getTeam1().getSeason())
				.build();
		}).toList();
	}

	private static AboutMatchControllerDto.TeamDetail convertToTeamDetail(Team team, boolean isWinner) {
		return AboutMatchControllerDto.TeamDetail.builder()
			.teamName(team.getTeamName())
			.teamLogoUrl(team.getTeamLogoUrl())
			.isWinner(isWinner)
			.build();
	}
}
