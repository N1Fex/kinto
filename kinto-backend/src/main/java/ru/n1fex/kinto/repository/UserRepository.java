package ru.n1fex.kinto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.n1fex.kinto.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getUserByUsernameOrEmail(String username, String email);

    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByUsername(String username);

    Optional<User> getUserById(Long id);
}
