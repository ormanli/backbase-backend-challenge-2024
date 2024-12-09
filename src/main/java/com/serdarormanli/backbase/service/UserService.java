package com.serdarormanli.backbase.service;

import com.serdarormanli.backbase.dto.CreateUserRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    UserDetails createUser(CreateUserRequest createUserRequest);
}
