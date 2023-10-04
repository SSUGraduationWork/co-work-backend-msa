package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GetSocialOAuthRes {
    private String jwtToken;
    private Long user_id;

    private String user_email;

    private String picture_url;
    private String role;
}
