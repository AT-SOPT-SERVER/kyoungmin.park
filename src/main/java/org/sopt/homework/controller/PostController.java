package org.sopt.homework.controller;

import java.util.List;

import org.sopt.homework.dto.PostRequest;
import org.sopt.homework.dto.PostResponse;
import org.sopt.homework.service.PostService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public PostResponse createPost(@RequestBody final PostRequest postRequestDto) {
		return postService.createPost(postRequestDto);
	}

	// 게시글 조회
	@GetMapping
	public List<PostResponse> getPosts() {
		// 쿼리스트링으로 키워드가 전달되면 검색을 진행, 키워드가 전달되지 않으면 전체 게시글을 조회
		return postService.getAllPosts();
	}

	// 게시글 검색
	@GetMapping(path = "/search")
	public List<PostResponse> searchPosts(@RequestParam(value = "keyword") final String keyword) {
		return postService.getPostsByTitle(keyword);
	}

	// 게시글 상세 조회
	@GetMapping(path = "/{postId}")
	public PostResponse getPostById(@PathVariable(value = "postId") final Long id) {
		return postService.getPostById(id);
	}

	// 게시글 제목 수정
	@PutMapping(path = "/{postId}")
	public void updatePostTitle(@PathVariable(value = "postId") final Long id,
		@RequestBody final PostRequest postRequestDto) {
		postService.updatePost(id, postRequestDto);
	}

	// 게시글 삭제
	@DeleteMapping(path = "/{postId}")
	public void deletePostById(@PathVariable(value = "postId") final Long id) {
		postService.deletePostById(id);
	}
}
