package com.lckback.lckforall.base.setting;

import java.util.List;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.context.annotation.Bean;

// authorization 버튼 추가
@io.swagger.v3.oas.annotations.security.SecurityScheme(
	name = "JWT Token",
	type = SecuritySchemeType.HTTP,
	scheme = "bearer",
	bearerFormat = "JWT",
	in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
	@Bean
	public OpenAPI LckAPI() {
		Info info = new Info()
			.title("모두의 LCK Server API")
			.description("모두의 LCK 명세서")
			.version("1.0.0");

		String jwtSchemeName = "JWT Token";
		// SecuritySchemes 생성
		SecurityScheme scheme = new SecurityScheme()
			.name(jwtSchemeName)
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.in(SecurityScheme.In.HEADER);

		// API 요청헤더에 인증정보 포함
		SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

		return new OpenAPI()
			.addServersItem(new Server().url("/"))
			.info(info)
			.components(new Components().addSecuritySchemes(jwtSchemeName, scheme))
			.security(List.of(securityRequirement));
	}
}
