package com.skApp.findAFile.shared.mapper.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String userId;
    private String password;
}
