package com.serdarormanli.backbase.service;

import com.serdarormanli.backbase.dto.CreateUserRequest;
import com.serdarormanli.backbase.dto.JwtAuthenticationResponse;
import com.serdarormanli.backbase.dto.SignUpRequest;
import com.serdarormanli.backbase.dto.SigninRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signup(@NonNull @Valid SignUpRequest request) {
        var createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail(request.getEmail());
        createUserRequest.setPassword(request.getPassword());

        var user = this.userService.createUser(createUserRequest);

        var jwt = this.jwtService.generateToken(user);

        return new JwtAuthenticationResponse(jwt);
    }

    @Override
    public JwtAuthenticationResponse signin(@NonNull @Valid SigninRequest request) {
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var user = this.userService.loadUserByUsername(request.getEmail());

        var jwt = this.jwtService.generateToken(user);

        return new JwtAuthenticationResponse(jwt);
    }
}
