package com.lckback.lckforall.base.config;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lckback.lckforall.base.log.QueryCountInspector;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class HibernateInspectConfig {

	private final QueryCountInspector queryCountInspector;

	// HibernateInspector 에 queryCountLogging 등록
	@Bean
	public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
		return hibernateProperties -> hibernateProperties.put(AvailableSettings.STATEMENT_INSPECTOR,
				queryCountInspector);
	}
}
