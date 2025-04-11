package org.sopt.homework.domain.util.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
	// 하나의 시각적 유니코드 grapheme의 정규식인 "\X"를 이용하여 문자열의 길이를 카운트
	private static final Pattern GRAPHEME_PATTERN = Pattern.compile("\\X");

	public static boolean isValidTitle(String title) {
		// isEmpty는 길이가 0인 경우만 true를 리턴, isBlank는 길이가 0이거나 공백으로만 이루어진 문자열인 경우 true
		return !title.isBlank() && lengthWithEmoji(title) < 30;
	}

	private static int lengthWithEmoji(String title) {
		Matcher matcher = GRAPHEME_PATTERN.matcher(title);
		int count = 0;

		while (matcher.find()) {
			count++;
		}
		return count;
	}
}
