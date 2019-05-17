package com.xebia.common.domain;

import org.springframework.data.annotation.Immutable;

import javax.persistence.*;

@Entity
@Table(name = "customer_view")
@Immutable
public class Customer {

    public Customer() {
    }

    public Customer(String name, String email, String mobile, boolean notificationEmail, boolean notificationText) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.notificationEmail = notificationEmail;
        this.notificationText = notificationText;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String email;

    private String mobile;

    private boolean notificationEmail;

    private boolean notificationText;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public boolean isNotificationEmail() {
        return notificationEmail;
    }

    public boolean isNotificationText() {
        return notificationText;
    }
}