package org.example.forum.DAO;

import org.example.forum.Entities.User;

public interface UserDAO {
    User getUserById(Long id);
    void saveUser (User user);
}
