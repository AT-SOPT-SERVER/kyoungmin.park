package org.sopt.homework.domain;

import org.sopt.homework.domain.util.validator.Validator;
import org.sopt.homework.exception.ExceptionMessage;

public class Post {
	private String title;

	public Post(String title) {
		if (!Validator.isValidTitle(title)) {
			throw new IllegalArgumentException(ExceptionMessage.INVALID_TITLE.getMessage());
		}
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void updateTitle(String title) {
		this.title = title;
	}
}
