package org.sopt.homework.dto.post.response;

import org.sopt.homework.domain.Post;

public record PostDetailResponse(
	long id,
	String title,
	String content,
	String tagName,
	String author
) {
	public static PostDetailResponse of(Post post) {
		String author = null;
		if (post.getAuthor() != null) {
			author = post.getAuthor().getName();
		}
		return new PostDetailResponse(
			post.getId(),
			post.getTitle(),
			post.getContent(),
			post.getTag().getTagName(),
			author
		);
	}
}
