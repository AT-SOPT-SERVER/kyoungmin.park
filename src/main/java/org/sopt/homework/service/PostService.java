package org.sopt.homework.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.sopt.homework.domain.Post;
import org.sopt.homework.domain.User;
import org.sopt.homework.domain.postUserLike.PostUserLike;
import org.sopt.homework.domain.util.post.Tag;
import org.sopt.homework.dto.post.request.PostWriteRequest;
import org.sopt.homework.dto.post.response.PostDetailResponse;
import org.sopt.homework.dto.post.response.PostListResponse;
import org.sopt.homework.global.api.message.ResponseMessage;
import org.sopt.homework.global.exception.GlobalException;
import org.sopt.homework.global.exception.message.ExceptionMessage;
import org.sopt.homework.repository.CommentRepository;
import org.sopt.homework.repository.PostRepository;
import org.sopt.homework.repository.PostUserLikeRepository;
import org.sopt.homework.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
	private static final int PAGE_SIZE = 10;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	private final PostUserLikeRepository postUserLikeRepository;

	// 게시글 저장
	@Transactional
	public ResponseMessage createPost(final UUID userId, final PostWriteRequest postWriteRequestDto) {
		// 검증 실패시에 쿼리문이 실행되지 않도록 엔티티 변환을 먼저 수행
		Post newPost = postWriteRequestDto.toEntity(getAuthor(userId));

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

		return ResponseMessage.POST_CREATED;
	}

	// 게시글 전체 조회
	public PostListResponse getAllPosts(int page) {
		validatePage(page);
		// 0번 페이지를 1번으로 인덱스 일치시키기
		Pageable pageable = PageRequest.of(--page, PAGE_SIZE);
		// 결과가 존재하지 않으면 빈 리스트 반환
		return PostListResponse.of(postRepository.findAllByOrderByCreatedAtDesc(pageable));
	}

	// 게시글 검색
	public PostListResponse searchPosts(final String user, final String title, final String tagName, int page) {
		validatePage(page);
		// 0번 페이지를 1번으로 인덱스 일치시키기
		Pageable pageable = PageRequest.of(--page, PAGE_SIZE);
		Tag tag = null;

		if (tagName != null) {
			tag = Tag.fromString(tagName);
		}

		return PostListResponse.of(
				postRepository.findByOptionsOrderByCreatedAtDesc(user, title, tag, pageable));
	}

	// 게시글 상세 조회
	public PostDetailResponse getPostById(final UUID userId, final UUID postId) {
		// id에 해당하는 게시글이 존재하지 않으면 예외 발생
		Post post = postRepository.findByIdWithUser(postId)
				.orElseThrow(() -> new GlobalException(ExceptionMessage.POST_NOT_FOUND));

		boolean isLiked = postUserLikeRepository.existsByUserIdAndPostId(userId, postId);
		return PostDetailResponse.of(post, isLiked);
	}

	// 게시글 수정
	@Transactional
	public ResponseMessage updatePost(final UUID postId, final UUID userId,
			final PostWriteRequest postWriteRequestDto) {
		// 제목 수정 시 요청에 포함된 제목이 이미 존재한다면 예외 발생
		if (postRepository.existsByTitle(postWriteRequestDto.title())) {
			throw new GlobalException(ExceptionMessage.EXIST_TITLE);
		}

		// id에 해당하는 게시글이 존재하지 않으면 예외 발생
		Post post = postRepository.findByIdWithUser(postId)
				.orElseThrow(() -> new GlobalException(ExceptionMessage.POST_NOT_FOUND));

		if (!post.getUser().getId().equals(userId)) {
			throw new GlobalException(ExceptionMessage.INVALID_AUTHOR);
		}

		post.updateTitle(postWriteRequestDto.title());
		post.updateContent(postWriteRequestDto.content());
		post.updateTag(postWriteRequestDto.tagName());

		return ResponseMessage.SUCCESS;
	}

	// 게시글 삭제
	@Transactional
	public ResponseMessage deletePostById(final UUID userId, final UUID postId) {
		Post post = postRepository.findByIdWithUser(postId)
				.orElseThrow(() -> new GlobalException(ExceptionMessage.POST_NOT_FOUND));

		if (post.getUser() == null || !post.getUser().getId().equals(userId)) {
			throw new GlobalException(ExceptionMessage.INVALID_AUTHOR);
		}

		commentRepository.deleteAllByPostId(postId);
		postRepository.deleteById(postId);

		return ResponseMessage.POST_DELETED;
	}

	@Transactional
	public ResponseMessage likePost(final UUID userId, final UUID postId) {
		Post post = postRepository.findByIdForUpdate(postId)
				.orElseThrow(() -> new GlobalException(ExceptionMessage.POST_NOT_FOUND));

		if (!userRepository.existsById(userId)) {
			throw new GlobalException(ExceptionMessage.USER_NOT_FOUND);
		}
		// 이미 좋아요를 눌렀는지 여부를 조회하여 좋아요 수를 조정
		if (postUserLikeRepository.existsByUserIdAndPostId(userId, postId)) {
			postUserLikeRepository.deleteByUserIdAndPostId(userId, postId);
			post.decreaseLikeCount();
			return ResponseMessage.SUCCESS;
		}
		PostUserLike postUserLike = PostUserLike.builder()
				.user(userRepository.getReferenceById(userId))
				.post(post)
				.build();

		postUserLikeRepository.save(postUserLike);
		post.increaseLikeCount();
		return ResponseMessage.SUCCESS;
	}

	// 게시글 생성을 위한 유저 조회
	private User getAuthor(final UUID userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new GlobalException(ExceptionMessage.USER_NOT_FOUND));
	}

	// 페이지가 올바른 인덱스인지 검사
	private void validatePage(final int page) {
		if (page < 1) {
			throw new GlobalException(ExceptionMessage.WRONG_PAGE_INDEX);
		}
	}
}
