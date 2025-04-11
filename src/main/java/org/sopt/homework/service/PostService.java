package org.sopt.homework.service;

import java.time.LocalDateTime;
import java.util.List;

import org.sopt.homework.domain.Post;
import org.sopt.homework.dto.PostDto;
import org.sopt.homework.exception.ExceptionMessage;
import org.sopt.homework.repository.PostRepository;

public class PostService {
	private final PostRepository postRepository = new PostRepository();

	// 파일로부터 게시글 정보 로딩
	public void loadPosts() {
		postRepository.load();
	}

	// 게시글 저장
	public void createPost(String title) {
		if (postRepository.getLastCreated().plusMinutes(3L).isAfter(LocalDateTime.now())) {
			throw new IllegalArgumentException(ExceptionMessage.CREATION_TIME_LIMIT.getMessage());
		}
		if (postRepository.existByTitle(title)) {
			throw new IllegalArgumentException(ExceptionMessage.EXIST_TITLE.getMessage());
		}
		postRepository.save(new Post(title));
	}

	// 게시글 전체 조회
	public List<PostDto> getAllPosts() {
		return PostDto.listOf(postRepository.findAll());
	}

	// 게시글 상세 조회
	public PostDto getPostById(Long id) {
		return PostDto.of(postRepository.findById(id));
	}

	// 게시글 제목 수정
	public boolean updatePost(Long id, String title) {
		if (postRepository.existByTitle(title)) {
			throw new IllegalArgumentException(ExceptionMessage.EXIST_TITLE.getMessage());
		}
		return postRepository.updateTitle(id, title);
	}

	// 게시글 삭제
	public boolean deletePostById(Long id) {
		return postRepository.deleteById(id);
	}

	// 키워드로 게시글 검색
	public List<PostDto> getPostsByTitle(String keyword) {
		return PostDto.listOf(postRepository.findByTitle(keyword));
	}
}
