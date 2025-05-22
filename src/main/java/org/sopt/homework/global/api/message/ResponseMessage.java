package org.sopt.homework.global.api.message;

import org.springframework.http.HttpStatus;

public enum ResponseMessage {
	SUCCESS(HttpStatus.OK, "s2000", "요청이 성공했습니다."),
	POST_CREATED(HttpStatus.CREATED, "s2010", "게시글이 생성되었습니다."),
	USER_CREATED(HttpStatus.CREATED, "s2011", "회원가입이 완료되었습니다."),
	UPDATED(HttpStatus.OK, "s2001", "게시글이 수정되었습니다."),
	POST_DELETED(HttpStatus.OK, "s2002", "게시글이 삭제되었습니다."),
	USER_DELETED(HttpStatus.OK, "s2003", "회원탈퇴가 완료되었습니다.");

	private final HttpStatus httpStatus;
	private final String subCode;
	private final String message;

	ResponseMessage(HttpStatus httpStatus, String subCode, String message) {
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
