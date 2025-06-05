package org.sopt.homework.global.security.util.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.sopt.homework.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
	private static final long ACCESS_TOKEN_EXPIRATION_MS = 24 * 60 * 60 * 1000L; // 24시간
	private static final long REFRESH_TOKEN_EXPIRATION_MS = 7 * 24 * 60 * 60 * 1000L; // 7일
	private final SecretKey secretKey;

	public JwtProvider(@Value("${spring.jwt.secret}") String secret) {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	public String generateAccessToken(final User user) {
		return Jwts.builder()
				.claim("userId", String.valueOf(user.getId()))
				.claim("role", user.getRole())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_MS))
				.signWith(secretKey)
				.compact();
	}

	public String generateRefreshToken(final User user) {
		return Jwts.builder()
				.claim("userId", user.getId())
				.claim("role", user.getRole())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_MS))
				.signWith(secretKey)
				.compact();
	}
}
