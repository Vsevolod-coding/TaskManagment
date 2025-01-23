package com.finalproject.TaskManagement.services.auth;

import com.finalproject.TaskManagement.dtos.SignUpRequest;
import com.finalproject.TaskManagement.dtos.UserDTO;

public interface AuthService {

    UserDTO signUp(SignUpRequest signUpRequest);

    Boolean hasUserThisEmail(String email);

}
