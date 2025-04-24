package org.sopt.homework.global.api.advice;

import org.sopt.homework.global.api.response.ErrorResponse;
import org.sopt.homework.global.exception.GlobalException;
import org.sopt.homework.global.exception.message.ExceptionMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionAdvice {
	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<ErrorResponse> handleInvalidTitleException(GlobalException e) {
		ExceptionMessage exceptionMessage = e.getExceptionMessage();

		return ResponseEntity
			.status(exceptionMessage.getHttpStatus())
			.body(ErrorResponse.of(e.getExceptionMessage()));
	}

	// 정의되지 않은 예외에 대해 500을 반환
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		ExceptionMessage exceptionMessage = ExceptionMessage.INTERNAL_SERVER_ERROR;

		return ResponseEntity
			.status(exceptionMessage.getHttpStatus())
			.body(ErrorResponse.of(exceptionMessage));
	}
}
