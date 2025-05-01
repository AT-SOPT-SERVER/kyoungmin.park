package org.sopt.homework.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.sopt.homework.domain.Post;
import org.sopt.homework.domain.User;
import org.sopt.homework.domain.util.Tag;
import org.sopt.homework.dto.post.request.PostWriteRequest;
import org.sopt.homework.dto.post.response.PostDetailResponse;
import org.sopt.homework.dto.post.response.PostListResponse;
import org.sopt.homework.global.exception.GlobalException;
import org.sopt.homework.global.exception.message.ExceptionMessage;
import org.sopt.homework.repository.PostRepository;
import org.sopt.homework.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	public PostService(PostRepository postRepository, UserRepository userRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
	}

	// 게시글 저장
	@Transactional
	public void createPost(final long userId, final PostWriteRequest postWriteRequestDto) {
		// 검증 실패시에 쿼리문이 실행되지 않도록 엔티티 변환을 먼저 수행
		Post newPost = postWriteRequestDto.toEntity(getUserProxy(userId));

		// 마지막으로 게시글이 작성된 시각 조회
		LocalDateTime lastCreated = postRepository.findCurrentCreatedAt().orElse(LocalDateTime.now().minusMinutes(3L));

		// 마지막 작성 시각과의 차이가 3분 이내라면 예외 발생
		if (Duration.between(lastCreated, LocalDateTime.now()).toMinutes() < 3) {
			throw new GlobalException(ExceptionMessage.CREATION_TIME_LIMIT);
		}

		// 요청에 포함된 제목이 이미 존재한다면 예외 발생
		if (postRepository.existsByTitle(postWriteRequestDto.title())) {
			throw new GlobalException(ExceptionMessage.EXIST_TITLE);
		}

		postRepository.save(newPost);
	}

	// 게시글 전체 조회
	public PostListResponse getAllPosts() {
		// 결과가 존재하지 않으면 빈 리스트 반환
		return PostListResponse.of(postRepository.findAllByOrderByCreatedAtDesc());
	}

	public PostListResponse searchPosts(final String author, final String title, final String tagName) {
		Tag tag = null;

		if (tagName != null) {
			tag = Tag.fromString(tagName);
		}

		return PostListResponse.of(
			postRepository.findByOptionsOrderByCreatedAtDesc(author, title, tag));
	}

	// 게시글 상세 조회
	public PostDetailResponse getPostById(final long postId) {
		// id에 해당하는 게시글이 존재하지 않으면 예외 발생
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new GlobalException(ExceptionMessage.POST_NOT_FOUND));
		return PostDetailResponse.of(post);
	}

	// 게시글 수정
	@Transactional
	public void updatePost(final long postId, final long userId, final PostWriteRequest postWriteRequestDto) {
		// 제목 수정 시 요청에 포함된 제목이 이미 존재한다면 예외 발생
		if (postRepository.existsByTitle(postWriteRequestDto.title())) {
			throw new GlobalException(ExceptionMessage.EXIST_TITLE);
		}

		// id에 해당하는 게시글이 존재하지 않으면 예외 발생
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new GlobalException(ExceptionMessage.POST_NOT_FOUND));

		if (post.getAuthor().getId() != userId) {
			throw new GlobalException(ExceptionMessage.INVALID_AUTHOR);
		}

		post.updateTitle(postWriteRequestDto.title());
		post.updateContent(postWriteRequestDto.content());
		post.updateTag(postWriteRequestDto.tagName());
	}

	// 게시글 삭제
	@Transactional
	public void deletePostById(final long userId, final long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new GlobalException(ExceptionMessage.POST_NOT_FOUND));

		if (post.getAuthor() == null || post.getAuthor().getId() != userId) {
			throw new GlobalException(ExceptionMessage.INVALID_AUTHOR);
		}

		postRepository.deleteById(postId); // 유효한 사용자인지 검증 하는 로직 추가
	}

	// 유저 엔티티의 프록시 객체 생성
	private User getUserProxy(final long userId) {
		if (!userRepository.existsById(userId)) {
			throw new GlobalException(ExceptionMessage.USER_NOT_FOUND);
		}
		return userRepository.getReferenceById(userId);
	}
}
