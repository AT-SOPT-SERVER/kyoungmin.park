package org.sopt.homework.repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.sopt.homework.domain.Post;
import org.sopt.homework.repository.util.generator.Generator;
import org.sopt.homework.repository.util.io.FileIO;
import org.sopt.homework.util.executor.ExecutorManager;

public class PostRepository {
	private Map<Long, Post> posts = new HashMap<>();

	// 도배 방지를 위해 repository가 마지막 게시글 작성 시각을 저장
	// 초기값: 프로그램 실행 시점 - 3분
	private LocalDateTime lastCreated = LocalDateTime.now().minusMinutes(3L);

	// 파일에 저장된 게시글 데이터 불러오기
	public void load() {
		this.posts = FileIO.loadFromFile();
	}

	// 게시글 저장
	public void save(Post post) {
		// 게시글의 아이디 생성
		Long id = Generator.generateId(posts.keySet());

		// 스레드를 이용하여 파일입력 병렬처리
		ExecutorManager.getExecutor().submit(() -> FileIO.saveToFile(id, post));

		this.posts.put(id, post);
		this.lastCreated = LocalDateTime.now();    // 마지막 게시글 생성 시점 기록
	}

	// 게시글 전체 반환
	public Map<Long, Post> findAll() {
		return this.posts;
	}

	// id에 해당하는 게시글만 반환, 없으면 null 반환, map의 원소 중 하나만 반환하기 위해 map.entry를 반환
	public Map.Entry<Long, Post> findById(Long id) {
		Post post = this.posts.get(id);
		if (post == null) {
			return null;
		}
		return Map.entry(id, post);
	}

	// id에 해당하는 게시글 찾아 제목을 수정
	public boolean updateTitle(Long id, String title) {
		Post post = this.posts.get(id);
		if (post == null) {
			return false;
		}
		post.updateTitle(title);

		ExecutorManager.getExecutor().submit(() -> FileIO.overwriteToFile(this.posts));

		return true;
	}

	// id에 해당하는 게시글만 삭제 후 삭제 여부 반환
	public boolean deleteById(Long id) {
		if (!existsById(id)) {
			return false;
		}
		this.posts.remove(id);

		ExecutorManager.getExecutor().submit(() -> FileIO.overwriteToFile(this.posts));

		return true;
	}

	// 제목이 키워드를 포함하는 게시글을 반환
	public Map<Long, Post> findByTitle(String title) {
		// map을 set으로 변환한 후에 stream을 이용하여 키워드가 포함된 제목을 탐색 후 반환
		return this.posts.entrySet().stream()
			.filter(entry -> entry.getValue().getTitle().contains(title))
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				Map.Entry::getValue
			));
	}

	// 이미 존재하는 게시글 제목인지 판별
	public boolean existByTitle(String title) {
		return this.posts.values().stream()
			.anyMatch(post -> post.getTitle().equals(title));
	}

	// 마지막으로 게시글이 생성된 시점 반환
	public LocalDateTime getLastCreated() {
		return this.lastCreated;
	}

	// 이미 존재하는 아이디인지 판별
	private boolean existsById(Long id) {
		return this.posts.containsKey(id);
	}
}
