package com.CDC.GuardiaBackend.Controllers;

import com.CDC.GuardiaBackend.DTO.LoginRequest;
import com.CDC.GuardiaBackend.Entities.User;
import com.CDC.GuardiaBackend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.CDC.GuardiaBackend.Services.UserService;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/cdc/")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("user")
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @PostMapping("user")
    public User createUser(@RequestBody User user) {
        //     Encriptar la contraseña antes de guardar al usuario
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        return userRepository.save(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {

        try {
            // Autenticar al usuario
            UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(userDetails, loginRequest.getPassword(), userDetails.getAuthorities());

            //autorizado para acceder a paginas protegidas
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Obtener el atributo de la sesión
            //Mantiene el estado del usuario 
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("userSession", userDetails);

            return ResponseEntity.ok().body("Login exitoso!");
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.ok().body("Inicio de sesión fallido");

        }
    }

}
