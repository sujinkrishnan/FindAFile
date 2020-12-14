package com.skApp.findAFile.requestintake;

import com.skApp.findAFile.authentication.AuthenticatorService;
import com.skApp.findAFile.shared.mapper.request.LoginRequest;
import com.skApp.findAFile.shared.mapper.response.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoginController {

    private final AuthenticatorService authenticatorService;

    @Autowired
    public LoginController(AuthenticatorService authenticatorService) {
        this.authenticatorService = authenticatorService;
    }

    @PostMapping(value = "/userLoginRequest")
    public LoginResponse authenticationService(@RequestBody LoginRequest loginRequest) {

        log.info("Inside authenticationService");
        return authenticatorService.checkAndSaveLogin(loginRequest.getUserId(),loginRequest.getPassword());
    }

}
