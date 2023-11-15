package com.CDC.GuardiaBackend.Entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.CDC.GuardiaBackend.Enums.Specialties;
import org.hibernate.annotations.GenericGenerator;

import com.CDC.GuardiaBackend.Enums.Roles;
import com.CDC.GuardiaBackend.Enums.UserStatus;

import lombok.Data;

import java.util.Date;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    protected String id;

    private String name;
    private String lastname;
    private String email;
    private String password;
    private String dni;
    private String medicalRegistration;
    private Date startDate;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @Enumerated(EnumType.STRING)
    private Specialties specialtie;

    @Enumerated(EnumType.STRING)
    private UserStatus status;
}
