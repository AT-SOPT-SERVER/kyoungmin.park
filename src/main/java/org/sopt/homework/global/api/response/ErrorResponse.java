package org.sopt.homework.global.api.response;

import org.sopt.homework.global.exception.message.ExceptionMessage;

public class ErrorResponse extends ApiResponse {
	public ErrorResponse(String code, String message) {
		super(code, message);
	}

	public static ErrorResponse of(ExceptionMessage exceptionMessage) {
		return new ErrorResponse(exceptionMessage.getSubCode(), exceptionMessage.getMessage());
	}
}
