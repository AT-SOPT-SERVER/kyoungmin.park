package org.sopt.homework.service;

import org.sopt.homework.dto.user.UserSignupRequest;
import org.sopt.homework.repository.PostRepository;
import org.sopt.homework.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PostRepository postRepository;

	@Transactional
	public void createUser(final UserSignupRequest userSignupRequest) {
		userRepository.save(userSignupRequest.toEntity());
	}

	@Transactional
	public void deleteUser(final long userId) {
		// 게시글과 유저의 연관관계 먼저 제거
		postRepository.removeAuthor(userId);

		// 유저 삭제
		userRepository.deleteById(userId);
	}
}
