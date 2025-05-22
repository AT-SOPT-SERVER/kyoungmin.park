package org.sopt.homework.global.api.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class ApiResponse {
	private final String code;
	private final String message;
}
