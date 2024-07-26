package com.lckback.lckforall.aboutlck.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

public class AboutMatchServiceDto {

	@Getter
	@Builder
	public static class findMatchesByDate {
		private LocalDate searchDate;
	}
}
