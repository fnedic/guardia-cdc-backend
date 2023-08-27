package com.CDC.GuardiaBackend.Services;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.CDC.GuardiaBackend.Entities.User;
import com.CDC.GuardiaBackend.Enums.Roles;
import com.CDC.GuardiaBackend.Enums.UserStatus;
import com.CDC.GuardiaBackend.Repositories.UserRepository;
import com.CDC.GuardiaBackend.dtos.CredentialsDto;
import com.CDC.GuardiaBackend.dtos.SignUpDto;
import com.CDC.GuardiaBackend.dtos.UserDto;
import com.CDC.GuardiaBackend.Exceptions.AppException;
import com.CDC.GuardiaBackend.Exceptions.MyException;
import com.CDC.GuardiaBackend.Mappers.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService  {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ---- Service USER ------ (Se usara para crear, leer, modificar y borrar )
    // REGISTER
    public UserDto register(SignUpDto userDto) {
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());

        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(userDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.getPassword())));
        user.setDni(userDto.getDni());
        user.setMedicalRegistration(userDto.getMedicalRegistration());
        user.setRole(Roles.USER);
        user.setStatus(UserStatus.PENDING);
        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    //LOGIN
    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByEmail(credentialsDto.getEmail())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            System.out.println("INICIO DE SESION CORRECTO");
            return userMapper.toUserDto(user);
        }
        System.out.println("USUARIO O CONTRASEÑAS NO VALIDOS");
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    // READ
    public User getById(String id) throws MyException {

        try {
            return userRepository.findById(id).get();
        } catch (Exception e) {
            throw new MyException("ERROR, Usuario no encontrado");
        }
    }

    public User getByEmail(String email) throws MyException {
        try {

            return userRepository.searchByEmail(email);

        } catch (Exception e) {
            throw new MyException("ERROR, Usuario no encontrado");
        }
    }

    public List<User> userList() throws MyException {
        try {

            List<User> usersList = new ArrayList<>();

            usersList = userRepository.findAll();

            return usersList;
        } catch (Exception e) {
            throw new MyException("ERROR, Lista de usuarios no encontrada");
        }

    }

    // UPDATE
    public void updateStatus(User user) throws MyException {
        if (user.getStatus().name().equals("ACTIVE")) {

            user.setStatus(UserStatus.INACTIVE);

        } else if (user.getStatus().name().equals("INACTIVE")) {

            user.setStatus(UserStatus.ACTIVE);

        } else if (user.getStatus().name().equals("PENDING")) {

            user.setStatus(UserStatus.ACTIVE);
        }

        userRepository.save(user);
    }

    // DELETE
    @Transactional
    public void deleteUser(String id) throws MyException {
        try {

            User originalUser = userRepository.findById(id).get();
            originalUser.setStatus(UserStatus.INACTIVE);

        } catch (Exception e) {
            throw new MyException("ERROR, Usuario no borrado");
        }
    }

    // VALIDATIONS
    @Transactional
    public boolean validateEmail(User user) throws MyException {
        try {

            boolean validator = false;

            if (userRepository.searchByEmail(user.getEmail()) != null) {
                validator = true;
            }
            // si el validador se vuelve verdadero, es porque hay coincidencia de emails.
            return validator;

        } catch (Exception e) {
            throw new MyException("ERROR, Email ocupado");

        }

    }

    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }
}
