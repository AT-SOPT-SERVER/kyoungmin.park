package org.sopt.homework.repository.postLikeUser;

import java.util.UUID;

import org.sopt.homework.domain.postUserLike.PostUserLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostUserLikeRepository extends JpaRepository<PostUserLike, Long> {
	boolean existsByUserIdAndPostId(UUID userId, UUID postId);

	void deleteByUserIdAndPostId(UUID userId, UUID postId);
}
