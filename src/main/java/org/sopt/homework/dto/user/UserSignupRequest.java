package org.sopt.homework.dto.user;

import org.sopt.homework.domain.User;

public record UserSignupRequest(String name) {
	public User toEntity() {
		return new User(this.name);
	}
}
