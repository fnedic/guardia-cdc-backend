/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.CDC.GuardiaBackend.Repositories;

import com.CDC.GuardiaBackend.Entities.Protocol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author micae
 */
@Repository
public interface ProtocolRepository extends JpaRepository<Protocol, String>{
    
}
