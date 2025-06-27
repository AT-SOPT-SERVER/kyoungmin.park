package org.sopt.homework.dto.post.response;

import java.util.List;
import java.util.UUID;

import org.sopt.homework.domain.Post;
import org.springframework.data.domain.Page;

public record PostListResponse(
		List<PostListElement> posts,
		PageInfo pageInfo
) {
	public static PostListResponse of(Page<Post> posts) {
		return new PostListResponse(posts.getContent()
				.stream().map(PostListElement::of).toList(),
				PageInfo.of(posts.getTotalPages(), posts.getNumber()));
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

	private record PageInfo(int totalPages, int currentPage) {
		private static PageInfo of(int totalPages, int currentPage) {
			return new PageInfo(totalPages, ++currentPage);
		}
	}
}
