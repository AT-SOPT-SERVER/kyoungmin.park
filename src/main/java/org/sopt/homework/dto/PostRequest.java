package org.sopt.homework.dto;

import org.sopt.homework.domain.Post;

public record PostRequest(String title) {
	public Post toEntity() {
		return new Post(this.title);
	}
}