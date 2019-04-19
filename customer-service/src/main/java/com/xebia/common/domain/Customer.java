package com.xebia.common.domain;

import lombok.*;
import org.springframework.data.annotation.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "customer")
@Immutable
public class Customer {

    public Customer() {
    }

    public Customer(String name, String email, String mobile, boolean notificationEmail, boolean notificationMobile) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.notificationEmail = notificationEmail;
        this.notificationMobile = notificationMobile;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "notification_email")
    private boolean notificationEmail;

    @Column(name = "notification_mobile")
    private boolean notificationMobile;

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

    public boolean isNotificationMobile() {
        return notificationMobile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return notificationEmail == customer.notificationEmail &&
                notificationMobile == customer.notificationMobile &&
                Objects.equals(id, customer.id) &&
                Objects.equals(name, customer.name) &&
                Objects.equals(email, customer.email) &&
                Objects.equals(mobile, customer.mobile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, mobile, notificationEmail, notificationMobile);
    }
}