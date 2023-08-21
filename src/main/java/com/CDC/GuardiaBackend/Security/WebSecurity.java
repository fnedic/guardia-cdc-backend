package com.CDC.GuardiaBackend.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import com.CDC.GuardiaBackend.Services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    @Autowired
    private UserService userService;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    private static final String[] PUBLIC_URLS = {
            "/cdc/login",
            "/cdc/user",
            "/cdc/user/{id}",
            "/cdc/user/delete/{id}",

            "/protocol/list",
            "/protocol/view/{id}",
            "/protocol/mostviewed",
            "/protocol/mostviewed/{id}",
            "/protocol/upload",
            "/protocol/delete/{id}",

    };

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(PUBLIC_URLS).permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
        return http.build();
    }

}
