package com.CDC.GuardiaBackend.Controllers;

import com.CDC.GuardiaBackend.Configs.UserAuthenticationProvider;
import com.CDC.GuardiaBackend.Entities.User;
import com.CDC.GuardiaBackend.Exceptions.MyException;
import com.CDC.GuardiaBackend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.CDC.GuardiaBackend.Services.UserService;
import com.CDC.GuardiaBackend.dtos.UserDto;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@CrossOrigin(origins = "http://localhost:3000")
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
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el id: " + id));
        return ResponseEntity.ok(user);
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
        existingUser.setStatus(updatedUser.getStatus());

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

}
