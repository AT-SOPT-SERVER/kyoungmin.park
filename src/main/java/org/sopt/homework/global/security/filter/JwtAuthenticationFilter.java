package org.sopt.homework.global.security.filter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.sopt.homework.global.api.response.ErrorResponse;
import org.sopt.homework.global.exception.message.ExceptionMessage;
import org.sopt.homework.global.security.util.constant.WhiteList;
import org.sopt.homework.global.security.util.jwt.JwtParser;
import org.sopt.homework.global.security.util.jwt.JwtValidator;
import org.sopt.homework.global.security.util.response.JsonResponseWriter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtParser jwtParser;
	private final JwtValidator jwtValidator;
	private final JsonResponseWriter jsonResponseWriter;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		String accessTokenWithBearer = request.getHeader("Authorization");

		// 접근이 허용된 경로에 대한 요청이 발생, 토큰이 없으면 jwt검증을 건너뛰고 다음 필터로 넘김
		if (WhiteList.isPermitted(request.getRequestURI(), HttpMethod.valueOf(request.getMethod()))
				&& accessTokenWithBearer == null) {
			filterChain.doFilter(request, response);
			return;
		}

		if (!jwtValidator.isValid(accessTokenWithBearer)) {
			jsonResponseWriter.writeJsonResponse(response, 406, ErrorResponse.of(ExceptionMessage.INVALID_TOKEN));
			return;
		}

		String accessToken = accessTokenWithBearer.split(" ")[1];

		registerAuthentication(accessToken);

		filterChain.doFilter(request, response);
	}

	private void registerAuthentication(String accessToken) {
		UUID userId = jwtParser.getUserId(accessToken);
		String role = jwtParser.getRole(accessToken);

		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
		Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);

		// contextHolder등록
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
