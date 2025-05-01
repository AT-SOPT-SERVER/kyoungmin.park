package org.sopt.homework.controller;

import org.sopt.homework.dto.post.request.PostWriteRequest;
import org.sopt.homework.dto.post.response.PostDetailResponse;
import org.sopt.homework.dto.post.response.PostListResponse;
import org.sopt.homework.global.api.message.ResponseMessage;
import org.sopt.homework.service.PostService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {
	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	// 게시글 생성
	@PostMapping
	public ResponseMessage createPost(
		@RequestHeader(name = "userId", required = true) final long userId,
		@RequestBody final PostWriteRequest postWriteRequestDto
	) {
		postService.createPost(userId, postWriteRequestDto);
		return ResponseMessage.POST_CREATED;
	}

	// 게시글 조회
	@GetMapping
	public PostListResponse getPosts() {
		// 쿼리스트링으로 키워드가 전달되면 검색을 진행, 키워드가 전달되지 않으면 전체 게시글을 조회
		return postService.getAllPosts();
	}

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
	public PostDetailResponse getPostById(@PathVariable(value = "postId") final long postId) {
		return postService.getPostById(postId);
	}

	// 게시글 수정
	@PutMapping(path = "/{postId}")
	public void updatePostTitle(
		@RequestHeader(name = "userId", required = true) final long userId,
		@PathVariable(value = "postId") final long postId,
		@RequestBody final PostWriteRequest postWriteRequestDto
	) {
		postService.updatePost(postId, userId, postWriteRequestDto);
	}

	// 게시글 삭제
	@DeleteMapping(path = "/{postId}")
	public ResponseMessage deletePostById(
		@RequestHeader(name = "userId", required = true) final long userId,
		@PathVariable(value = "postId") final long postId
	) {
		postService.deletePostById(userId, postId);
		return ResponseMessage.POST_DELETED;
	}
}
