package org.sopt.homework.global.api.advice;

import org.sopt.homework.dto.user.response.TokenResponse;
import org.sopt.homework.global.api.message.ResponseMessage;
import org.sopt.homework.global.api.response.ErrorResponse;
import org.sopt.homework.global.api.response.SuccessfulResponse;
import org.sopt.homework.global.common.util.CookieUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
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

		// 예외처리된 응답이나 이미 응답 형태로 감싸진 경우에 대한 상태코드 설정 및 apiResponse wrapping 생략
		if (body instanceof ErrorResponse || body instanceof SuccessfulResponse) {
			return body;
		}

		// 반환 형태가 ResponseMessage인 경우 해당되는 응답 코드 적용 및 형태 적용
		if (body instanceof ResponseMessage responseMessage) {
			response.setStatusCode(responseMessage.getHttpStatus());
			return SuccessfulResponse.of(responseMessage, null);
		}

		// 로그인 성공시 반환되는 토큰에 대한 응답 처리
		if (body instanceof TokenResponse tokenResponse) {
			response.setStatusCode(HttpStatus.OK);

			// 헤더에 access토큰 추가
			response.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.accessToken());

			// 쿠키에 refresh토큰 추가
			((ServletServerHttpResponse)response).getServletResponse()
					.addCookie(
							CookieUtil.createCookie("Refresh", tokenResponse.refreshToken(), 7 * 24 * 60 * 60));

			return SuccessfulResponse.of(ResponseMessage.LOGIN_SUCCESS, null);
		}

		return SuccessfulResponse.of(ResponseMessage.SUCCESS, body);
	}
}
