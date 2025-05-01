package org.sopt.homework.service;

import org.sopt.homework.dto.user.UserSignupRequest;
import org.sopt.homework.repository.PostRepository;
import org.sopt.homework.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {
	private final UserRepository userRepository;
	private final PostRepository postRepository;

	public UserService(UserRepository userRepository, PostRepository postRepository) {
		this.userRepository = userRepository;
		this.postRepository = postRepository;
	}

	@Transactional
	public void createUser(UserSignupRequest userSignupRequest) {
		userRepository.save(userSignupRequest.toEntity());
	}

	@Transactional
	public void deleteUser(long userId) {
		// 게시글과 유저의 연관관계 먼저 제거
		postRepository.removeAuthor(userId);

		// 유저 삭제
		userRepository.deleteById(userId);
	}
}
