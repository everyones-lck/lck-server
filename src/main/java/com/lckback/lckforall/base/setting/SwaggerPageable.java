package com.lckback.lckforall.base.setting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameters({
	@Parameter(
		in = ParameterIn.QUERY,
		name = "page",
		description = "페이지 번호",
		schema = @io.swagger.v3.oas.annotations.media.Schema(type = "integer", defaultValue = "0"),
		required = true
	),
	@Parameter(
		in = ParameterIn.QUERY,
		name = "size",
		description = "페이지 크기",
		schema = @io.swagger.v3.oas.annotations.media.Schema(type = "integer", defaultValue = "10"),
		required = true
	),
	@Parameter(
		name = "pageable",
		hidden = true
	)
})
public @interface SwaggerPageable {
}
