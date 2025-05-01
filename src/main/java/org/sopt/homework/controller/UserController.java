package org.sopt.homework.controller;

import org.sopt.homework.dto.user.UserSignupRequest;
import org.sopt.homework.global.api.message.ResponseMessage;
import org.sopt.homework.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/signup")
	public ResponseMessage create(@RequestBody final UserSignupRequest userSignupRequest) {
		userService.createUser(userSignupRequest);
		return ResponseMessage.POST_CREATED;
	}

	@DeleteMapping
	public ResponseMessage delete(@RequestHeader(name = "userId", required = true) final long userId) {
		userService.deleteUser(userId);
		return ResponseMessage.POST_DELETED;
	}
}
