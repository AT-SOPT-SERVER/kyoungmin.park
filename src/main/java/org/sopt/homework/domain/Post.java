package org.sopt.homework.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.sopt.homework.domain.util.validator.Validator;
import org.sopt.homework.global.exception.GlobalException;
import org.sopt.homework.global.exception.message.ExceptionMessage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "post")
public final class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id", nullable = false, unique = true, updatable = false)
	private Long id;

	// 이모지만 30개 넣으면 varChar 기본 크기인 255바이트가 모자랄 수 있음
	@Column(name = "title", nullable = false, length = 1024)
	private String title;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	public Post(String title) {
		// 검증 결과에 따라 예외처리
		validateTitle(title);
		this.title = title;
	}

	public Post() {
	}

	public Long getId() {
		return this.id;
	}

	public String getTitle() {
		return this.title;
	}

	// 게시글 수정 시 사용되는 update 메서드
	public void updateTitle(String title) {
		validateTitle(title);
		this.title = title;
	}

	private void validateTitle(String title) {
		if (title == null) {
			throw new GlobalException(ExceptionMessage.BAD_REQUEST);
		}
		if (Validator.isEmptyTitle(title)) {
			throw new GlobalException(ExceptionMessage.EMPTY_TITLE);
		}
		if (Validator.isOverLengthTitle(title)) {
			throw new GlobalException((ExceptionMessage.OVER_LENGTH_TITLE));
		}
	}
}
