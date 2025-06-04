package org.sopt.homework.controller;

import java.util.List;
import java.util.UUID;

import org.sopt.homework.dto.comment.request.CommentWriteRequest;
import org.sopt.homework.dto.comment.response.CommentDetailResponse;
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
			@PathVariable(value = "postId") final UUID postId,
			@RequestBody final CommentWriteRequest commentWriteRequest
	) {
		commentService.createComment(userId, postId, commentWriteRequest);
		return ResponseMessage.COMMENT_CREATED;
	}

	// 특정 게시물에 대한 댓글 조회
	@GetMapping(path = "/posts/{postId}/comments")
	public List<CommentDetailResponse> getComments(@PathVariable(value = "postId") final UUID postId) {
		return commentService.getComments(postId);
	}

	// 댓글 수정
	@PutMapping(path = "/comments/{commentId}")
	public ResponseMessage updateComment(
			@AuthenticationPrincipal final UUID userId,
			@PathVariable(value = "commentId") final UUID commentId,
			@RequestBody final CommentWriteRequest commentWriteRequest) {
		commentService.updateComment(userId, commentId, commentWriteRequest);
		return ResponseMessage.SUCCESS;
	}

	// 댓글 삭제
	@DeleteMapping(path = "/comments/{commentId}")
	public ResponseMessage deleteComment(
			@AuthenticationPrincipal final UUID userId,
			@PathVariable(name = "commentId") final UUID commentId
	) {
		commentService.deleteComment(userId, commentId);
		return ResponseMessage.COMMENT_DELETED;
	}
}
