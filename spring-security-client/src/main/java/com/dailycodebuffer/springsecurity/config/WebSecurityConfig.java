package com.dailycodebuffer.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private static final String[] WHITELIST_URLS = {"/hello","/register","/verifyRegistration", "/resendToken","/resetPassword","/saveNewPassword", "/changePassword"};
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain( HttpSecurity http ) throws Exception{
        http.cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                /*In Spring Security 5.8, the antMatchers, mvcMatchers, and regexMatchers
                 methods were deprecated in favor of new requestMatchers methods.*/
                .requestMatchers(WHITELIST_URLS)
                .permitAll();
        return http.build();
    }
}
