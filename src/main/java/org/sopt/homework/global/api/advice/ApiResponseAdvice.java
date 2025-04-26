package org.sopt.homework.global.api.advice;

import org.sopt.homework.dto.PostResponse;
import org.sopt.homework.global.api.message.ResponseMessage;
import org.sopt.homework.global.api.response.ErrorResponse;
import org.sopt.homework.global.api.response.SuccessfulResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
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

		// 삭제 요청에 대한 응답 상태 코드 설정
		if (request.getMethod().equals(HttpMethod.DELETE)) {
			response.setStatusCode(ResponseMessage.DELETED.getHttpStatus());
			return SuccessfulResponse.of(ResponseMessage.DELETED, body);
		}

		// 생성 요청에 대한 응답 상태 코드 설정
		if (request.getMethod().equals(HttpMethod.POST) && returnType.getParameterType().equals(PostResponse.class)) {
			response.setStatusCode(ResponseMessage.CREATED.getHttpStatus());
			return SuccessfulResponse.of(ResponseMessage.CREATED, body);
		}

		return SuccessfulResponse.of(ResponseMessage.SUCCESS, body);
	}
}
