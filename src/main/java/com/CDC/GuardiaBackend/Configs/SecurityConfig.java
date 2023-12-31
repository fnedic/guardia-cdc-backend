package com.CDC.GuardiaBackend.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    private final UserAuthenticationProvider userAuthenticationProvider;

    private static final String[] PUBLIC_URLS = {
            "/login",
            "/register",
    };

    private static final String[] GET_URLS = {

            "/cdc/user",
            "/cdc/user/{id}",
            "/cdc/user/profile",
            "/cdc/user/delete/{id}",
            "/cdc/user/role",
            "/cdc/user/profile",

            "/protocol/list",
            "/protocol/view/{id}",
            "/protocol/mostviewed",
            "/protocol/delete/{id}",
            "/protocol/mostviewed/{id}",

            "/procedure/list",
            "/procedure/delete/{id}",

            "/video/get",
            "/video/update/{id}",

            "/notice/get",

            "/calendar/get-events",
            "/calendar/get-user-events",
            "/calendar/get-requested-events",
            "/calendar/get-requested-others",
            "/calendar/get-unpublished-events",
            "/calendar/req-change/{id}",
            "/calendar/req-cancel/{id}",
            "/calendar/req-accept/{id}",
            "/calendar/req-approve/{id}",
            "/calendar/req-change-cancel/{id}",
            "/calendar/get-changed-events"
    };

    private final static String[] POST_URLS = {
            "/protocol/upload",
            "/video/register",
            "/notice/create",
            "/calendar/publish-events",

    };

    private static final String[] PUT_URLS = {
            "/cdc/user/{id}",
            "/calendar/edit-event/{id}",

    };

    public static final String[] DELETE_URLS = {
            "/video/delete/{id}",
            "/calendar/delete/{id}"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().authenticationEntryPoint(userAuthenticationEntryPoint)
                .and()
                .addFilterBefore(new AuthFilter(userAuthenticationProvider),
                        BasicAuthenticationFilter.class)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(requests -> requests
                        .antMatchers(HttpMethod.POST, PUBLIC_URLS).permitAll()
                        .antMatchers(HttpMethod.POST, POST_URLS).authenticated()
                        .antMatchers(HttpMethod.GET, GET_URLS).authenticated()
                        .antMatchers(HttpMethod.PUT, PUT_URLS).authenticated()
                        .antMatchers(HttpMethod.DELETE, DELETE_URLS).authenticated()
                        .anyRequest().authenticated())
                .httpBasic().disable()
                .formLogin().disable();
        return http.build();
    }
}
