package org.sopt.homework.repository.commentLikeUser;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.sopt.homework.domain.commentUserLike.CommentUserLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentUserLikeRepository extends JpaRepository<CommentUserLike, Long> {
	boolean existsByUserIdAndCommentId(UUID userId, UUID commentId);

	void deleteByUserIdAndCommentId(UUID userId, UUID commentId);

	@Query("""
				SELECT cul.comment.id
				FROM CommentUserLike cul
				WHERE cul.user.id = :userId AND cul.comment.id IN :commentIds
			""")
	Set<UUID> findCommentIdsUserLiked(@Param("userId") UUID userId, @Param("commentIds") List<UUID> commentIds);
}
