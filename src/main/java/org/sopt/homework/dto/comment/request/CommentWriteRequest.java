package org.sopt.homework.dto.comment.request;

import org.sopt.homework.domain.Comment;
import org.sopt.homework.domain.Post;
import org.sopt.homework.domain.User;

public record CommentWriteRequest(String content) {
	public Comment toEntity(User user, Post post) {
		return Comment.builder()
				.content(this.content)
				.user(user)
				.post(post)
				.build();
	}
}
