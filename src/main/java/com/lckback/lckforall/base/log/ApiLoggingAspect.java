package com.lckback.lckforall.base.log;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lckback.lckforall.base.api.exception.RestApiException;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class ApiLoggingAspect {

	private final ObjectMapper objectMapper = new ObjectMapper();

	// 해당 annotation이 붙은 method에 대해 pointcut 설정
	@Pointcut("execution(* com.lckback.lckforall..*Controller.*(..)) && "
		+ "@annotation(org.springframework.web.bind.annotation.GetMapping) || "
		+ "@annotation(org.springframework.web.bind.annotation.PostMapping) || "
		+ "@annotation(org.springframework.web.bind.annotation.PutMapping) || "
		+ "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || "
		+ "@annotation(org.springframework.web.bind.annotation.PatchMapping) || "
		+ "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void controllerLogPointCut() {
	}

	@Around("controllerLogPointCut()")
	public Object queryCountLogging(ProceedingJoinPoint joinPoint) throws Throwable {
		objectMapper.registerModule(new JavaTimeModule());
		// target class 추출
		Class<?> clazz = joinPoint.getTarget().getClass();

		// target class logger 생성
		Logger logger = LoggerFactory.getLogger(clazz);
		String baseLog = getBaseLog(joinPoint, clazz);

		// 파라미터 바인딩 추출
		Map<String, Object> params = getParameterBindings(joinPoint);

		// return object 설정
		Object ret = null;

		long start = System.currentTimeMillis();
		try {
			// 실행 전 파라미터 바인딩 로깅처리
			logger.info("{} : Params = {}", baseLog, objectMapper.writeValueAsString(params));
			// target method 실행
			ret = joinPoint.proceed();
		} catch (Exception e) {
			// Exception 발생 시 로깅처리
			if (e instanceof RestApiException)
				logger.error("{} : Exception = {}", baseLog, objectMapper.writeValueAsString(((RestApiException)e).getErrorCode()));
			throw e;
		} finally {
			// 실행 후 리턴값 로깅처리
			logger.info("{} : Response = {}", baseLog, objectMapper.writeValueAsString(ret));
			// target method 실행 후 로깅처리
			logger.info("{} : Execution time = {}ms, Query Count = {}", baseLog, System.currentTimeMillis() - start,
				QueryCountInspector.getQueryCount());
			// query count 초기화
			QueryCountInspector.clear();
		}
		return ret;
	}

	private Map<String, Object> getParameterBindings(ProceedingJoinPoint joinPoint) {
		CodeSignature codeSignature = (CodeSignature)joinPoint.getSignature();
		// parameter 이름 추출
		String[] parameterNames = codeSignature.getParameterNames();
		// parameter 값 추출
		Object[] args = joinPoint.getArgs();

		Map<String, Object> params = new HashMap<>();

		// parameter 이름과 값 매핑
		for (int i = 0; i < parameterNames.length; i++) {
			if (args[i] instanceof MultipartFile) {
				params.put(parameterNames[i], ((MultipartFile)args[i]).getOriginalFilename());
				continue;
			}
			params.put(parameterNames[i], args[i]);
		}
		return params;
	}

	private String getBaseLog(ProceedingJoinPoint joinPoint, Class clazz) {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		// reflection -> method 정보 추출
		Method method = signature.getMethod();

		// baseUrl 추출
		RequestMapping requestMapping = (RequestMapping)clazz.getAnnotation(RequestMapping.class);
		if (requestMapping == null) {
			return null;
		}
		String baseUrl = requestMapping.value()[0];

		// baseLogUrl 추출
		return Stream.of(RequestMapping.class, GetMapping.class, PostMapping.class, PutMapping.class,
				DeleteMapping.class,
				PatchMapping.class)
			.filter(method::isAnnotationPresent)
			.map(annotation -> getRequestMethod(method, annotation, baseUrl))
			.findFirst().orElse(null);
	}

	private String getRequestMethod(Method method, Class<? extends Annotation> mappingClass, String baseUrl) {
		Annotation a = method.getAnnotation(mappingClass);
		String[] value;
		String httpMethod;
		try {
			// annotation의 value 값 추출
			value = (String[])mappingClass.getMethod("value").invoke(a);
			// annotation의 httpMethod 추출 (GETMapping -> GET)
			httpMethod = (mappingClass.getSimpleName().replace("Mapping", "")).toUpperCase();
		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		value = value == null || value.length == 0 ? new String[]{""} : value;
		// 로그 포맷으로 출력 (GET /test/hello)
		return String.format("%s %s%s", httpMethod, baseUrl, value[0]);
	}
}
