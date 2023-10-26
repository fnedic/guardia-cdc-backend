package com.CDC.GuardiaBackend.Configs;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.CDC.GuardiaBackend.Exceptions.MyException;
import com.CDC.GuardiaBackend.Services.UserService;
import com.CDC.GuardiaBackend.dtos.UserDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserAuthenticationProvider {
    
    @Value("${SECRET_KEY:default-value}")
    private String secretKey;

    private final UserService userService;

    @PostConstruct
    protected void init() {
        // this is to avoid having the raw secret key available in the JVM
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String email, String status, String role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 2200000); // tiempo en milisegundos

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withSubject(email)
                .withClaim("status", status) // Agregar la variable "status"
                .withClaim("role", role) // Agregar la variable "role"
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(algorithm);
    }

    public Authentication validateToken(String token) {

        if (token != null && !token.equals("null")) {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            JWTVerifier verifier = JWT.require(algorithm)
                    .build();

            DecodedJWT decoded = verifier.verify(token);

            UserDto user = userService.findByEmail(decoded.getSubject());

            return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

        } else
            return null;

    }

    public UserDto getUser(String token) throws MyException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            JWTVerifier verifier = JWT.require(algorithm)
                    .build();

            DecodedJWT decoded = verifier.verify(token);

            UserDto user = new UserDto();

            user.setStatus(decoded.getClaim("status").asString());
            user.setRole(decoded.getClaim("role").asString());
            user.setEmail(decoded.getSubject());

            return user;
        } catch (Exception e) {
            throw new MyException("Usuario no logueado o inexistente en la DB");
        }
    }

    public String getUserEmail(String token) throws MyException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            JWTVerifier verifier = JWT.require(algorithm)
                    .build();

            DecodedJWT decoded = verifier.verify(token);

            return decoded.getSubject();
        } catch (Exception e) {
            throw new MyException("(getUserEmail) Usuario no logueado o inexistente en la DB");
        }
    }

}
