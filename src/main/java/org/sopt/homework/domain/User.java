package org.sopt.homework.domain;

import org.sopt.homework.domain.util.validator.Validator;
import org.sopt.homework.global.exception.GlobalException;
import org.sopt.homework.global.exception.message.ExceptionMessage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public final class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true, updatable = false)
	private long id;

	@Column(name = "name", nullable = false)
	private String name;

	public User(String name) {
		validateName(name);
		this.name = name;
	}

	protected User() {
	}

	public long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
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
