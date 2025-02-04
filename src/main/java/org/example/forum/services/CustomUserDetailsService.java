package org.example.forum.services;

import org.example.forum.DAO.UserDAO;
import org.example.forum.DAO.UserDAOImpl;
import org.example.forum.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDAOImpl userDAOImpl;

    @Autowired
    public CustomUserDetailsService(UserDAOImpl userDAOImpl) {
        this.userDAOImpl = userDAOImpl;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new CustomUserDetails(userDAOImpl.getUserByEmail(username));
    }
}
