package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DataOwner
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String username;

    String email;

    String password;

    int point;

    public int getConfirmation_status() {
        return confirmation_status;
    }

    public void setConfirmation_status(int confirmation_status) {
        this.confirmation_status = confirmation_status;
    }

    int confirmation_status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
