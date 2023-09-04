package com.CDC.GuardiaBackend.Configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    private final UserAuthenticationProvider userAuthenticationProvider;

    private static final String[] PUBLIC_URLS = {
            "/login",
            "/register",
            "/protocol/mostviewed/{id}",
            "/protocol/upload"

    };

    private static final String[] GET_URLS = {

            "/cdc/user",
            "/cdc/user/{id}",
            "/cdc/user/delete/{id}",

            "/protocol/list",
            "/protocol/view/{id}",
            "/protocol/mostviewed",
            "/protocol/delete/{id}",
    };

    private final static String[] POST_URLS = {
            "/cdc/user/{id}" };

    // private static final String[] PUT_URLS = {
    // };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().authenticationEntryPoint(userAuthenticationEntryPoint)
                .and()
                .addFilterBefore(new AuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(requests -> requests
                        .antMatchers(HttpMethod.POST, PUBLIC_URLS).permitAll()
                        .antMatchers(HttpMethod.GET, GET_URLS).permitAll()
                        .antMatchers(HttpMethod.PUT, POST_URLS).permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }
}
