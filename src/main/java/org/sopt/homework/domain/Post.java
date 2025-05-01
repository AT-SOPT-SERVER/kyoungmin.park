package org.sopt.homework.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.sopt.homework.domain.util.Tag;
import org.sopt.homework.domain.util.validator.Validator;
import org.sopt.homework.global.exception.GlobalException;
import org.sopt.homework.global.exception.message.ExceptionMessage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "post")
public final class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id", nullable = false, unique = true, updatable = false)
	private long id;

	// 이모지만 30개 넣으면 varChar 기본 크기인 255바이트가 모자랄 수 있음
	@Column(name = "title", nullable = false, length = 1024)
	private String title;

	// 이모지만 1000자인 경우에 varchar의 최대길이를 넘어갈 수 있으므로 text로 설정
	@Lob
	@Column(name = "content", columnDefinition = "TEXT", nullable = false)
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(name = "tag")
	private Tag tag;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	// ON DELETE SET NULL 제약조건 적용됨
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User author;

	public Post(String title, String content, String tagName, User author) {
		// 검증 결과에 따라 예외처리
		validateTitle(title);
		validateContent(content);
		this.title = title;
		this.content = content;
		this.tag = Tag.fromString(tagName);
		this.author = author;
	}

	protected Post() {
	}

	public long getId() {
		return this.id;
	}

	public String getTitle() {
		return this.title;
	}

	public String getContent() {
		return this.content;
	}

	public Tag getTag() {
		return this.tag;
	}

	public User getAuthor() {
		return this.author;
	}

	// 게시글 제목 수정
	public void updateTitle(String title) {
		validateTitle(title);
		this.title = title;
	}

	// 게시글 내용 수정
	public void updateContent(String content) {
		validateContent(content);
		this.content = content;
	}

	// 게시글 태그 수정
	public void updateTag(String tagName) {
		this.tag = Tag.fromString(tagName);
	}

	private void validateTitle(String title) {
		if (title == null) {
			throw new GlobalException(ExceptionMessage.BAD_REQUEST);
		}
		if (Validator.isEmpty(title)) {
			throw new GlobalException(ExceptionMessage.EMPTY_TITLE);
		}
		if (Validator.isOverLength(title, 30)) {
			throw new GlobalException((ExceptionMessage.OVER_LENGTH_TITLE));
		}
	}

	private void validateContent(String content) {
		if (content == null) {
			throw new GlobalException(ExceptionMessage.BAD_REQUEST);
		}
		if (Validator.isEmpty(content)) {
			throw new GlobalException(ExceptionMessage.EMPTY_TITLE);
		}
		if (Validator.isOverLength(content, 1000)) {
			throw new GlobalException((ExceptionMessage.OVER_LENGTH_TITLE));
		}
	}
}
