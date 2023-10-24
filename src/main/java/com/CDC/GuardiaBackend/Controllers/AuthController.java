package com.CDC.GuardiaBackend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.CDC.GuardiaBackend.Configs.UserAuthenticationProvider;
import com.CDC.GuardiaBackend.Exceptions.AppException;
import com.CDC.GuardiaBackend.Repositories.UserRepository;
import com.CDC.GuardiaBackend.Services.UserService;
import com.CDC.GuardiaBackend.dtos.CredentialsDto;
import com.CDC.GuardiaBackend.dtos.SignUpDto;
import com.CDC.GuardiaBackend.dtos.UserDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
        userDto.setToken(userAuthenticationProvider.createToken(userDto.getEmail(), userDto.getStatus(), userDto.getRole()));
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid SignUpDto user) {
        try {
            userService.register(user);
            String successMsg = "Datos Registrados! Aguarde autorización del administrador.";
            return ResponseEntity.status(HttpStatus.CREATED).body(successMsg);
        } catch (AppException e) { // Captura la excepción específica
            String errorMsg = "Email ya registrado! Inicie sesíon o contacte al administrador."; // Obtén el mensaje de error de la excepción
            return ResponseEntity.status(e.getStatus()).body(errorMsg);
        } catch (Exception e) {
            String errorMsg = "Error al registrarse, intente nuevamente!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
    }

}
