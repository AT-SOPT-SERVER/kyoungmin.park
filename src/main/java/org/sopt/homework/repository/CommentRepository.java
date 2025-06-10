package org.sopt.homework.repository;

import java.util.Optional;
import java.util.UUID;

import org.sopt.homework.domain.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
	@Query("""
			SELECT c FROM Comment c
			LEFT JOIN FETCH c.user
			WHERE c.post.id = :postId
			ORDER BY c.createdAt DESC
			""")
	Slice<Comment> findAllByPostId(@Param("postId") UUID postId, Pageable pageable);

	@Query("SELECT c FROM Comment c LEFT JOIN FETCH c.user WHERE c.id = :commentId")
	Optional<Comment> findByIdWithUser(@Param("commentId") UUID commentId);

	// 비관적락 사용
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT c FROM Comment c WHERE c.id = :commentId")
	Optional<Comment> findByIdForUpdate(@Param("commentId") UUID commentId);

	@Modifying
	@Query("UPDATE Comment c SET c.user = NULL WHERE c.user.id = :userId")
	void removeAuthor(@Param("userId") UUID userId);

	@Modifying
	@Query("DELETE FROM Comment c WHERE c.post.id = :postId")
	void deleteAllByPostId(@Param("postId") UUID postId);
}
