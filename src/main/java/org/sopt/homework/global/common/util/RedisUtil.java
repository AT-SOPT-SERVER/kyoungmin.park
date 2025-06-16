package org.sopt.homework.global.common.util;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.sopt.homework.global.security.util.jwt.JwtParser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisUtil {
	private final RedisTemplate<String, Object> redisTemplate;
	private final JwtParser jwtParser;

	public void addTokenOnBlackList(String token) {
		if (token.startsWith("Bearer ")) {
			String accessToken = token.substring(7);
			redisTemplate.opsForValue()
					.set(generateKey(accessToken), "ACCESS", calculateTokenTtlSeconds(accessToken), TimeUnit.SECONDS);
			return;
		}
		redisTemplate.opsForValue()
				.set(generateKey(token), "REFRESH", calculateTokenTtlSeconds(token), TimeUnit.SECONDS);
	}

	public Optional<Object> searchTokenOnBlackList(String token) {
		if (token.startsWith("Bearer ")) {
			return Optional.ofNullable(redisTemplate.opsForValue().get(generateKey(token.substring(7))));
		}
		return Optional.ofNullable(redisTemplate.opsForValue().get(generateKey(token)));
	}

	private long calculateTokenTtlSeconds(String token) {
		return (jwtParser.getExpire(token).getTime() - System.currentTimeMillis()) / 1000;
	}

	private String generateKey(String token) {
		return "blackList:" + token;
	}
}
