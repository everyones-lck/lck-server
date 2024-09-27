package com.lckback.lckforall.base.log;

import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;

public class ControllerLoggingDto {

	@Getter
	@Builder
	public static class Response {
		private String httpMethod;
		private HttpStatus httpStatus;
		private String requestUri;
		private String classPath;
		private String methodName;
		private Map<String, Object> parameter;
		private Object result;
		private Long elapsedTime;
	}
}
