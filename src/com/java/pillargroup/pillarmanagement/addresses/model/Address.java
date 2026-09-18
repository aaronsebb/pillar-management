/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.java.pillargroup.pillarmanagement.addresses.model;

/**
 *
 * @author informatica
 */
public class Address {
    String addressId;
    String country;
    String city;
    String district;
    String avenue;
    String street;
    String house;

    public Address(String addressId, String country, String city, String district, String avenue, String street, String house) {
        this.addressId = addressId;
        this.country = country;
        this.city = city;
        this.district = district;
        this.avenue = avenue;
        this.street = street;
        this.house = house;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAvenue() {
        return avenue;
    }

    public void setAvenue(String avenue) {
        this.avenue = avenue;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }
    
    
}
