package org.sopt.homework.util.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorManager {
	// 파일입출력을 순차적으로 처리하기 위해 singleThread사용
	private static final ExecutorService executor = Executors.newSingleThreadExecutor();

	private ExecutorManager() {
	}    // 싱글톤 사용을 위해 인스턴스화 방지

	// 스레드 인스턴스 반환
	public static ExecutorService getExecutor() {
		return executor;
	}

	// 스레드풀 삭제
	public static void shutdown() {
		executor.shutdown();
	}
}
