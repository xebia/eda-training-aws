package com.xebia.common.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

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

    @NotEmpty
    @Size(max = 100)
    private String street;

    @NotEmpty
    @Size(max = 100)
    private String number;

    @NotEmpty
    @Size(max = 6)
    private String zipCode;

    @NotEmpty
    @Size(max = 100)
    private String city;

    @NotEmpty
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) &&
                Objects.equals(number, address.number) &&
                Objects.equals(zipCode, address.zipCode) &&
                Objects.equals(city, address.city) &&
                Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, number, zipCode, city, country);
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", number='" + number + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
