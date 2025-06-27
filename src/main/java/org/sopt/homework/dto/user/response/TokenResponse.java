package org.sopt.homework.dto.user.response;

public record TokenResponse(
		String accessToken,
		String refreshToken
) {
}
