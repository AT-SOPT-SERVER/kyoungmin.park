package org.sopt.homework.domain.util;

import java.util.Arrays;

import org.sopt.homework.global.exception.GlobalException;
import org.sopt.homework.global.exception.message.ExceptionMessage;

public enum Tag {
	BACKEND("백엔드"),
	DATABASE("데이터베이스"),
	INFRASTRUCTURE("인프라");

	private final String tagName;

	Tag(String tagName) {
		this.tagName = tagName;
	}

	public static Tag fromString(String tagName) {
		return Arrays.stream(Tag.values())
			.filter(tag -> tag.tagName.equalsIgnoreCase(tagName))
			.findFirst()
			.orElseThrow(() -> new GlobalException(ExceptionMessage.INVALID_TAG_NAME));
	}

	public String getTagName() {
		return this.tagName;
	}
}
