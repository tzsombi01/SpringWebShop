package com.tzsombi.webshop.repositories;

import com.tzsombi.webshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String userName);

    boolean existsByEmail(String email);
}
