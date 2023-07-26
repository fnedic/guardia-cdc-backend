package com.CDC.GuardiaBackend.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import com.CDC.GuardiaBackend.Services.UserService;

@Configuration
public class WebSecurity {
    
    @Autowired
    public UserService userService; 

}
