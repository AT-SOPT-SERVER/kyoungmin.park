package org.sopt.homework.service;

import java.util.UUID;

import org.sopt.homework.domain.User;
import org.sopt.homework.dto.user.request.SigninRequest;
import org.sopt.homework.dto.user.request.UserSignupRequest;
import org.sopt.homework.dto.user.response.TokenResponse;
import org.sopt.homework.global.api.message.ResponseMessage;
import org.sopt.homework.global.exception.GlobalException;
import org.sopt.homework.global.exception.message.ExceptionMessage;
import org.sopt.homework.global.security.util.jwt.JwtProvider;
import org.sopt.homework.repository.comment.CommentRepository;
import org.sopt.homework.repository.post.PostRepository;
import org.sopt.homework.repository.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final JwtProvider jwtProvider;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

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

		String accessToken = jwtProvider.generateAccessToken(user);
		String refreshToken = jwtProvider.generateRefreshToken(user);

		return new TokenResponse(accessToken, refreshToken);
	}

	@Transactional
	public ResponseMessage deleteUser(final UUID userId) {
		// 게시글과 유저의 연관관계 먼저 제거
		postRepository.removeAuthor(userId);

		// 댓글과 유저의 연관관계 제거
		commentRepository.removeAuthor(userId);

		// 유저 삭제
		userRepository.deleteById(userId);

		return ResponseMessage.USER_DELETED;
	}
}
