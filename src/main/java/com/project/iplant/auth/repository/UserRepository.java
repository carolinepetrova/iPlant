package com.project.iplant.auth.repository;

import com.project.iplant.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    //User findById(int id);
    User findByUsername(String username);
}