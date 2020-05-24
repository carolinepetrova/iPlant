package com.project.iplant.auth.web;

import com.project.iplant.auth.service.CustomUserDetails;
import com.project.iplant.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserControllerAdvice {
    @Autowired
    private UserService userService;

    @ModelAttribute("name")
    public String getUser(Model model) {
        String name;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            name = ((CustomUserDetails)principal).getName();

        } else {
            name = principal.toString();
        }
        return name;
    }
}
