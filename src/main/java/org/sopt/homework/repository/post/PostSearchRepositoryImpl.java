package org.sopt.homework.repository.post;

import java.util.List;
import java.util.Optional;

import org.sopt.homework.domain.Post;
import org.sopt.homework.domain.QPost;
import org.sopt.homework.domain.QUser;
import org.sopt.homework.domain.util.post.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostSearchRepositoryImpl implements PostSearchRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Post> findByOptionsOrderByCreatedAtDesc(String author, String title, Tag tag, Pageable pageable) {
		QPost post = QPost.post;
		QUser user = QUser.user;

		List<Post> posts = queryFactory.selectFrom(post)
				.leftJoin(post.user, user).fetchJoin()
				.where(
						authorContaining(author),
						titleContaining(title),
						tagEquals(tag)
				)
				.orderBy(post.createdAt.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		// 검색 대상을 찾지 못할 경우 null이 반환될 수도 있음
		Long totalCount = queryFactory.select(post.count())
				.from(post)
				.leftJoin(post.user, user)
				.where(
						authorContaining(author),
						titleContaining(title),
						tagEquals(tag)
				)
				.fetchOne();

		return new PageImpl<>(posts, pageable, Optional.ofNullable(totalCount).orElse(0L));
	}

	private BooleanExpression authorContaining(String author) {
		if (author == null || author.isEmpty()) {
			return null;
		}
		return QPost.post.user.name.contains(author);
	}

	private BooleanExpression titleContaining(String title) {
		if (title == null || title.isEmpty()) {
			return null;
		}
		return QPost.post.title.contains(title);
	}

	private BooleanExpression tagEquals(Tag tag) {
		if (tag == null) {
			return null;
		}
		return QPost.post.tag.eq(tag);
	}
}
