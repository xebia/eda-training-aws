package com.xebia.common.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class Address {

    public Address() {
    }

    public Address(String street, String number, String zipCode, String city, String country) {
        this.street = street;
        this.city = city;
        this.number = number;
        this.country = country;
        this.zipCode = zipCode;
    }

    @NotNull
    @Size(max = 100)
    private String street;

    @NotNull
    @Size(max = 100)
    private String number;

    @NotNull
    @Size(max = 6)
    private String zipCode;

    @NotNull
    @Size(max = 100)
    private String city;

    @NotNull
    @Size(max = 100)
    private String country;


    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
