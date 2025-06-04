package org.sopt.homework.service;

import java.util.List;
import java.util.UUID;

import org.sopt.homework.domain.Comment;
import org.sopt.homework.dto.comment.request.CommentWriteRequest;
import org.sopt.homework.dto.comment.response.CommentDetailResponse;
import org.sopt.homework.global.exception.GlobalException;
import org.sopt.homework.global.exception.message.ExceptionMessage;
import org.sopt.homework.repository.CommentRepository;
import org.sopt.homework.repository.PostRepository;
import org.sopt.homework.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;

	@Transactional
	public void createComment(final UUID userId, final UUID postId,
			final CommentWriteRequest commentWriteRequest
	) {
		if (!postRepository.existsById(postId)) {
			throw new GlobalException(ExceptionMessage.POST_NOT_FOUND);
		}
		Comment newComment = commentWriteRequest.toEntity(
				userRepository.getReferenceById(userId),
				postRepository.getReferenceById(postId));

		commentRepository.save(newComment);
	}

	public List<CommentDetailResponse> getComments(final UUID postId) {
		return commentRepository.findAllByPostId(postId).stream()
				.map(CommentDetailResponse::from)
				.toList();
	}

	@Transactional
	public void updateComment(final UUID userId, final UUID commentId,
			final CommentWriteRequest commentWriteRequest
	) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new GlobalException(ExceptionMessage.COMMENT_NOT_FOUND));

		// 요청한 사용자와 작성자의 일치여부 검증
		if (!comment.getUser().getId().equals(userId)) {
			throw new GlobalException(ExceptionMessage.INVALID_AUTHOR);
		}

		comment.updateComment(commentWriteRequest.content());
	}

	@Transactional
	public void deleteComment(final UUID userId, final UUID commentId) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new GlobalException(ExceptionMessage.COMMENT_NOT_FOUND));

		// 요청한 사용자와 작성자의 일치여부 검증
		if (!comment.getUser().getId().equals(userId)) {
			throw new GlobalException(ExceptionMessage.INVALID_AUTHOR);
		}

		commentRepository.deleteById(commentId);
	}
}
