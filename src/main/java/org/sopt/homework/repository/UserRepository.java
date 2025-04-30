package org.sopt.homework.repository;

import org.sopt.homework.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsById(long id);
}
