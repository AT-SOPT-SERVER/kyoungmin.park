package org.sopt.homework.dto.comment.response;

import java.time.LocalDateTime;
import java.util.UUID;

import org.sopt.homework.domain.Comment;
import org.sopt.homework.domain.User;

public record CommentDetailResponse(Author author, CommentInfo comment) {
	public static CommentDetailResponse of(Comment comment, boolean isLiked) {
		return new CommentDetailResponse(
				Author.of(comment.getUser()),
				CommentInfo.of(comment, isLiked));
	}

	private record Author(UUID id, String name) {
		private static Author of(User user) {
			return new Author(user.getId(), user.getName());
		}
	}

	private record CommentInfo(UUID id, String content, int likeCount, boolean isLiked, LocalDateTime createdAt) {
		private static CommentInfo of(Comment comment, boolean isLiked) {
			return new CommentInfo(comment.getId(), comment.getContent(), comment.getLikeCount(), isLiked,
					comment.getCreatedAt());
		}
	}
}
