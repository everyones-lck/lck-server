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
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AboutLckMatchService {

	private final MatchRepository matchRepository;

	public FindMatchesByDateDto.Response findMatchInformationByDate(FindMatchesByDateDto.Parameter param) {
		LocalDate searchDate = param.getSearchDate();
		LocalDateTime start = LocalDateTime.of(searchDate.minusDays(1), LocalTime.of(0, 0, 0));
		LocalDateTime end = LocalDateTime.of(searchDate.plusDays(1), LocalTime.of(23, 59, 59));

		log.info("start: {}, end: {}", start, end);
		List<Match> matches = matchRepository.findMatchesByMatchDateBetween(start, end);
		return AboutMatchConverter.convertToAboutMatchResponse(matches);
	}
}
