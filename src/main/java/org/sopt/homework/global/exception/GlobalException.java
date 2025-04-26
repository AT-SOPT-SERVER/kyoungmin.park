package org.sopt.homework.global.exception;

import org.sopt.homework.global.exception.message.ExceptionMessage;

public class GlobalException extends RuntimeException {
	private final ExceptionMessage exceptionMessage;

	public GlobalException(ExceptionMessage exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public ExceptionMessage getExceptionMessage() {
		return this.exceptionMessage;
	}
}
