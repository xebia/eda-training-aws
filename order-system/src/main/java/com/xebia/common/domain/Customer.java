package com.xebia.common.domain;

import org.springframework.data.annotation.Immutable;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "customer_view")
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

    private String name;

    private String email;

    private String mobile;

    private boolean notificationEmail;

    private boolean notificationText;
    @Embedded
    private Address address;

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
                Objects.equals(name, customer.name) &&
                Objects.equals(email, customer.email) &&
                Objects.equals(mobile, customer.mobile) &&
                Objects.equals(address, customer.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, mobile, notificationEmail, notificationText, address);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", notificationEmail=" + notificationEmail +
                ", notificationText=" + notificationText +
                ", address=" + address +
                '}';
    }
}