package org.sopt.homework.controller;

import java.util.List;

import org.sopt.homework.dto.PostDto;
import org.sopt.homework.service.PostService;

public class PostController {
	private final PostService postService = new PostService();

	private int postId;

	// 파일로부터 게시글 데이터를 로딩
	public void loadPosts() {
		postService.loadPosts();
	}

	// 게시글 생성
	public void createPost(String title) {
		postService.createPost(title);
	}

	// 게시글 전체 조회
	public List<PostDto> getAllPosts() {
		return postService.getAllPosts();
	}

	// 게시글 상세 조회
	public PostDto getPostById(Long id) {
		return postService.getPostById(id);
	}

	// 게시글 제목 수정
	public boolean updatePostTitle(Long id, String title) {
		return postService.updatePost(id, title);
	}

	// 게시글 삭제
	public boolean deletePostById(Long id) {
		return postService.deletePostById(id);
	}

	// 키워드로 게시물 검색
	public List<PostDto> searchPostsByKeyword(String keyword) {
		return postService.getPostsByTitle(keyword);
	}
}
