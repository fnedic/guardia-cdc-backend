package com.CDC.GuardiaBackend.Services;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CDC.GuardiaBackend.Entities.Protocol;
import com.CDC.GuardiaBackend.Exceptions.MyException;
import com.CDC.GuardiaBackend.Repositories.ProtocolRepository;

@Service
public class ProtocolService {

    @Autowired
    private ProtocolRepository protocolRepository;

    public Protocol getMostViewed() throws MyException {

        try {
            List<Protocol> protocols = protocolRepository.findAll();

            if (!protocols.isEmpty()) {
                Protocol maxViewsProtocol = Collections.max(protocols, Comparator.comparingInt(Protocol::getViews));
                System.out.println("Protocolo con mas vistas --> " + maxViewsProtocol);
                return maxViewsProtocol;
            } else {
                System.out.println("Lista de protocolos vacía!");
                return null;
            }
        } catch (Exception e) {
            throw new MyException("Error al procesar solicitud en los servicios!");
        }
    }
}
