package com.skApp.findAFile.shared.mapper;

import com.skApp.findAFile.shared.mapper.response.IndexResponse;
import com.skApp.findAFile.shared.mapper.response.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MapperConfiguration {

    @Bean
    public LoginResponse loginResponse() {
        log.info("Creating new loginResponse");
        return new LoginResponse();
    }

    @Bean
    public IndexResponse indexResponse() {
        return new IndexResponse();
    }
}
