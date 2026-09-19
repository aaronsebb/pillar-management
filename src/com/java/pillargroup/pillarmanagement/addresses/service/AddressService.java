/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.java.pillargroup.pillarmanagement.addresses.service;

import com.java.pillargroup.pillarmanagement.addresses.model.Address;
import com.java.pillargroup.pillarmanagement.addresses.repository.AddressRepository;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService() {
        this.addressRepository = new AddressRepository();
    }

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address create(Address address) throws SQLException {
        validate(address);

        // Si no trae ID, el repository lo genera
        if (address.getAddressId() != null && !address.getAddressId().trim().isEmpty()) {
            if (addressRepository.existsById(address.getAddressId())) {
                throw new IllegalArgumentException("Ya existe una dirección con el ID: " + address.getAddressId());
            }
        }

        return addressRepository.save(address);
    }

    public Optional<Address> findById(String addressId) throws SQLException {
        if (addressId == null || addressId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de la dirección no puede estar vacío");
        }
        return addressRepository.findById(addressId);
    }

    public List<Address> findAll() throws SQLException {
        return addressRepository.findAll();
    }

    public Address update(Address address) throws SQLException {
        validate(address);

        if (!addressRepository.existsById(address.getAddressId())) {
            throw new IllegalArgumentException("No existe una dirección con el ID: " + address.getAddressId());
        }

        return addressRepository.update(address);
    }

    public boolean delete(String addressId) throws SQLException {
        if (addressId == null || addressId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de la dirección no puede estar vacío");
        }

        if (!addressRepository.existsById(addressId)) {
            throw new IllegalArgumentException("No existe una dirección con el ID: " + addressId);
        }

        return addressRepository.deleteById(addressId);
    }

    // ========== VALIDACIONES ==========
    private void validate(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("La dirección no puede ser nula");
        }
        if (address.getCountry() == null || address.getCountry().trim().isEmpty()) {
            throw new IllegalArgumentException("El país es obligatorio");
        }
        if (address.getCity() == null || address.getCity().trim().isEmpty()) {
            throw new IllegalArgumentException("La ciudad es obligatoria");
        }
        if (address.getDistrict() == null || address.getDistrict().trim().isEmpty()) {
            throw new IllegalArgumentException("El distrito es obligatorio");
        }
        if (address.getAvenue() == null || address.getAvenue().trim().isEmpty()) {
            throw new IllegalArgumentException("La avenida es obligatoria");
        }
        if (address.getStreet() == null || address.getStreet().trim().isEmpty()) {
            throw new IllegalArgumentException("La calle es obligatoria");
        }
        if (address.getHouse() == null || address.getHouse().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de casa es obligatorio");
        }
    }
}