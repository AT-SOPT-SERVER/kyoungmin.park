package org.sopt.homework.global.exception.message;

import org.springframework.http.HttpStatus;

public enum ExceptionMessage {
	EMPTY_TITLE(HttpStatus.BAD_REQUEST, "c4001", "제목이 비어있습니다."),
	OVER_LENGTH_TITLE(HttpStatus.BAD_REQUEST, "c4002", "제목은 30글자를 넘을 수 없습니다."),
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "c4050", "잘못된 HTTP method 요청입니다."),
	EXIST_TITLE(HttpStatus.CONFLICT, "c4090", "이미 존재하는 게시글입니다."),
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "c4041", "존재하지 않는 게시물입니다."),
	CREATION_TIME_LIMIT(HttpStatus.TOO_MANY_REQUESTS, "c4291", "아직 게시글을 작성할 수 없습니다"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "s5000", "서버 내부 오류입니다.");

	private final HttpStatus httpStatus;
	private final String subCode;
	private final String message;

	ExceptionMessage(HttpStatus httpStatus, String subCode, String message) {
		this.httpStatus = httpStatus;
		this.subCode = subCode;
		this.message = message;
	}

	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}

	public String getSubCode() {
		return this.subCode;
	}

	public String getMessage() {
		return this.message;
	}
}
