package com.serdarormanli.backbase.controller;

import com.serdarormanli.backbase.dto.JwtAuthenticationResponse;
import com.serdarormanli.backbase.dto.SignUpRequest;
import com.serdarormanli.backbase.dto.SigninRequest;
import com.serdarormanli.backbase.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/auth", produces = APPLICATION_JSON_VALUE,consumes = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    @ResponseStatus(value = OK)
    public JwtAuthenticationResponse signup(@Valid @RequestBody SignUpRequest request) {
        return authenticationService.signup(request);
    }

    @PostMapping("/signin")
    @ResponseStatus(value = OK)
    public JwtAuthenticationResponse signin(@Valid @RequestBody SigninRequest request) {
        return authenticationService.signin(request);
    }
}
