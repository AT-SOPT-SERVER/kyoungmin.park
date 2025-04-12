package org.sopt.homework.repository.util.generator;

import java.util.Set;

public class Generator {
	// 메모리에 존재하는 키값을 조회하고 키를 결정
	public static Long generateId(Set<Long> keys) {
		return keys.stream().max(Long::compareTo).orElse(0L) + 1;
	}
}
