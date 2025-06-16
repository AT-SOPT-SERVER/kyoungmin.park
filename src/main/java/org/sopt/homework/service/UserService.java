package org.sopt.homework.service;

import java.util.UUID;

import org.sopt.homework.domain.User;
import org.sopt.homework.dto.user.request.SigninRequest;
import org.sopt.homework.dto.user.request.UserSignupRequest;
import org.sopt.homework.dto.user.response.TokenResponse;
import org.sopt.homework.global.api.message.ResponseMessage;
import org.sopt.homework.global.common.util.CookieUtil;
import org.sopt.homework.global.common.util.RedisUtil;
import org.sopt.homework.global.exception.GlobalException;
import org.sopt.homework.global.exception.message.ExceptionMessage;
import org.sopt.homework.global.security.util.jwt.JwtParser;
import org.sopt.homework.global.security.util.jwt.JwtProvider;
import org.sopt.homework.global.security.util.jwt.JwtValidator;
import org.sopt.homework.repository.comment.CommentRepository;
import org.sopt.homework.repository.post.PostRepository;
import org.sopt.homework.repository.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final JwtProvider jwtProvider;
	private final JwtValidator jwtValidator;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final RedisUtil redisUtil;
	private final JwtParser jwtParser;

	@Transactional
	public ResponseMessage createUser(final UserSignupRequest userSignupRequest) {
		// 로그인 아이디가 이미 사용중인지 검증
		if (userRepository.existsByUsername(userSignupRequest.username())) {
			throw new GlobalException(ExceptionMessage.EXIST_USERNAME);
		}

		// 비밀번호 암호화
		String encodedPassword = bCryptPasswordEncoder.encode(userSignupRequest.password());
		userRepository.save(userSignupRequest.toEntity(encodedPassword));

		return ResponseMessage.USER_CREATED;
	}

	public TokenResponse login(final SigninRequest signinRequest) {
		User user = userRepository.findByUsername(signinRequest.username())
				.orElseThrow(() -> new GlobalException(ExceptionMessage.WRONG_LOGIN_ID));

		if (!bCryptPasswordEncoder.matches(signinRequest.password(), user.getPassword())) {
			throw new GlobalException(ExceptionMessage.WRONG_PASSWORD);
		}

		return generateToken(user);
	}

	public TokenResponse reissueToken(final String refreshToken) {
		if (jwtValidator.isValid(refreshToken)) {
			throw new GlobalException(ExceptionMessage.INVALID_TOKEN);
		}
		User user = userRepository.findById(jwtParser.getUserId(refreshToken))
				.orElseThrow(() -> new GlobalException(ExceptionMessage.USER_NOT_FOUND));

		return generateToken(user);
	}

	public ResponseMessage logout(final String accessTokenWithBearer, final String refreshToken,
			HttpServletResponse response) {
		// 토큰 유효성 검사
		if (!jwtValidator.isValid(accessTokenWithBearer) || refreshToken == null) {
			throw new GlobalException(ExceptionMessage.INVALID_TOKEN);
		}
		// access 토큰을 블랙리스트에 추가
		redisUtil.addTokenOnBlackList(accessTokenWithBearer);
		// refresh 토큰을 블래리스트에 추가
		redisUtil.addTokenOnBlackList(refreshToken);
		// 기존 쿠키 삭제
		response.addCookie(CookieUtil.createCookie("Refresh", null, 0));
		return ResponseMessage.SUCCESS;
	}

	@Transactional
	public ResponseMessage deleteUser(
			final UUID userId,
			final String accessTokenWithBearer,
			final String refreshToken,
			final HttpServletResponse response
	) {
		// 게시글과 유저의 연관관계 먼저 제거
		postRepository.removeAuthor(userId);

		// 댓글과 유저의 연관관계 제거
		commentRepository.removeAuthor(userId);

		// 유저 삭제
		userRepository.deleteById(userId);

		// 토큰 유효성 검사
		if (!jwtValidator.isValid(accessTokenWithBearer) || refreshToken == null) {
			throw new GlobalException(ExceptionMessage.INVALID_TOKEN);
		}
		// access 토큰을 블랙리스트에 추가
		redisUtil.addTokenOnBlackList(accessTokenWithBearer);
		// refresh 토큰을 블래리스트에 추가
		redisUtil.addTokenOnBlackList(refreshToken);
		// 기존 쿠키 삭제
		response.addCookie(CookieUtil.createCookie("Refresh", null, 0));

		return ResponseMessage.USER_DELETED;
	}

	private TokenResponse generateToken(final User user) {
		String accessToken = jwtProvider.generateAccessToken(user);
		String refreshToken = jwtProvider.generateRefreshToken(user);

		return new TokenResponse(accessToken, refreshToken);
	}
}
