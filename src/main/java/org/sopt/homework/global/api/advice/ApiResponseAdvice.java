package org.sopt.homework.global.api.advice;

import org.sopt.homework.global.api.message.ResponseMessage;
import org.sopt.homework.global.api.response.ErrorResponse;
import org.sopt.homework.global.api.response.SuccessfulResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		// 모든 응답에 대해 응답 생성 전 형태 변환 적용
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
		Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
		ServerHttpResponse response) {

		// 예외처리된 응답에 대한 상태코드 설정 및 apiResponse wrapping 생략
		if (body instanceof ErrorResponse errorResponse) {
			return body;
		}

		// 이미 응답 형태로 감싸진 경우 바로 body를 반환
		if (body instanceof SuccessfulResponse) {
			return body;
		}

		// 반환 형태가 ResponseMessage인 경우 해당되는 응답 코드 적용 및 형태 적용
		if (body instanceof ResponseMessage responseMessage) {
			response.setStatusCode(responseMessage.getHttpStatus());
			return SuccessfulResponse.of(responseMessage, null);
		}

		return SuccessfulResponse.of(ResponseMessage.SUCCESS, body);
	}
}
