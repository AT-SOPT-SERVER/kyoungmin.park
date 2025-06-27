package org.sopt.homework.global.exception;

import org.sopt.homework.global.exception.message.ExceptionMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GlobalException extends RuntimeException {
	private final ExceptionMessage exceptionMessage;
}
