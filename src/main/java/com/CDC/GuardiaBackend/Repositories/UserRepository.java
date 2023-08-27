package com.CDC.GuardiaBackend.Repositories;

import com.CDC.GuardiaBackend.Entities.User;
import com.CDC.GuardiaBackend.Enums.Roles;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

    Optional <User> findByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User searchByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRole(@Param("role") Roles role);

    @Query("SELECT u FROM User u WHERE u.name = :name")
    List<User> findByName(@Param("name") String name);

    @Query("SELECT u FROM User u WHERE u.lastname = :lastname")
    List<User> findByNameAndLastName(@Param("lastname") String lastname);
    
}
