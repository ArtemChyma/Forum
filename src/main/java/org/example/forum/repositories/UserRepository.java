package org.example.forum.repositories;

import org.example.forum.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Long, User> {

}
