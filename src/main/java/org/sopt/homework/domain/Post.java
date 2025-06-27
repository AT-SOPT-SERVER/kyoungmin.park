package org.sopt.homework.domain;

import java.util.UUID;

import org.sopt.homework.domain.util.post.Tag;
import org.sopt.homework.domain.util.validator.Validator;
import org.sopt.homework.global.exception.GlobalException;
import org.sopt.homework.global.exception.message.ExceptionMessage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class Post extends BaseEntity {
	@Id
	@Column(name = "post_id", columnDefinition = "BINARY(16)", nullable = false, unique = true, updatable = false)
	private UUID id;

	// 이모지만 30개 넣으면 varChar 기본 크기인 255바이트가 모자랄 수 있음
	@Column(name = "title", nullable = false, length = 1024)
	private String title;

	// 이모지만 1000자인 경우에 varchar의 최대길이를 넘어갈 수 있으므로 text로 설정
	@Lob
	@Column(name = "content", columnDefinition = "TEXT", nullable = false)
	private String content;

	@Column(name = "like_count")
	private int likeCount;

	@Enumerated(EnumType.STRING)
	@Column(name = "tag")
	private Tag tag;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder
	private Post(String title, String content, String tagName, User user) {
		// 검증 결과에 따라 예외처리
		validateTitle(title);
		validateContent(content);
		this.id = UUID.randomUUID();
		this.title = title;
		this.content = content;
		this.tag = Tag.fromString(tagName);
		this.user = user;
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

	public void increaseLikeCount() {
		this.likeCount++;
	}

	public void decreaseLikeCount() {
		this.likeCount--;
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
			throw new GlobalException((ExceptionMessage.OVER_LENGTH_POST_TITLE));
		}
	}

	private void validateContent(String content) {
		if (content == null) {
			throw new GlobalException(ExceptionMessage.BAD_REQUEST);
		}
		if (Validator.isEmpty(content)) {
			throw new GlobalException(ExceptionMessage.EMPTY_CONTENT);
		}
		if (Validator.isOverLength(content, 1000)) {
			throw new GlobalException((ExceptionMessage.OVER_LENGTH_POST_CONTENT));
		}
	}
}
