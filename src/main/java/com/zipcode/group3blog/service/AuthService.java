package com.zipcode.group3blog.service;

import com.zipcode.group3blog.dto.LoginRequest;
import com.zipcode.group3blog.dto.RegisterRequest;
import com.zipcode.group3blog.model.Users;
import com.zipcode.group3blog.repository.UserRepository;
import com.zipcode.group3blog.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;

    public void signup(RegisterRequest registerRequest){
        Users newUsers = new Users();
        newUsers.setUsername(registerRequest.getUsername());
        newUsers.setPassword( encodePassword(registerRequest.getPassword()));
        newUsers.setEmail(registerRequest.getEmail());
        userRepository.save(newUsers);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public String login(LoginRequest loginRequest){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return jwtProvider.generateToken(authenticate);
    }
}