package com.CDC.GuardiaBackend.DTO;

import lombok.Data;

/**
 *
 * @author micae
 */
@Data
public class LoginRequest {

    private String email;
    private String password;

}
