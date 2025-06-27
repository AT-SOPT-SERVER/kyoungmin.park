package org.sopt.homework.dto.post.response;

import java.util.UUID;

import org.sopt.homework.domain.Post;

public record PostDetailResponse(
		UUID id,
		String title,
		String content,
		String tagName,
		String author,
		int likeCount,
		boolean isLiked
) {
	public static PostDetailResponse of(Post post, boolean isLiked) {
		String author = null;
		if (post.getUser() != null) {
			author = post.getUser().getName();
		}
		return new PostDetailResponse(
				post.getId(),
				post.getTitle(),
				post.getContent(),
				post.getTag().getTagName(),
				author,
				post.getLikeCount(),
				isLiked
		);
	}
}
