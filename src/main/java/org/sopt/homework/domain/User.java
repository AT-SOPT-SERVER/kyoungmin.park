package org.sopt.homework.domain;

import java.util.UUID;

import org.sopt.homework.domain.util.user.Role;
import org.sopt.homework.domain.util.validator.Validator;
import org.sopt.homework.global.exception.GlobalException;
import org.sopt.homework.global.exception.message.ExceptionMessage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class User {
	@Id
	@Column(name = "user_id", columnDefinition = "BINARY(16)", nullable = false, unique = true, updatable = false)
	private UUID id;

	@Column(name = "username", nullable = false, unique = true, updatable = false)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	@Builder
	private User(String username, String password, String name) {
		validateName(name);
		this.id = UUID.randomUUID();
		this.name = name;
		this.username = username;
		this.password = password;
		this.role = Role.ROLE_MEMBER;
	}

	private void validateName(String name) {
		if (name == null) {
			throw new GlobalException(ExceptionMessage.BAD_REQUEST);
		}
		if (Validator.isEmpty(name)) {
			throw new GlobalException(ExceptionMessage.EMPTY_NAME);
		}
		if (Validator.isOverLength(name, 10)) {
			throw new GlobalException(ExceptionMessage.OVER_LENGTH_NAME);
		}
	}
}
