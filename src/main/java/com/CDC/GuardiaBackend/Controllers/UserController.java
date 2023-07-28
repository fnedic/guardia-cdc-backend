package com.CDC.GuardiaBackend.Controllers;

import com.CDC.GuardiaBackend.Entities.User;
import com.CDC.GuardiaBackend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import com.CDC.GuardiaBackend.Services.UserService;
import java.util.List;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/cdc/")
public class UserController {
    
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
    
    
    
    @GetMapping("user")
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
    @PostMapping("user")
    public User createUser(@RequestBody User user){
         // Encriptar la contraseña antes de guardar al usuario
//        String encryptedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encryptedPassword);
        return userRepository.save(user);
    }
}