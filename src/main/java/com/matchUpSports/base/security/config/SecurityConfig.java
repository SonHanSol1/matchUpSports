package com.matchUpSports.base.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.oauth2Login(oauth -> oauth.loginPage("/member/login"))
                .formLogin(form -> form.loginPage("/member/login"))
                .logout(logout -> logout
                        .invalidateHttpSession(true));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/management/**").hasRole("MANAGE")
                .requestMatchers("/member/login").anonymous()
                .requestMatchers("/member/**").hasAnyRole("USER", "ADMIN", "MANAGE")
                .requestMatchers("/favicon.ico", "/resource/**", "/error",
                        "/image/**", "/js/**").permitAll()
                .anyRequest().authenticated());

        http.csrf(c -> c.ignoringRequestMatchers("/product/more/**"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}