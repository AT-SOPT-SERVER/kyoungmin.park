package org.sopt.homework.dto;

import org.sopt.homework.domain.Post;

public record PostResponse(Long id, String title) {
	public static PostResponse of(Post post) {
		return new PostResponse(post.getId(), post.getTitle());
	}
}
