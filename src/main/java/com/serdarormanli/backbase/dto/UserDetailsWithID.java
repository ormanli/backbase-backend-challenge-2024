package com.serdarormanli.backbase.dto;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsWithID extends UserDetails {
    Long getID();
}
