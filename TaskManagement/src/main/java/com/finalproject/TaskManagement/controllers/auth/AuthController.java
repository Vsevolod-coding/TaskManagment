package com.finalproject.TaskManagement.controllers.auth;

import com.finalproject.TaskManagement.dtos.AuthRequest;
import com.finalproject.TaskManagement.dtos.AuthResponse;
import com.finalproject.TaskManagement.dtos.SignUpRequest;
import com.finalproject.TaskManagement.dtos.UserDTO;
import com.finalproject.TaskManagement.entities.User;
import com.finalproject.TaskManagement.repository.UserRepository;
import com.finalproject.TaskManagement.services.auth.AuthService;
import com.finalproject.TaskManagement.services.jwt.UserService;
import com.finalproject.TaskManagement.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        if (authService.hasUserThisEmail(signUpRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Аккаунт с таким email уже существует!");
        }
        UserDTO userDTO = authService.signUp(signUpRequest);
        if (userDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(),
                    authRequest.getPassword()
            ));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("E-mail или пароль указаны неверно.");
        }
        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authRequest.getEmail());
        Optional<User> optionalUser = userRepository.findFirstByEmail(authRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        AuthResponse response = new AuthResponse();
        if (optionalUser.isPresent()) {
            response.setJwt(jwt);
            response.setUserRole(optionalUser.get().getUserRole());
            response.setUserId(optionalUser.get().getId());
        }
        return response;
    }

}
