package com.homeservices.back.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AppUser {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String pwd;
    private AppUserRole role;
    private boolean locked;
    private boolean enabled;
}
