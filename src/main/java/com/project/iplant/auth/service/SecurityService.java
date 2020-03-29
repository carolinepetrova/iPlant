package com.project.iplant.auth.service;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public interface SecurityService  {
    public String findLoggedInUsername();

    public void autoLogin(String username, String password);

}