package com.lckback.lckforall.base.config;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;

@Getter
public class CustomAuthenticationToken extends AbstractAuthenticationToken {

	private final Object principal;
	private Object credentials;
	private String role;

	// 사용자 인증 전
	public CustomAuthenticationToken(Object principal, Object credentials, String role) {
		super(null);
		this.principal = principal;
		this.credentials = credentials;
		this.role = role;
		setAuthenticated(false);
	}

	// 사용자 인증 후
	public CustomAuthenticationToken(Object principal, Object credentials, String role, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		this.role = role;
		super.setAuthenticated(true);
	}

}
