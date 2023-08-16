package com.CDC.GuardiaBackend.Controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CDC.GuardiaBackend.Entities.Protocol;
import com.CDC.GuardiaBackend.Exceptions.MyException;
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
    public Protocol createProtocol(@RequestBody Protocol protocol) {

        return protocolRepository.save(protocol);
    }

    @GetMapping("view")
    public ResponseEntity<Protocol> protocolList() throws MyException {
        try {
            String id = "9b192651-e14b-4a01-bb4a-b3b5374fda27";
            Optional<Protocol> protocolFind = protocolRepository.findById(id);

            if (protocolFind.isPresent()) {
                Protocol protocol = protocolFind.get();
                return ResponseEntity.ok(protocol);
            } else {
                throw new MyException("Protocolo no encontrado");
            }
        } catch (Exception e) {
            throw new MyException("Error al traer protocolo");
        }
    }
}
