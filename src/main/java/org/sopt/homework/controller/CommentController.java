package org.sopt.homework.controller;

import java.util.UUID;

import org.sopt.homework.dto.comment.request.CommentWriteRequest;
import org.sopt.homework.dto.comment.response.CommentListResponse;
import org.sopt.homework.global.api.message.ResponseMessage;
import org.sopt.homework.service.CommentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;

	// 댓글 생성
	@PostMapping(path = "/posts/{postId}/comments")
	public ResponseMessage createComment(
			@AuthenticationPrincipal final UUID userId,
			@PathVariable(name = "postId") final UUID postId,
			@RequestBody final CommentWriteRequest commentWriteRequest
	) {
		return commentService.createComment(userId, postId, commentWriteRequest);
	}

	// 특정 게시물에 대한 댓글 조회
	@GetMapping(path = "/posts/{postId}/comments/{page}")
	public CommentListResponse getComments(
			@AuthenticationPrincipal final UUID userId,
			@PathVariable(name = "postId") final UUID postId,
			@PathVariable(name = "page") final int page) {
		return commentService.getComments(userId, postId, page);
	}

	// 댓글 수정
	@PutMapping(path = "/comments/{commentId}")
	public ResponseMessage updateComment(
			@AuthenticationPrincipal final UUID userId,
			@PathVariable(name = "commentId") final UUID commentId,
			@RequestBody final CommentWriteRequest commentWriteRequest) {
		return commentService.updateComment(userId, commentId, commentWriteRequest);

	}

	// 댓글 삭제
	@DeleteMapping(path = "/comments/{commentId}")
	public ResponseMessage deleteComment(
			@AuthenticationPrincipal final UUID userId,
			@PathVariable(name = "commentId") final UUID commentId
	) {
		return commentService.deleteComment(userId, commentId);
	}

	@PostMapping(path = "/comments/{commentId}/like")
	public ResponseMessage likeComment(
			@AuthenticationPrincipal UUID userId,
			@PathVariable(name = "commentId") UUID commentId
	) {
		return commentService.likeComment(userId, commentId);
	}
}
