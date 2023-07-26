package com.CDC.GuardiaBackend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.CDC.GuardiaBackend.Repositories.UserRepository;
import com.CDC.GuardiaBackend.Services.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
}
