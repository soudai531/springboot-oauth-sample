package com.example.springbootoauthsample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        // 認可の設定
                        //上記以外のURLパスがきた場合は認証必須とする設定
                        .anyRequest().authenticated()
                )
                .oauth2Login(Customizer.withDefaults());
        http.logout(logout -> logout.logoutSuccessUrl("/").permitAll());
        return http.build();
    }
}
