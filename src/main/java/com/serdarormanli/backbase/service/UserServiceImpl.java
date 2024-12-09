package com.serdarormanli.backbase.service;

import com.serdarormanli.backbase.dto.CreateUserRequest;
import com.serdarormanli.backbase.dto.UserDTO;
import com.serdarormanli.backbase.model.User;
import com.serdarormanli.backbase.model.UserRole;
import com.serdarormanli.backbase.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.EnumSet;

import static com.serdarormanli.backbase.dto.CreateUserRequest.emailRegexp;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final Clock clock;

    @Override
    public UserDetails createUser(@NonNull @Valid CreateUserRequest createUserRequest) {
        var user = new User();
        user.setRole(UserRole.USER);
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        user.setEmail(createUserRequest.getEmail());
        user.setCreatedAt(clock.instant());

        user = this.userRepository.save(user);

        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                EnumSet.of(UserRole.USER, user.getRole())
        );
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull @Email(regexp = emailRegexp) String email) throws UsernameNotFoundException {
        var user = this.userRepository.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("User not found with email: %s".formatted(email)));

        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                EnumSet.of(UserRole.USER, user.getRole())
        );
    }

}
