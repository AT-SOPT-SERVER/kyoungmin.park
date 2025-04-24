package org.sopt.homework.global.api.response;

import org.sopt.homework.global.api.message.ResponseMessage;

public class SuccessfulResponse<T> extends ApiResponse {
	private final T data;

	public SuccessfulResponse(String code, String message, T data) {
		super(code, message);
		this.data = data;
	}

	public static <T> SuccessfulResponse<T> of(ResponseMessage responseMessage, T data) {
		return new SuccessfulResponse<T>(responseMessage.getSubCode(), responseMessage.getMessage(), data);
	}

	public T getData() {
		return this.data;
	}
}
