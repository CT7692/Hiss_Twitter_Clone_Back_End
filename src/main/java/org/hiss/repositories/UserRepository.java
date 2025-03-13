package org.hiss.repositories;

import org.hiss.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByName(String name);
    User findByName(String name);
}
