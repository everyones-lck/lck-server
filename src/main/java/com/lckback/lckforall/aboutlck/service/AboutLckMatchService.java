package com.lckback.lckforall.aboutlck.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lckback.lckforall.aboutlck.converter.match.AboutMatchConverter;
import com.lckback.lckforall.aboutlck.dto.match.FindMatchesByDateDto;
import com.lckback.lckforall.match.model.Match;
import com.lckback.lckforall.match.repository.MatchRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AboutLckMatchService {

	private final MatchRepository matchRepository;

	public FindMatchesByDateDto.Response findMatchInformationByDate(FindMatchesByDateDto.Parameter param) {
		LocalDate searchDate = param.getSearchDate();
		LocalDateTime start = LocalDateTime.of(searchDate, LocalTime.of(0, 0, 0));
		LocalDateTime end = searchDate.atTime(23, 59, 59);
		List<Match> matches = matchRepository.findMatchesByMatchDateBetween(start, end);
		return AboutMatchConverter.convertToAboutMatchResponse(matches);
	}
}
