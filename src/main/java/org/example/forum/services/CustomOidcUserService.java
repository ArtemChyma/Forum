package org.example.forum.services;

import org.example.forum.Entities.User;
import org.example.forum.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final UserRepository userRepository;

    @Autowired
    public CustomOidcUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = new DefaultOidcUser(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                userRequest.getIdToken()
        );
        System.out.println("Google user: " + oidcUser.getAttributes());
        String name = oidcUser.getGivenName();
        String surname = oidcUser.getFamilyName();
        String email = oidcUser.getEmail();
        String googleId = oidcUser.getSubject();

        Optional<User> user = userRepository.findUserByEmail(email);

        if (user.isPresent()) {
            if (user.get().getGoogleId() != null) {
                if (!googleId.equals(user.get().getGoogleId()))
                    System.out.println("Email already taken by another user");
                else
                    return oidcUser;
            } else
                throw new OAuth2AuthenticationException("Email already registered without Google linking");
        } else {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setGoogleId(googleId);
            newUser.setRole("ROLE_USER");
            newUser.setFirstName(name);
            newUser.setSecondName(surname);
            userRepository.save(newUser);
        }
        return oidcUser;
    }
}
