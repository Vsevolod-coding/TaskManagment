package com.finalproject.TaskManagement.services.auth;

import com.finalproject.TaskManagement.dtos.SignUpRequest;
import com.finalproject.TaskManagement.dtos.UserDTO;
import com.finalproject.TaskManagement.entities.User;
import com.finalproject.TaskManagement.enums.UserRole;
import com.finalproject.TaskManagement.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @PostConstruct
    private void createAdminAcc(){
        Optional<User> optionalUser = userRepository.findByUserRole(UserRole.ADMIN);
        if (optionalUser.isEmpty()) {
            User newAdmin = new User();
            newAdmin.setName("admin");
            newAdmin.setEmail("admin_admin@example.com");
            newAdmin.setPassword(new BCryptPasswordEncoder().encode("admin_admin-01"));
            newAdmin.setUserRole(UserRole.ADMIN);
            userRepository.save(newAdmin);
        } else {
            System.out.println("Аккаунт администратора уже существует!");
        }
    }

    @Override
    public UserDTO signUp(SignUpRequest signUpRequest) {
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setName(signUpRequest.getName());
        user.setUserRole(UserRole.EMPLOYEE);
        user.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));
        return userRepository.save(user).getUserDTO();
    }

    @Override
    public Boolean hasUserThisEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
