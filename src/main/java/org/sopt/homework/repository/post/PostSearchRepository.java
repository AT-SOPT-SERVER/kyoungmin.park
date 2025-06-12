package org.sopt.homework.repository.post;

import org.sopt.homework.domain.Post;
import org.sopt.homework.domain.util.post.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostSearchRepository {
	Page<Post> findByOptionsOrderByCreatedAtDesc(String author, String title, Tag tag, Pageable pageable);
}
