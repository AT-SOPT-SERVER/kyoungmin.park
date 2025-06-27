package org.sopt.homework.dto.comment.response;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.sopt.homework.domain.Comment;
import org.springframework.data.domain.Slice;

public record CommentListResponse(List<CommentDetailResponse> comments, PageInfo pageInfo) {
	public static CommentListResponse of(
			Slice<Comment> comments, Set<UUID> likedCommentIds
	) {
		return new CommentListResponse(
				comments.getContent().stream()
						.map(comment -> CommentDetailResponse.of(comment, likedCommentIds.contains(comment.getId())))
						.toList(),
				PageInfo.of(comments.getNumber(), comments.hasNext())
		);
	}

	private record PageInfo(int currentPage, boolean hasNext) {
		private static PageInfo of(int currentPage, boolean hasNext) {
			return new PageInfo(++currentPage, hasNext);
		}
	}
}
