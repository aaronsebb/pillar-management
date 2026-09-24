/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.java.pillargroup.pillarmanagement.residence.service;

import com.java.pillargroup.pillarmanagement.residence.model.Residence;
import com.java.pillargroup.pillarmanagement.residence.repository.ResidenceRepository;
import com.java.pillargroup.pillarmanagement.addresses.model.Address;
import com.java.pillargroup.pillarmanagement.addresses.service.AddressService;
import com.java.pillargroup.pillarmanagement.exception.RepositoryException;
import com.java.pillargroup.pillarmanagement.exception.ServiceException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ResidenceService {

    private final ResidenceRepository residenceRepository;

    public ResidenceService() {
        this.residenceRepository = new ResidenceRepository();
    }

    public ResidenceService(ResidenceRepository residenceRepository) {
        this.residenceRepository = residenceRepository;
    }

    public boolean create(Residence residence) throws SQLException {
        validate(residence);

        if (residence.getResidenceId() != null && !residence.getResidenceId().trim().isEmpty()) {
            if (residenceRepository.existsById(residence.getResidenceId())) {
                throw new IllegalArgumentException("Ya existe una residencia con el ID: " + residence.getResidenceId());
            }
        }

        return residenceRepository.save(residence);
    }

    public Optional<Residence> findById(String residenceId) throws SQLException {
        if (residenceId == null || residenceId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de la residencia no puede estar vacío");
        }
        return residenceRepository.findById(residenceId);
    }

    public List<Residence> findAll() throws SQLException {
        return residenceRepository.findAll();
    }
    
    public void createWithAddress(Residence residence, Address address) throws ServiceException {
        AddressService addressService = new AddressService();
        Address saved = null;
        boolean created = false;

        try {
            saved = addressService.create(address);
            residence.setAddressId(saved.getAddressId());
            created = create(residence);
        } catch (IllegalArgumentException e) {
            throw new ServiceException(e.getMessage());
        } catch (SQLException | RepositoryException e) {
            throw new ServiceException("No se pudo guardar la residencia. Intenta de nuevo.");
        } finally {
            if (!created && saved != null) {
                try {
                    addressService.delete(saved.getAddressId());
                } catch (Exception ignored) {
                }
            }
        }

        if (!created) {
            throw new ServiceException("No se pudo guardar la residencia.");
        }
    }

    public Residence update(Residence residence) throws SQLException {
        validate(residence);

        if (!residenceRepository.existsById(residence.getResidenceId())) {
            throw new IllegalArgumentException("No existe una residencia con el ID: " + residence.getResidenceId());
        }

        return residenceRepository.update(residence);
    }

    public boolean delete(String residenceId) throws SQLException {
        if (residenceId == null || residenceId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de la residencia no puede estar vacío");
        }

        if (!residenceRepository.existsById(residenceId)) {
            throw new IllegalArgumentException("No existe una residencia con el ID: " + residenceId);
        }

        return residenceRepository.deleteById(residenceId);
    }

    private void validate(Residence residence) {
        if (residence == null) {
            throw new IllegalArgumentException("La residencia no puede ser nula");
        }
        if (residence.getStatusId() <= 0) {
            throw new IllegalArgumentException("El status_id es obligatorio");
        }
        if (residence.getLumpSum() <= 0) {
            throw new IllegalArgumentException("El monto total (lump_sum) debe ser mayor a 0");
        }
        if (residence.getMonthlyPayment() <= 0) {
            throw new IllegalArgumentException("El pago mensual debe ser mayor a 0");
        }
        if (residence.getAddressId() == null || residence.getAddressId().trim().isEmpty()) {
            throw new IllegalArgumentException("El address_id es obligatorio");
        }
        if (residence.getUserId() == null || residence.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("El user_id es obligatorio");
        }
    }
}