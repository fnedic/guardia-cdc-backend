package com.CDC.GuardiaBackend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.CDC.GuardiaBackend.Services.UserService;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    UserService userService;
}
