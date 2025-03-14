package org.example.forum.DAO;

import org.example.forum.Entities.User;
import org.example.forum.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDAOImpl implements UserDAO{
    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
