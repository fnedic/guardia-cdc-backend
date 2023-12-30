package com.CDC.GuardiaBackend.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CDC.GuardiaBackend.Entities.Protocol;
import com.CDC.GuardiaBackend.Exceptions.MyException;
import com.CDC.GuardiaBackend.Repositories.ProtocolRepository;

@RestController
@RequestMapping("/procedure")
public class ProcedureController {

    @Autowired
    ProtocolRepository protocolRepository;

    @GetMapping("/list")
    public List<Map<String, Object>> proceduresList() throws MyException {

        String criteria = "PROCEDIMIENTO";
        List<Protocol> allProtocols = protocolRepository.findAll();

        List<Map<String, Object>> proceduresList = allProtocols.stream()
                .filter(protocol -> criteria.equals(protocol.getProtocolGroup()))
                .map(protocol -> {
                    Map<String, Object> procedureMap = new HashMap<>();
                    procedureMap.put("id", protocol.getId());
                    procedureMap.put("title", protocol.getTitle());
                    procedureMap.put("intro", protocol.getIntro());
                    procedureMap.put("protocolGroup", protocol.getProtocolGroup());
                    procedureMap.put("publicationDate", protocol.getPublicationDate());
                    procedureMap.put("views", protocol.getViews());
                    procedureMap.put("videoLink", protocol.getVideoLink());

                    return procedureMap;
                })
                .collect(Collectors.toList());

        return proceduresList;
    }


    @GetMapping("/delete/{id}")
    public String deleteProcedure(@PathVariable String id) throws MyException {

        try {

            Optional<Protocol> optionalProcedure = protocolRepository.findById(id);
            if (optionalProcedure.isPresent()) {
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
