package com.project.iplant.auth.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
        @Id
        private int id;

        private String username;

        private String password;

        @Transient
        private String passwordConfirm;

        @ManyToMany
        private boolean isAdmin;

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

       public boolean getIsAdmin() {
            return isAdmin;
       }

        public void setIsAdmin(boolean isAdmin) {
            this.isAdmin = isAdmin;
        }
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    }
