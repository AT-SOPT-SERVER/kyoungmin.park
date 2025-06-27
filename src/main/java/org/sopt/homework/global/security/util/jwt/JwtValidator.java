package org.sopt.homework.global.security.util.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtValidator {
	private final SecretKey secretKey;

	public JwtValidator(@Value("${spring.jwt.secret}") String secret) {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	public boolean isValid(String accessTokenWithBearer) {
		return isValidFormat(accessTokenWithBearer) && !isExpired(accessTokenWithBearer);
	}

	private boolean isValidFormat(final String accessTokenWithBearer) {
		return accessTokenWithBearer != null && accessTokenWithBearer.startsWith("Bearer ");
	}

	public Boolean isExpired(final String accessTokenWithBearer) {
		String token = accessTokenWithBearer.split(" ")[1];
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getExpiration()
				.before(new Date());
	}
}
