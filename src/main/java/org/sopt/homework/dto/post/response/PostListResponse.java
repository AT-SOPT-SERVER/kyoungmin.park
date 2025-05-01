package org.sopt.homework.dto.post.response;

import java.util.List;

import org.sopt.homework.domain.Post;

public record PostListResponse(
	List<PostListElement> posts
) {
	public static PostListResponse of(List<Post> posts) {
		return new PostListResponse(posts.stream().map(PostListElement::of).toList());
	}

	public record PostListElement(
		long id,
		String title,
		String author
	) {
		public static PostListElement of(Post post) {
			String author = null;
			if (post.getAuthor() != null) {
				author = post.getAuthor().getName();
			}
			return new PostListElement(
				post.getId(),
				post.getTitle(),
				author
			);
		}
	}
}
