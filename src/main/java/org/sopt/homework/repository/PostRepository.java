package org.sopt.homework.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.sopt.homework.domain.Post;
import org.sopt.homework.domain.util.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
	// 가장 최근에 작성된 게시글의 작성 시각 조회
	@Query("SELECT MAX(p.createdAt) FROM Post p")
	Optional<LocalDateTime> findCurrentCreatedAt();

	// 이미 존재하는 게시글 제목인지 판별
	boolean existsByTitle(String title);

	@Query("SELECT p FROM Post p LEFT JOIN p.author WHERE p.id = :postId")
	Optional<Post> findById(@Param("postId") long postId);

	// 게시글 전체 목록 최신순 조회
	@Query("SELECT p FROM Post p LEFT JOIN p.author u ORDER BY p.createdAt DESC")
	List<Post> findAllByOrderByCreatedAtDesc();

	// 연관관계 제거 반영을 위한 벌크 연산
	@Modifying
	@Query("UPDATE Post p SET p.author = NULL WHERE p.author.id = :userId")
	void removeAuthor(@Param("userId") long userId);

	// 검색 조건에 따라 검색, 키원드가 전부 null이거나 빈 문자열이면 전체 리스트를 반환
	@Query("""
		    SELECT p FROM Post p
		    LEFT JOIN p.author u
		    WHERE (:title IS NULL OR :title = '' OR p.title LIKE CONCAT('%', :title, '%'))
		      AND (:author IS NULL OR :author = '' OR u.name LIKE CONCAT('%', :author, '%'))
		      AND (:tag IS NULL OR p.tag = :tag)
		    ORDER BY p.createdAt DESC
		""")
	List<Post> findByOptionsOrderByCreatedAtDesc(
		@Param("author") String author,
		@Param("title") String title,
		@Param("tag") Tag tag);
}
