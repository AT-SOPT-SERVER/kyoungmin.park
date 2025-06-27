package org.sopt.homework.global.security.util.constant;

import java.util.Arrays;

import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WhiteList {
	SIGNUP("/users/signup", HttpMethod.POST),
	LOGIN("/users/login", HttpMethod.POST),
	REISSUE("/users/reissue", HttpMethod.POST),
	READ_ALL_POSTS("/posts/**", HttpMethod.GET),
	READ_POST_DETAIL("/posts/detail/**", HttpMethod.GET),
	SEARCH_POSTS("/posts/search/**", HttpMethod.GET),
	READ_COMMENTS("/posts/**/comments/**", HttpMethod.GET);

	private static final AntPathMatcher pathMatcher = new AntPathMatcher();
	private final String path;
	private final HttpMethod method;

	public static boolean isPermitted(String requestPath, HttpMethod requestMethod) {
		return Arrays.stream(values())
				.anyMatch(entry -> pathMatcher.match(entry.path, requestPath)
						&& entry.method == requestMethod);
	}
}
