package com.example.clonestagram.Security;


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
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // csrf 잠시 off
        http.csrf((csrf) -> csrf.disable());

        http.authorizeHttpRequests((authorize) ->
                authorize.requestMatchers("/**").permitAll()
        );

        // form으로 로그인 하겟다는 뜻
        http.formLogin((formLogin) -> formLogin.loginPage("/login")
                .defaultSuccessUrl("/main")
                .failureUrl("/login")
        );

        // 로그아웃 기능
        http.logout( logout -> logout.logoutUrl("/logout") );

        return http.build();
    }
}
