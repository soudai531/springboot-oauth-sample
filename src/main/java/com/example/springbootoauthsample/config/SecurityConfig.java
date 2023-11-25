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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                // 認可の設定
                .requestMatchers("/hello").permitAll()
				.requestMatchers("/login/oauth2", "/login/oauth2/authorization").permitAll()
                .anyRequest().authenticated()
        ).oauth2Login(oauth2 -> oauth2
				.authorizationEndpoint(authorization -> authorization
						.baseUri("/api/login/oauth2/authorization")
				)
        ).formLogin(Customizer.withDefaults());
        return http.build();
    }
}
