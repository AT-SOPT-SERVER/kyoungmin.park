package org.sopt.homework.dto.user.request;

import org.sopt.homework.domain.User;

public record UserSignupRequest(String username, String password, String name) {
	public User toEntity(String encordedPassword) {
		return User.builder()
				.username(this.username)
				.password(encordedPassword)
				.name(this.name)
				.build();
	}
}
