package org.sopt.homework.dto.post.response;

import java.util.List;
import java.util.UUID;

import org.sopt.homework.domain.Post;

public record PostListResponse(
		List<PostListElement> posts
) {
	public static PostListResponse of(List<Post> posts) {
		return new PostListResponse(posts.stream().map(PostListElement::of).toList());
	}

	private record PostListElement(
			UUID id,
			String title,
			String author,
			int likeCount
	) {
		private static PostListElement of(Post post) {
			String author = null;
			if (post.getUser() != null) {
				author = post.getUser().getName();
			}
			return new PostListElement(
					post.getId(),
					post.getTitle(),
					author,
					post.getLikeCount()
			);
		}
	}
}
