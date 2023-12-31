package com.CDC.GuardiaBackend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.CDC.GuardiaBackend.Entities.Protocol;

@Repository
public interface ProtocolRepository extends JpaRepository<Protocol, String>{

    @Query("SELECT p FROM Protocol p WHERE p.title = :title")
    Protocol searchByTitle(@Param("title") String title);

}
