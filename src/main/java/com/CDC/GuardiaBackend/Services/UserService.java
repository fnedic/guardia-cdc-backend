package com.CDC.GuardiaBackend.Services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.CDC.GuardiaBackend.Entities.User;
import com.CDC.GuardiaBackend.Enums.Roles;
import com.CDC.GuardiaBackend.Enums.UserStatus;
import com.CDC.GuardiaBackend.Repositories.UserRepository;
import com.CDC.GuardiaBackend.Exceptions.MyException;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

// ---- Service USER ------ (Se usara para crear, leer, modificar y borrar )
    // CREATED
    @Transactional
    public void createUser(User user) throws MyException {

        validateEmail(user);

        try {

            user.setName(user.getName());
            user.setLastname(user.getLastname());
            user.setEmail(user.getEmail());
            user.setPassword(user.getPassword());
            user.setDNI(user.getDNI());
            user.setMedicalRegistration(user.getMedicalRegistration());

            user.setRole(Roles.USER);

            user.setStatus(UserStatus.PENDING);

            userRepository.save(user);

        } catch (Exception e) {
            throw new MyException("ERROR, Usuario no creado");
        }
    }

    //READ
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

    //UPDATE
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

    //DELETE
    @Transactional
    public void deleteUser(String id) throws MyException {
        try {

            User originalUser = userRepository.findById(id).get();
            originalUser.setStatus(UserStatus.INACTIVE);

        } catch (Exception e) {
            throw new MyException("ERROR, Usuario no borrado");
        }
    }

    //VALIDATIONS
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.searchByEmail(email);
        if (user != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + user.getRole().toString());
            authorities.add(p);

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(), user.getPassword(), authorities);
        } else {
            System.out.println("USUARIO NULO");
            throw new UsernameNotFoundException("Usuario no encontrado");

        }
    }
}
