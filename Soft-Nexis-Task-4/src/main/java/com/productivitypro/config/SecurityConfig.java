package com.productivitypro.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService users(PasswordEncoder encoder) {
        UserDetails user = User.withUsername("admin")
                .password(encoder.encode("secret"))
                .roles("USER")
                .build();

        UserDetails user2 = User.withUsername("user")
                .password(encoder.encode("password"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user, user2);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .csrf().disable() // for simplicity; enable for browser form submissions if needed
          .authorizeHttpRequests(auth -> auth
              .requestMatchers("/h2-console/**").permitAll()
              .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
              .anyRequest().authenticated()
          )
          .httpBasic();

        // allow H2 console frames
        http.headers().frameOptions().sameOrigin();

        return http.build();
    }
}
