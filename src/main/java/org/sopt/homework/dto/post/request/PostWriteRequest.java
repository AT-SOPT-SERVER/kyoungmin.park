package org.sopt.homework.dto.post.request;

import org.sopt.homework.domain.Post;
import org.sopt.homework.domain.User;

public record PostWriteRequest(String title, String content, String tagName) {
	public Post toEntity(User user) {
		return Post.builder()
				.title(this.title)
				.content(this.content)
				.tagName(this.tagName)
				.user(user)
				.build();
	}
}