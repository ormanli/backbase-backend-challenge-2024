package com.serdarormanli.backbase.service;


import com.serdarormanli.backbase.dto.JwtAuthenticationResponse;
import com.serdarormanli.backbase.dto.SignUpRequest;
import com.serdarormanli.backbase.dto.SigninRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}
