package org.sopt.homework.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.sopt.homework.domain.Comment;
import org.sopt.homework.domain.commentUserLike.CommentUserLike;
import org.sopt.homework.dto.comment.request.CommentWriteRequest;
import org.sopt.homework.dto.comment.response.CommentListResponse;
import org.sopt.homework.global.api.message.ResponseMessage;
import org.sopt.homework.global.exception.GlobalException;
import org.sopt.homework.global.exception.message.ExceptionMessage;
import org.sopt.homework.repository.comment.CommentRepository;
import org.sopt.homework.repository.commentLikeUser.CommentUserLikeRepository;
import org.sopt.homework.repository.post.PostRepository;
import org.sopt.homework.repository.user.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
	private static final int PAGE_SIZE = 10;
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final CommentUserLikeRepository commentUserLikeRepository;

	@Transactional
	public ResponseMessage createComment(final UUID userId, final UUID postId,
			final CommentWriteRequest commentWriteRequest
	) {
		if (!postRepository.existsById(postId)) {
			throw new GlobalException(ExceptionMessage.POST_NOT_FOUND);
		}
		if (!userRepository.existsById(userId)) {
			throw new GlobalException(ExceptionMessage.USER_NOT_FOUND);
		}
		Comment newComment = commentWriteRequest.toEntity(
				userRepository.getReferenceById(userId),
				postRepository.getReferenceById(postId));

		commentRepository.save(newComment);

		return ResponseMessage.COMMENT_CREATED;
	}

	public CommentListResponse getComments(final UUID userId, final UUID postId, int page) {
		validatePage(page);
		Pageable pageable = PageRequest.of(--page, PAGE_SIZE);
		Slice<Comment> comments = commentRepository.findAllByPostId(postId, pageable);

		// 모든 댓글 ID 추출
		List<UUID> commentIds = comments.getContent()
				.stream()
				.map(Comment::getId)
				.toList();
		// 좋아요 누른 댓글 ID 목록 조회
		Set<UUID> likedCommentIds = commentUserLikeRepository.findCommentIdsUserLiked(userId, commentIds);

		return CommentListResponse.of(comments, likedCommentIds);
	}

	@Transactional
	public ResponseMessage updateComment(final UUID userId, final UUID commentId,
			final CommentWriteRequest commentWriteRequest
	) {
		Comment comment = commentRepository.findByIdWithUser(commentId)
				.orElseThrow(() -> new GlobalException(ExceptionMessage.COMMENT_NOT_FOUND));

		// 요청한 사용자와 작성자의 일치여부 검증
		if (!comment.getUser().getId().equals(userId)) {
			throw new GlobalException(ExceptionMessage.INVALID_AUTHOR);
		}

		comment.updateComment(commentWriteRequest.content());

		return ResponseMessage.SUCCESS;
	}

	@Transactional
	public ResponseMessage deleteComment(final UUID userId, final UUID commentId) {
		Comment comment = commentRepository.findByIdWithUser(commentId)
				.orElseThrow(() -> new GlobalException(ExceptionMessage.COMMENT_NOT_FOUND));

		// 요청한 사용자와 작성자의 일치여부 검증
		if (!comment.getUser().getId().equals(userId)) {
			throw new GlobalException(ExceptionMessage.INVALID_AUTHOR);
		}

		commentRepository.deleteById(commentId);
		return ResponseMessage.COMMENT_DELETED;
	}

	@Transactional
	public ResponseMessage likeComment(final UUID userId, final UUID commentId) {
		// 비관적 락 사용
		Comment comment = commentRepository.findByIdForUpdate(commentId)
				.orElseThrow(() -> new GlobalException(ExceptionMessage.COMMENT_NOT_FOUND));

		if (!userRepository.existsById(userId)) {
			throw new GlobalException(ExceptionMessage.USER_NOT_FOUND);
		}
		// 이미 좋아요를 눌렀는지 여부를 조회하여 좋아요 수를 조정
		if (commentUserLikeRepository.existsByUserIdAndCommentId(userId, commentId)) {
			commentUserLikeRepository.deleteByUserIdAndCommentId(userId, commentId);
			comment.decrementLikeCount();
			return ResponseMessage.SUCCESS;
		}
		CommentUserLike commentUserLike = CommentUserLike.builder()
				.comment(comment)
				.user(userRepository.getReferenceById(userId))
				.build();

		commentUserLikeRepository.save(commentUserLike);
		comment.incrementLikeCount();
		return ResponseMessage.SUCCESS;
	}

	private void validatePage(final int page) {
		if (page < 1) {
			throw new GlobalException(ExceptionMessage.WRONG_PAGE_INDEX);
		}
	}
}
