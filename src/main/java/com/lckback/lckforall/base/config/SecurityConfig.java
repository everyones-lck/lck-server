package com.lckback.lckforall.base.config;

import com.lckback.lckforall.base.auth.jwt.JWTFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

	// 계층 권한 설정
	@Bean
	public RoleHierarchy roleHierarchy() {

		return RoleHierarchyImpl.fromHierarchy("""
         ROLE_ADMIN > ROLE_USER
         """);
	}

	private final JWTFilter jwtFilter;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// csrf disable
		http
			.csrf((auth) -> auth.disable());

		// From 로그인 방식 disable
		http
			.formLogin((auth) -> auth.disable());

		// HTTP Basic 인증 방식 disable
		http
			.httpBasic((auth) -> auth.disable());

		// 경로별 인가 작업
		http
			.authorizeHttpRequests((auth) -> auth
				.requestMatchers("/auth/login", "/auth/signup", "/auth/refresh", "/auth/nickname", "/my-pages/logout").permitAll() // 인증 없이 접근 가능
				.requestMatchers(HttpMethod.GET,
					"/swagger-ui/*", "/v3/api-docs/swagger-config", "/api/logistics", "/v3/api-docs").permitAll()
				.requestMatchers("/auth/users/**").hasRole("USER")
				.requestMatchers("/auth/admin/**").hasRole("ADMIN")
					.requestMatchers("/ws/**").permitAll() // 웹소켓 허용
				.anyRequest().authenticated()); // 나머지 경로는 인증 필요

		// 세션 설정 : STATELESS
		http
			.sessionManagement((session) -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		http
			.exceptionHandling((handler) -> handler
				.authenticationEntryPoint(customAuthenticationEntryPoint)
				.accessDeniedHandler(customAccessDeniedHandler));

		return http.build();
	}
}
