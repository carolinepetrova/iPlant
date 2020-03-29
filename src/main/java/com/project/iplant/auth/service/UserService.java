package com.project.iplant.auth.service;

import com.project.iplant.auth.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
