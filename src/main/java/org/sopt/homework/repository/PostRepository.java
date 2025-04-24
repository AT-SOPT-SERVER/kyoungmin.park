package org.sopt.homework.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.sopt.homework.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
	@Query("SELECT MAX(p.createdAt) FROM Post p")
	Optional<LocalDateTime> findCurrentCreatedAt();

	boolean existsByTitle(String title);

	List<Post> findByTitleContaining(String title);
}
