package org.example.forum.services;

import lombok.extern.slf4j.Slf4j;
import org.example.forum.DAO.UserDAO;
import org.example.forum.DAO.UserDAOImpl;
import org.example.forum.Entities.User;
import org.example.forum.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        System.out.println("Looking for user with email: " + username);
        return new CustomUserDetails(user.get());
    }
}
