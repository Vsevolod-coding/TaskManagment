package com.finalproject.TaskManagement.dtos;

import com.finalproject.TaskManagement.enums.UserRole;
import lombok.Data;

@Data
public class AuthResponse {

    private String jwt;

    private Long userId;

    private UserRole userRole;

}
