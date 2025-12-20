package com.example.demo.config;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/auth/register")
                )
                .authorizeHttpRequests(auth -> auth

                        // PUBLIC
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                        // ===== CATALOG (artists/albums/tracks) =====
                        // Чтение — всем авторизованным
                        .requestMatchers(HttpMethod.GET, "/tracks/**", "/albums/**", "/artists/**")
                        .authenticated()

                        // Изменение каталога — только ADMIN
                        .requestMatchers(HttpMethod.POST, "/tracks/**", "/albums/**", "/artists/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/tracks/**", "/albums/**", "/artists/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/tracks/**", "/albums/**", "/artists/**")
                        .hasRole("ADMIN")

                        // ===== PLAYLISTS =====
                        // Смотреть плейлисты — авторизованным (потом можно ограничить до “только свои”)
                        .requestMatchers(HttpMethod.GET, "/playlists/**")
                        .authenticated()

                        // Операции с плейлистами — USER и ADMIN
                        .requestMatchers(HttpMethod.POST, "/playlists/**")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/playlists/**")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/playlists/**")
                        .hasAnyRole("USER", "ADMIN")

                        // ===== USERS endpoint (если оставляешь) =====
                        // Админ смотрит всех и удаляет
                        .requestMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> {});

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // База для будущей аутентификации "логин+пароль":
    // Security уже умеет грузить пользователя по username
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .authorities(user.getRole())
                    .build();
        };
    }
}
