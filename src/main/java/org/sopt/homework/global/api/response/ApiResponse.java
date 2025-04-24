package org.sopt.homework.global.api.response;

public abstract class ApiResponse {
	private final String code;
	private final String message;

	public ApiResponse(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}
}
