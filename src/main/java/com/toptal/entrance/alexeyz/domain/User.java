package com.toptal.entrance.alexeyz.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author alexey.zakharchenko@gmail.com
 */
@Entity
public class User {
    public enum Role {
        admin, user
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
}
