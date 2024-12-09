package com.serdarormanli.backbase.dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record UserDTO(
        Long id,
        String username,
        String password,
        Collection<? extends GrantedAuthority> authorities
) implements UserDetailsWithID {

    @Override
    public Long getID() {
        return this.id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
