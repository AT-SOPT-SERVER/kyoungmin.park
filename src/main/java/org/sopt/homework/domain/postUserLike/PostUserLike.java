package org.sopt.homework.domain.postUserLike;

import org.sopt.homework.domain.Post;
import org.sopt.homework.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "post_user_like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUserLike {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_user_like_id", nullable = false, updatable = false, unique = true)
	private long id;

	@ManyToOne
	@JoinColumn(name = "post_id", nullable = false, updatable = false)
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	private User user;

	@Builder
	private PostUserLike(Post post, User user) {
		this.post = post;
		this.user = user;
	}
}
