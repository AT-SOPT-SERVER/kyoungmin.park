package org.sopt.homework.dto;

import java.util.List;
import java.util.Map;

import org.sopt.homework.domain.Post;

public class PostDto {
	private Long id;
	private Post post;

	private PostDto(Long id, Post post) {
		this.id = id;
		this.post = post;
	}

	// 인스턴스를 반환하는 정적 팩토리 메서드 오버로드: 파라미터가 Map.Entry인 경우
	public static PostDto of(Map.Entry<Long, Post> entry) {
		if (entry == null) {
			return null;
		}
		return new PostDto(entry.getKey(), entry.getValue());
	}

	// 인스턴스를 반환하는 정적 팩토리 메서드 오버로드: 파라미터가 Long, Post인 경우
	// public static PostDto of(Long id, Post post) {
	// 	return new PostDto(id, post);
	// }

	// 여러 개의 게시글 데이터를 리스트로 반환
	public static List<PostDto> listOf(Map<Long, Post> map) {
		return map.entrySet().stream()
			.map(PostDto::of).toList();
	}

	public Long getId() {
		return this.id;
	}

	public Post getPost() {
		return this.post;
	}
}
