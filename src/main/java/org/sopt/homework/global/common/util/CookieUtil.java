package org.sopt.homework.global.common.util;

import jakarta.servlet.http.Cookie;

public class CookieUtil {
	public static Cookie createCookie(String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setHttpOnly(true);
		// cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);
		return cookie;
	}
}
