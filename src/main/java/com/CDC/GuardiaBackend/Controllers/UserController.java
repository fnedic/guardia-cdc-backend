package com.CDC.GuardiaBackend.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CDC.GuardiaBackend.Configs.UserAuthenticationProvider;
import com.CDC.GuardiaBackend.Entities.User;
import com.CDC.GuardiaBackend.Exceptions.AppException;
import com.CDC.GuardiaBackend.Exceptions.MyException;
import com.CDC.GuardiaBackend.Repositories.UserRepository;
import com.CDC.GuardiaBackend.Services.UserService;
import com.CDC.GuardiaBackend.dtos.UserDto;

@RestController
@RequestMapping("/cdc")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserAuthenticationProvider userAuthenticationProvider;

    @GetMapping("/user")
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserToEdit(@PathVariable String id) throws MyException {

        try {

            Optional<User> optional = userRepository.findById(id);

            if (optional.isPresent()) {

                User user = new User();

                user.setEmail(optional.get().getEmail());
                user.setDni(optional.get().getDni());
                user.setName(optional.get().getName());
                user.setLastname(optional.get().getLastname());
                user.setMedicalRegistration(optional.get().getMedicalRegistration());
                user.setId(optional.get().getId());
                user.setStatus(optional.get().getStatus());

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
            } else {
                throw new AppException("Usuario no encontrado", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new AppException("Usuario no encontrado", HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User updatedUser) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar los campos necesarios del usuario
        existingUser.setName(updatedUser.getName());
        existingUser.setLastname(updatedUser.getLastname());
        existingUser.setDni(updatedUser.getDni());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setMedicalRegistration(updatedUser.getMedicalRegistration());
        if (updatedUser.getStatus().toString().equals("ACTIVE")
                || updatedUser.getStatus().toString().equals("INACTIVE")) {
            existingUser.setStatus(updatedUser.getStatus());
        }

        // Guardar el usuario actualizado en la base de datos
        User savedUser = userRepository.save(existingUser);

        return ResponseEntity.ok().body(savedUser);

    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable String id) throws MyException {

        try {

            Optional<User> optionalProtocol = userRepository.findById(id);
            if (optionalProtocol.isPresent()) {
                userRepository.deleteById(id);
                return null;
            } else {
                throw new MyException("Usuario no encontrado");
            }

        } catch (Exception e) {
            throw new MyException("Error al procesar solicitud en el controlador!");
        }
    }

    @GetMapping("/user/role")
    public ResponseEntity<?> getRole(@RequestHeader("Authorization") String authorizationHeader) throws MyException {
        try {

            String token = authorizationHeader.substring(7);
            if (authorizationHeader.length() >= 7 && authorizationHeader.startsWith("Bearer ")) {

                UserDto userDto = userAuthenticationProvider.getUser(token);

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(userDto);
            } else {
                return ResponseEntity.ok(null);
            }
        } catch (Exception e) {
            throw new MyException("CONTROLLER ERROR: Usuario no logueado o inexistente en la DB");
        }
    }

    @GetMapping("/user/profile")
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String authorizationHeader) throws MyException {
        try {

            String token = authorizationHeader.substring(7);
            if (authorizationHeader.length() >= 7 && authorizationHeader.startsWith("Bearer ")) {

                String email = userAuthenticationProvider.getUserEmail(token);

                try {

                    Optional<User> optional = userRepository.findByEmail(email);

                    if (optional.isPresent()) {

                        User user = new User();

                        user.setEmail(optional.get().getEmail());
                        user.setDni(optional.get().getDni());
                        user.setName(optional.get().getName());
                        user.setLastname(optional.get().getLastname());
                        user.setMedicalRegistration(optional.get().getMedicalRegistration());
                        user.setId(optional.get().getId());
                        user.setStatus(optional.get().getStatus());

                        return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
                    } else {
                        throw new AppException("Usuario no encontrado", HttpStatus.BAD_REQUEST);
                    }
                } catch (Exception e) {
                    throw new AppException("Usuario no encontrado", HttpStatus.BAD_REQUEST);
                }
            } else {
                throw new AppException("Usuario no encontrado", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new MyException("CONTROLLER ERROR: Usuario no logueado o inexistente en la DB");
        }
    }

}
