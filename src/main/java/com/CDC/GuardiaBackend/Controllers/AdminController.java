package com.CDC.GuardiaBackend.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.CDC.GuardiaBackend.Entities.Protocol;
import com.CDC.GuardiaBackend.Repositories.ProtocolRepository;
import com.CDC.GuardiaBackend.Repositories.UserRepository;
import com.CDC.GuardiaBackend.Services.ProtocolService;
import com.CDC.GuardiaBackend.Services.UserService;


@CrossOrigin(origins = "http://localhost:3000")
@Controller
@RequestMapping("/admin/")
public class AdminController {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProtocolService protocolService;
    @Autowired
    ProtocolRepository protocolRepository;

    @GetMapping("protocol")
    public List<Protocol> getProtocol() {
        return protocolRepository.findAll();
    }
}
