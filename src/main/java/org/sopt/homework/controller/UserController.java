package org.sopt.homework.controller;

import java.util.UUID;

import org.sopt.homework.dto.user.request.SigninRequest;
import org.sopt.homework.dto.user.request.UserSignupRequest;
import org.sopt.homework.dto.user.response.TokenResponse;
import org.sopt.homework.global.api.message.ResponseMessage;
import org.sopt.homework.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@PostMapping(path = "/signup")
	public ResponseMessage create(@RequestBody final UserSignupRequest userSignupRequest) {
		return userService.createUser(userSignupRequest);
	}

	@PostMapping(path = "/login")
	public TokenResponse login(@RequestBody final SigninRequest signinRequest) {
		return userService.login(signinRequest);
	}

	@PostMapping(path = "/logout")
	public ResponseMessage logout(
			@RequestHeader(name = "Authorization") final String accessTokenWithBearer,
			@CookieValue(name = "Refresh") final String refreshToken,
			HttpServletResponse response
	) {
		return userService.logout(accessTokenWithBearer, refreshToken, response);
	}

	@PostMapping(path = "/reissue")
	public TokenResponse reissue(@CookieValue(name = "Refresh") final String refreshToken) {
		return userService.reissueToken(refreshToken);
	}

	@DeleteMapping
	public ResponseMessage unregister(
			@AuthenticationPrincipal final UUID userId,
			@RequestHeader(name = "Authorization") final String accessTokenWithBearer,
			@CookieValue(name = "Refresh") final String refreshToken,
			HttpServletResponse response) {
		return userService.deleteUser(userId, accessTokenWithBearer, refreshToken, response);
	}
}
