package com.CDC.GuardiaBackend.Controllers;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CDC.GuardiaBackend.Entities.Protocol;
import com.CDC.GuardiaBackend.Exceptions.AppException;
import com.CDC.GuardiaBackend.Exceptions.MyException;
import com.CDC.GuardiaBackend.Repositories.ProtocolRepository;
import com.CDC.GuardiaBackend.Services.ProtocolService;
import com.CDC.GuardiaBackend.Services.VideoService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/protocol")
public class ProtocolController {

    @Autowired
    ProtocolService protocolService;
    @Autowired
    VideoService videoService;
    @Autowired
    ProtocolRepository protocolRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> createProtocol(@RequestBody Protocol protocol) {

        try {
            protocolService.create(protocol);
            return new ResponseEntity<>("Contenido publicado correctamente!", HttpStatus.OK);
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Protocol> protocolRender(@PathVariable String id) throws MyException {
        try {
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

    @GetMapping("/list")
    public List<Protocol> protocolList() throws MyException {
        return protocolRepository.findAll();
    }

    @GetMapping("/mostviewed")
    public ResponseEntity<Protocol> getMostViewed() throws MyException {

        try {

            Protocol mostViewedProtocol = protocolService.getMostViewed();
            return ResponseEntity.ok(mostViewedProtocol);

        } catch (Exception e) {
            throw new MyException("Error al procesar solicitud en el controlador!");
        }
    }

    @GetMapping("/mostviewed/{id}")
    public void mostViewedProtocol(@PathVariable String id) throws MyException {

        try {

            Optional<Protocol> optionalProtocol = protocolRepository.findById(id);

            if (optionalProtocol.isPresent()) {
                Protocol protocol = optionalProtocol.get();
                protocol.setViews(optionalProtocol.get().getViews() + 1);
                System.out.println("             +1            ");
                protocolRepository.save(protocol);
            }

        } catch (Exception e) {
            throw new MyException("Error al procesar numero de visita");
        }

    }

    @GetMapping("/delete/{id}")
    public String deleteProtocol(@PathVariable String id) throws MyException {

        try {

            Optional<Protocol> optionalProtocol = protocolRepository.findById(id);
            if (optionalProtocol.isPresent()) {
                protocolRepository.deleteById(id);
                return null;
            } else {
                throw new MyException("Protocolo no encontrado");
            }

        } catch (Exception e) {
            throw new MyException("Error al procesar solicitud en el controlador!");
        }
    }

}
