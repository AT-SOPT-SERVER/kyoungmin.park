package org.sopt.homework.domain.commentUserLike;

import org.sopt.homework.domain.Comment;
import org.sopt.homework.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "comment_user_like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentUserLike {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_user_like_id", nullable = false, updatable = false, unique = true)
	private long id;

	@ManyToOne
	@JoinColumn(name = "comment_id", nullable = false, updatable = false)
	private Comment comment;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	private User user;

	@Builder
	private CommentUserLike(Comment comment, User user) {
		this.comment = comment;
		this.user = user;
	}
}
