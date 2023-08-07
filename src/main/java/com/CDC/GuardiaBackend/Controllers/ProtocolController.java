package com.CDC.GuardiaBackend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CDC.GuardiaBackend.Entities.Protocol;
import com.CDC.GuardiaBackend.Repositories.ProtocolRepository;
import com.CDC.GuardiaBackend.Services.ProtocolService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/protocol/")
public class ProtocolController {
    
    @Autowired
    ProtocolService protocolService;

    @Autowired
    ProtocolRepository protocolRepository;

    @PostMapping("upload")
    public Protocol createProtocol (@RequestBody Protocol protocol) {

        return protocolRepository.save(protocol);
    }
}
