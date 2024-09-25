package com.lckback.lckforall.base.log;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;

@Component
public class QueryCountInspector implements StatementInspector {

	// ThreadLocal을 사용하여 각 쓰레드별로 queryCount를 관리 -> 동시성 이슈 해결
	private static final ThreadLocal<Integer> queryCount = ThreadLocal.withInitial(() -> 0);

	// query 후처리 메서드에 해당 로직을 삽입
	@Override
	public String inspect(String s) {
		queryCount.set(queryCount.get() + 1);
		return s;
	}

	// queryCount 초기화
	public static void clear(){
		queryCount.set(0);
	}

	// queryCount 조회
	public static Integer getQueryCount() {
		return queryCount.get();
	}
}
