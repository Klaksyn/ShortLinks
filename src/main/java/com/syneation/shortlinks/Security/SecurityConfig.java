package com.syneation.shortlinks.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
            throws Exception {

        return httpSecurity.
                authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/main_pages/**").permitAll()
                        .requestMatchers("/register").not().authenticated()
                        .requestMatchers("/login").not().authenticated()
                        .requestMatchers("/logout").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("login")
                        .defaultSuccessUrl("/profile", true)
                        .permitAll()
                )
                .logout( config -> config
                        .logoutSuccessUrl("/")
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
