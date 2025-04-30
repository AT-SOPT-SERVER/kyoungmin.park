package org.sopt.homework.service;

import org.sopt.homework.dto.user.UserSignupRequest;
import org.sopt.homework.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional
	public void createUser(UserSignupRequest userSignupRequest) {
		userRepository.save(userSignupRequest.toEntity());
	}
}
