package org.example.forum.DAO;

import org.example.forum.Entities.User;

import java.util.Optional;

public interface UserDAO {
    User getUserById(Long id);
    void saveUser (User user);
    Optional<User> getUserByEmail(String email);
}
