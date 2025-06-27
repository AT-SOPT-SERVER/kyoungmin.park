package org.sopt.homework.global.security.util.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtParser {
	private final SecretKey secretKey;

	public JwtParser(@Value("${spring.jwt.secret}") String secret) {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	// token에서 username을 추출(parsing)
	public UUID getUserId(final String token) {
		return UUID.fromString(
				Jwts.parser()
						.verifyWith(secretKey)
						.build()
						.parseSignedClaims(token)
						.getPayload()
						.get("userId", String.class)
		);
	}

	// token에서 role을 추출(parsing)
	public String getRole(final String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.get("role", String.class);
	}

	// 토큰에서 만료 시점을 추출
	public Date getExpire(final String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getExpiration();
	}
}
