package com.CDC.GuardiaBackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.CDC.GuardiaBackend.Repositories.ProtocolRepository;

@Service
public class ProtocolService {
    
    @Autowired
    private ProtocolRepository protocolRepository; 
}
