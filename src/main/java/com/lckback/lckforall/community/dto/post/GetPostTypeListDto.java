package com.lckback.lckforall.community.dto.post;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

public class GetPostTypeListDto {

	@Getter
	@Builder
	public static class Response {
		private List<String> postTypeList;
		private Integer listSize;
	}
}
