package com.toptal.entrance.alexeyz.domain;

import com.toptal.entrance.alexeyz.Application;
import com.toptal.entrance.alexeyz.util.UserUtil;

import javax.persistence.*;
import java.util.Date;

/**
 * @author alexey.zakharchenko@gmail.com
 */
@Entity
public class User {
    public static final int PASSWORD_HASH_LENGTH = 32;
    public static final int MAX_PASSWORD_LENGTH = PASSWORD_HASH_LENGTH - 1;
    public static final int MIN_PASSWORD_LENGTH = 3;
    public static final int MIN_LOGIN_LENGTH = MIN_PASSWORD_LENGTH;

    public enum Role {
        admin, manager, user
    }

    @Id
    @GeneratedValue
    private Long id;

    private String login;
    private String password;
    private Role role = Role.user;
    private Date createdAt = new Date();

    public User() {}

    public User(String login, String password) {
        this(login, password, Role.user, new Date());
    }

    public User(String login, String password, Role role, Date createdAt) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Transient
    public boolean isAdmin() {
        return this.role == Role.admin;
    }

    @Transient
    public boolean isManager() {
        return this.role == Role.admin || this.role == Role.manager;
    }

    @PrePersist
    @PreUpdate
    void encryptPassword() {
        if (Application.PWD_HASH)
            if (password.length() < MAX_PASSWORD_LENGTH)
                // It means password is not hashed
                setPassword(UserUtil.hash(password));
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", createdAt=" + createdAt +
                '}';
    }
}
