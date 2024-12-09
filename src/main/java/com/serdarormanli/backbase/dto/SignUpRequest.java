package com.serdarormanli.backbase.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {
    public static final String emailRegexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @NotNull
    @Email(regexp = emailRegexp)
    @Size(max = 100)
    private String email;
    @Size(min = 1)
    private String password;
}
