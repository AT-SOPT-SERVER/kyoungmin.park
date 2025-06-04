package org.sopt.homework.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class Comment extends BaseEntity {
	@Id
	@Column(name = "comment_id", columnDefinition = "BINARY(16)", nullable = false, unique = true, updatable = false)
	private UUID id;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "like_count")
	private int likeCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;

	@Builder
	private Comment(String content, User user, Post post) {
		this.id = UUID.randomUUID();
		this.content = content;
		this.user = user;
		this.post = post;
	}

	public void updateComment(String content) {
		this.content = content;
	}
}
