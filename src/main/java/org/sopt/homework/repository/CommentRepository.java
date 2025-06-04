package org.sopt.homework.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.sopt.homework.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
	@Query("""
			SELECT c FROM Comment c
			JOIN FETCH c.user AS a
			WHERE c.post.id = :postId
			ORDER BY c.createdAt DESC
			""")
	List<Comment> findAllByPostId(@Param("postId") UUID postId);

	@Query("SELECT c FROM Comment c LEFT JOIN FETCH c.user WHERE c.id = :commentId")
	Optional<Comment> findById(@Param("commentId") UUID commentId);

	@Modifying
	@Query("UPDATE Comment c SET c.user = NULL WHERE c.user.id = :userId")
	void removeAuthor(@Param("userId") UUID userId);

	@Modifying
	@Query("DELETE FROM Comment c WHERE c.post.id = :postId")
	void deleteAllByPostId(@Param("postId") UUID postId);
}
