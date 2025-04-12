package org.sopt.homework.exception;

public enum ExceptionMessage {
	INVALID_TITLE("❌ 올바른 제목을 입력해주세요"),
	EXIST_TITLE("❌ 이미 존재하는 제목입니다"),
	CREATION_TIME_LIMIT("❌ 아직 게시글을 작성할 수 없습니다"),
	FILE_ACCESS_ERROR("❌ 파일 입출력 중 오류가 발생했습니다");

	private final String message;

	ExceptionMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
