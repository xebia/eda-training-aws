package com.xebia.common.domain;

import org.springframework.data.annotation.Immutable;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "customer")
@Immutable
public class Customer {

    public Customer() {
    }

    public Customer(String name, String email, String mobile, boolean notificationEmail, boolean notificationText, Address address) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.notificationEmail = notificationEmail;
        this.notificationText = notificationText;
        this.address = address;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Embedded
    @Valid
    private Address address;

    @Column(name = "name")
    @NotEmpty
    private String name;

    @Column(name = "email")
    @NotEmpty
    private String email;

    @Column(name = "mobile")
    @NotEmpty
    private String mobile;

    @Column(name = "notification_email")
    @NotNull
    private boolean notificationEmail;

    @Column(name = "notification_text")
    @NotNull
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

    public Address getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return notificationEmail == customer.notificationEmail &&
                notificationText == customer.notificationText &&
                Objects.equals(id, customer.id) &&
                Objects.equals(address, customer.address) &&
                Objects.equals(name, customer.name) &&
                Objects.equals(email, customer.email) &&
                Objects.equals(mobile, customer.mobile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, name, email, mobile, notificationEmail, notificationText);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", address=" + address +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", notificationEmail=" + notificationEmail +
                ", notificationText=" + notificationText +
                '}';
    }
}