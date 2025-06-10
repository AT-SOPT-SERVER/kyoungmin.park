package org.sopt.homework.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.sopt.homework.domain.Post;
import org.sopt.homework.domain.util.post.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface PostRepository extends JpaRepository<Post, UUID> {
	// 가장 최근에 작성된 게시글의 작성 시각 조회
	@Query("SELECT MAX(p.createdAt) FROM Post p")
	Optional<LocalDateTime> findCurrentCreatedAt();

	// 아이디를 통해 존재하는 게시글인지 판별
	boolean existsById(UUID postId);

	// 이미 존재하는 게시글 제목인지 판별
	boolean existsByTitle(String title);

	// 게시글 상세보기 조회
	@Query("SELECT p FROM Post p LEFT JOIN FETCH p.user WHERE p.id = :postId")
	Optional<Post> findByIdWithUser(@Param("postId") UUID postId);

	// 비관적락 사용
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Post p WHERE p.id = :postId")
	Optional<Post> findByIdForUpdate(@Param("postId") UUID postId);

	// 게시글 전체 목록 최신순 조회
	@Query("SELECT p FROM Post p LEFT JOIN FETCH p.user u ORDER BY p.createdAt DESC")
	Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

	// 연관관계 제거 반영을 위한 벌크 연산
	@Modifying
	@Query("UPDATE Post p SET p.user = NULL WHERE p.user.id = :userId")
	void removeAuthor(@Param("userId") UUID userId);

	// 검색 조건에 따라 검색, 키워드가 전부 null이거나 빈 문자열이면 전체 리스트를 반환
	@Query("""
			    SELECT p FROM Post p
			    LEFT JOIN FETCH p.user u
			    WHERE (:title IS NULL OR :title = '' OR p.title LIKE CONCAT('%', :title, '%'))
			    AND (:user IS NULL OR :user = '' OR u.name LIKE CONCAT('%', :user, '%'))
			    AND (:tag IS NULL OR p.tag = :tag)
			    ORDER BY p.createdAt DESC
			""")
	Page<Post> findByOptionsOrderByCreatedAtDesc(
			@Param("user") String user,
			@Param("title") String title,
			@Param("tag") Tag tag,
			Pageable pageable);
}
