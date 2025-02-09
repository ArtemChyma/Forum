package org.example.forum.configs;

import org.example.forum.DAO.UserDAOImpl;
import org.example.forum.repositories.UserRepository;
import org.example.forum.services.CustomOidcUserService;
import org.example.forum.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    @Autowired
    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService, UserRepository userRepository) {
        this.customUserDetailsService = customUserDetailsService;
        this.userRepository = userRepository;
    }
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomOidcUserService customOidcUserService() {
        return new CustomOidcUserService(userRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(req -> req.requestMatchers("/login", "/register", "/oauth2/**")
                .permitAll().anyRequest().authenticated())
                .formLogin(f -> f.loginPage("/login")
                        .usernameParameter("email")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/main-page", true).permitAll()
                ).logout(l -> l.invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(
                                new AntPathRequestMatcher(("logout"))
                        ).logoutSuccessUrl("/login?logout").permitAll())
                .oauth2Login(o -> o
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(customOidcUserService()))
                        .defaultSuccessUrl("/main-page", true).permitAll())
                .logout(l -> l
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(
                                new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());
        return http.build();
    }
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}
