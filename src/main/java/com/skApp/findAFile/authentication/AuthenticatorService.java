package com.skApp.findAFile.authentication;

import com.skApp.findAFile.shared.mapper.response.LoginResponse;
import com.skApp.findAFile.shared.persistance.entity.UserDetail;
import com.skApp.findAFile.shared.persistance.repo.UserDetailRepo;
import com.skApp.findAFile.shared.properties.CommonProperties;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticatorService {

    CommonProperties commonProperties;
    LoginResponse loginResponse;
    UserDetailRepo userDetailRepo;

    @Autowired
    public AuthenticatorService(CommonProperties commonProperties, LoginResponse loginResponse, UserDetailRepo userDetailRepo) {
        this.commonProperties = commonProperties;
        this.loginResponse = loginResponse;
        this.userDetailRepo = userDetailRepo;
    }


    public LoginResponse checkAndSaveLogin(String userName, String password){

        log.info("Inside checkAndSaveLogin");
        LoginResponse loginResponse = checkLogin(userName, password);
        if(loginResponse != null && loginResponse.getLoginStatus()){
            saveLogin(userName);
        }

        return loginResponse;
    }

    private void saveLogin(String userName) {
        UserDetail userDetail = userDetailRepo.findByUserNameAndDeleted(userName,0);

        if(null == userDetail) {
            userDetail = new UserDetail();
            userDetail.setUserName(userName);
            userDetail.setPrivilege(UserPrivilegeInfo.DEFAULT.name());
            userDetailRepo.save(userDetail);
        }

    }

    private LoginResponse checkLogin(String userName, String password) {

        log.info("Inside checkLogin method with username " + userName);
        JSch jsch = new JSch();
        Session session;
        try {
            session = jsch.getSession(userName, commonProperties.getAuthenticationIp(), commonProperties.getAuthenticationPort());
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            log.info("Establishing Connection...");
            session.connect();
            session.disconnect();
            log.info("Login Successful for user :- " + userName);
            loginResponse.setLoginMessage("Login Successful");
            loginResponse.setLoginStatus(true);
            return loginResponse;
        } catch (Exception e) {
            log.error("Connection failed with error ", e);
            loginResponse.setLoginMessage("UserName / Password Incorrect");
            loginResponse.setLoginStatus(false);
            return loginResponse;
        }
    }

}
