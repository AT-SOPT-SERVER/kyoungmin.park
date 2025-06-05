package org.sopt.homework.repository;

import java.util.Optional;
import java.util.UUID;

import org.sopt.homework.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
	boolean existsById(UUID id);

	boolean existsByUsername(String username);

	Optional<User> findByUsername(String username);
}
