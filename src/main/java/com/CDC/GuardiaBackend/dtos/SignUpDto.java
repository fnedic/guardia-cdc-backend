package com.CDC.GuardiaBackend.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String lastname;

    @NotEmpty
    private String email;

    @NotEmpty
    private String dni;

    @NotEmpty
    private String medicalRegistration;

    @NotEmpty
    private char[] password;

}
