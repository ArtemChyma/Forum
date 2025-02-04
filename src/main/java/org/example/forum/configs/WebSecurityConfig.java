package org.example.forum.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(req -> req.requestMatchers("/login", "/register")
                .permitAll().anyRequest().authenticated())
                .formLogin(f -> f.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/main_page", true).permitAll()
                ).logout(l -> l.invalidateHttpSession(true)
                        .clearAuthentication(true).logoutRequestMatcher(
                                new AntPathRequestMatcher(("logout"))
                        ).logoutSuccessUrl("/login?logout").permitAll());
        return http.build();
    }
}
