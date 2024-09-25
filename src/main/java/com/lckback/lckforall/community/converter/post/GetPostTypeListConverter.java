package com.lckback.lckforall.community.converter.post;

import java.util.List;

import com.lckback.lckforall.community.dto.post.GetPostTypeListDto;
import com.lckback.lckforall.community.model.PostType;

public class GetPostTypeListConverter {

	public static GetPostTypeListDto.Response convertResponse(List<PostType> postTypes) {
		List<String> postTypeList = postTypes.stream().map(PostType::getType).toList();
		return GetPostTypeListDto.Response.builder()
			.postTypeList(postTypeList)
			.listSize(postTypeList.size())
			.build();
	}
}
