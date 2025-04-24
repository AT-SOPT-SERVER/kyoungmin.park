package org.sopt.homework.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.sopt.homework.domain.Post;
import org.sopt.homework.dto.PostRequest;
import org.sopt.homework.dto.PostResponse;
import org.sopt.homework.global.exception.GlobalException;
import org.sopt.homework.global.exception.message.ExceptionMessage;
import org.sopt.homework.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {
	private final PostRepository postRepository;

	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	// 게시글 저장
	@Transactional
	public PostResponse createPost(PostRequest postRequestDto) {
		// 검증 실패시에 쿼리문이 실행되지 않도록 엔티티 변환을 먼저 수행
		Post newPost = postRequestDto.toEntity();

		// 마지막으로 게시글이 작성된 시각 조회
		LocalDateTime lastCreated = postRepository.findCurrentCreatedAt().orElse(LocalDateTime.now().minusMinutes(3L));

		// 마지막 작성 시각과의 차이가 3분 이내라면 예외 발생
		if (Duration.between(lastCreated, LocalDateTime.now()).toMinutes() < 1) {
			throw new GlobalException(ExceptionMessage.CREATION_TIME_LIMIT);
		}

		// 요청에 포함된 제목이 이미 존재한다면 예외 발생
		if (postRepository.existsByTitle(postRequestDto.title())) {
			throw new GlobalException(ExceptionMessage.EXIST_TITLE);
		}

		return PostResponse.of(postRepository.save(newPost));
	}

	// 게시글 전체 조회
	public List<PostResponse> getAllPosts() {
		// 결과가 존재하지 않으면 빈 리스트 반환
		return postRepository.findAll().stream().map(PostResponse::of).toList();
	}

	// 키워드로 게시글 검색
	public List<PostResponse> getPostsByTitle(String keyword) {
		// 결과가 존재하지 않으면 검색 쿼리를 날리지 않고 빈 리스트 반환
		if (keyword.isBlank()) {
			return new ArrayList<>();
		}

		return postRepository.findByTitleContaining(keyword).stream().map(PostResponse::of).toList();
	}

	// 게시글 상세 조회
	public PostResponse getPostById(Long id) {
		// id에 해당하는 게시글이 존재하지 않으면 예외 발생
		Post post = postRepository.findById(id)
			.orElseThrow(() -> new GlobalException(ExceptionMessage.POST_NOT_FOUND));
		return PostResponse.of(post);
	}

	// 게시글 제목 수정
	@Transactional
	public void updatePost(Long id, PostRequest postRequestDto) {
		// 제목 수정 시 요청에 포함된 제목이 이미 존재한다면 예외 발생
		if (postRepository.existsByTitle(postRequestDto.title())) {
			throw new GlobalException(ExceptionMessage.EXIST_TITLE);
		}

		// id에 해당하는 게시글이 존재하지 않으면 예외 발생
		Post post = postRepository.findById(id)
			.orElseThrow(() -> new GlobalException(ExceptionMessage.POST_NOT_FOUND));

		post.updateTitle(postRequestDto.title());
	}

	// 게시글 삭제
	@Transactional
	public void deletePostById(Long id) {
		postRepository.deleteById(id);
	}
}
