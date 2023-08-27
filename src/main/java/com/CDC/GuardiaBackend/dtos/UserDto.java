package com.CDC.GuardiaBackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String id;
    private String name;
    private String lastname;
    private String email;
    private String dni;
    private String medicalRegistration;
    private String role;
    private String token;

}
