package com.project.iplant.auth.model;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private int id;

        @Column(name = "name")
        private String name;

         @Column(name = "username")
        private String username;

        @Column(name = "password")
        private String password;

        @Column(name = "passwordConfirm")
        @Transient
        private String passwordConfirm;

        @ManyToMany
    private Set<Role> roles;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPasswordConfirm() {
            return passwordConfirm;
        }

        public void setPasswordConfirm(String passwordConfirm) {
            this.passwordConfirm = passwordConfirm;
        }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
            return name;
        }
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    }
