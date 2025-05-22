package org.sopt.homework.dto.post.request;

import org.sopt.homework.domain.Post;
import org.sopt.homework.domain.User;

public record PostWriteRequest(String title, String content, String tagName) {
	public Post toEntity(User user) {
		return new Post(this.title, this.content, this.tagName, user);
	}
}