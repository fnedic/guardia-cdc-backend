package com.CDC.GuardiaBackend.Controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.CDC.GuardiaBackend.Configs.UserAuthenticationProvider;
import com.CDC.GuardiaBackend.Repositories.UserRepository;
import com.CDC.GuardiaBackend.Services.UserService;
import com.CDC.GuardiaBackend.dtos.CredentialsDto;
import com.CDC.GuardiaBackend.dtos.SignUpDto;
import com.CDC.GuardiaBackend.dtos.UserDto;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Valid CredentialsDto credentialsDto) {
        UserDto userDto = userService.login(credentialsDto);
        userDto.setToken(userAuthenticationProvider.createToken(userDto.getEmail()));
        userDto.setRole(userRepository.findByEmail(credentialsDto.getEmail()).get().getRole().toString());
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid SignUpDto user) {
        UserDto createdUser = userService.register(user);
        createdUser.setToken(userAuthenticationProvider.createToken(user.getEmail()));
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }

}
