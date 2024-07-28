package com.lckback.lckforall.aboutlck.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lckback.lckforall.aboutlck.converter.AboutMatchConverter;
import com.lckback.lckforall.aboutlck.dto.FindMatchesByDateDto;
import com.lckback.lckforall.aboutlck.repository.MatchRepository;
import com.lckback.lckforall.match.model.Match;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AboutLckMatchService {

	private final MatchRepository matchRepository;

	public FindMatchesByDateDto.Response aboutMatchesByDate(FindMatchesByDateDto.Parameter param) {
		LocalDate searchDate = param.getSearchDate();
		LocalDateTime start = searchDate.atStartOfDay();
		LocalDateTime end = searchDate.atTime(23, 59, 59);
		List<Match> matches = matchRepository.findMatchesByMatchDateBetween(start, end);
		return AboutMatchConverter.convertToAboutMatchResponse(matches);
	}
}
