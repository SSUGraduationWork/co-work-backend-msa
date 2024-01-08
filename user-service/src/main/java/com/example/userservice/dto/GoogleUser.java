package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GoogleUser {
    public String id;
    public String email;
    public Boolean verified_email;
    public String name;
    public String given_name;
    public String family_name;
    public String picture;
    public String locale;
}
