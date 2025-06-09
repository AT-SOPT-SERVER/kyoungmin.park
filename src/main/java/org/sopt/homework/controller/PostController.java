package org.sopt.homework.controller;

import java.util.UUID;

import org.sopt.homework.dto.post.request.PostWriteRequest;
import org.sopt.homework.dto.post.response.PostDetailResponse;
import org.sopt.homework.dto.post.response.PostListResponse;
import org.sopt.homework.global.api.message.ResponseMessage;
import org.sopt.homework.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;

	// 게시글 생성
	@PostMapping
	public ResponseMessage createPost(
			@AuthenticationPrincipal UUID userId,
			@RequestBody final PostWriteRequest postWriteRequestDto
	) {
		return postService.createPost(userId, postWriteRequestDto);
	}

	// 게시글 조회
	@GetMapping
	public PostListResponse getPosts() {
		return postService.getAllPosts();
	}

	// 게시글 검색
	@GetMapping(path = "/search")
	public PostListResponse searchPosts(
			@RequestParam(name = "author", required = false) final String author,
			@RequestParam(name = "title", required = false) final String title,
			@RequestParam(name = "tag", required = false) final String tagName
	) {
		return postService.searchPosts(author, title, tagName);
	}

	// 게시글 상세 조회
	@GetMapping(path = "/{postId}")
	public PostDetailResponse getPostById(
			@AuthenticationPrincipal final UUID userId,
			@PathVariable(name = "postId") final UUID postId) {
		return postService.getPostById(userId, postId);
	}

	// 게시글 수정
	@PutMapping(path = "/{postId}")
	public ResponseMessage updatePostTitle(
			@AuthenticationPrincipal final UUID userId,
			@PathVariable(name = "postId") final UUID postId,
			@RequestBody final PostWriteRequest postWriteRequestDto
	) {
		return postService.updatePost(postId, userId, postWriteRequestDto);
	}

	// 게시글 삭제
	@DeleteMapping(path = "/{postId}")
	public ResponseMessage deletePostById(
			@AuthenticationPrincipal final UUID userId,
			@PathVariable(name = "postId") final UUID postId
	) {
		return postService.deletePostById(userId, postId);

	}

	@PostMapping(path = "/{postId}/like")
	public ResponseMessage likePost(
			@AuthenticationPrincipal final UUID userId,
			@PathVariable(name = "postId") final UUID postId
	) {
		return postService.likePost(userId, postId);
	}
}
