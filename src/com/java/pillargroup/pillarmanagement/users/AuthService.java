/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.java.pillargroup.pillarmanagement.users;

import com.java.pillargroup.pillarmanagement.exception.ServiceException;
import java.sql.SQLException;
import com.java.pillargroup.pillarmanagement.addresses.model.Address;
import com.java.pillargroup.pillarmanagement.addresses.service.AddressService;

public class AuthService {

    private final AuthRepository authRepository;
    private final AddressService addressService = new AddressService();

    public AuthService() {
        this.authRepository = new AuthRepository();
    }

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public UserDto login(String email, String password) throws ServiceException {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new ServiceException("Debes ingresar correo y contraseña.");
        }

        UserDto user;
        try {
            user = authRepository.findUserByEmail(email);
        } catch (SQLException e) {
            throw new ServiceException("No se pudo conectar con la base de datos.");
        }

        if (user == null || !password.equals(user.getPasswordHash())) {
            throw new ServiceException("Correo o contraseña incorrectos.");
        }

        return user;
    }

    public void register(String firstName, String lastName, String email, String password) throws ServiceException {
        if (firstName == null || firstName.isBlank()
                || lastName == null || lastName.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()) {
            throw new ServiceException("Todos los campos son obligatorios.");
        }

        User nuevoUsuario = new User(null, firstName, lastName, email, password, null, 0);

        try {
            boolean creado = authRepository.save(nuevoUsuario);
            if (!creado) {
                throw new ServiceException("No se pudo crear el usuario.");
            }
        } catch (SQLException e) {
            throw new ServiceException("No se pudo conectar con la base de datos.");
        }
    }
    
        // Paso 1 del registro: solo valida, no guarda nada.
    public void validarDatosCuenta(String firstName, String lastName, String email,
            String password, String confirmPassword) throws ServiceException {
        if (firstName == null || firstName.isBlank()
                || lastName == null || lastName.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()) {
            throw new ServiceException("Todos los campos son obligatorios.");
        }
        if (!email.contains("@")) {
            throw new ServiceException("Ingresa un correo electrónico válido.");
        }
        if (!password.equals(confirmPassword)) {
            throw new ServiceException("Las contraseñas no coinciden.");
        }
    }

    // Paso 2 del registro: crea la cuenta. La dirección es opcional (puede ser null).
    public void register(String firstName, String lastName, String email, String password, Address address)
            throws ServiceException {

        validarDatosCuenta(firstName, lastName, email, password, password);

        Address savedAddress = null;
        boolean created = false;

        try {
            String addressId = null;
            if (address != null) {
                savedAddress = addressService.create(address); // valida y guarda la dirección
                addressId = savedAddress.getAddressId();
            }

            User nuevoUsuario = new User(null, firstName, lastName, email, password, addressId, 0);
            created = authRepository.save(nuevoUsuario, addressId);

        } catch (IllegalArgumentException e) {
            throw new ServiceException(e.getMessage());
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // correo duplicado en MySQL
                throw new ServiceException("Ya existe una cuenta con ese correo.");
            }
            throw new ServiceException("No se pudo conectar con la base de datos.");
        } finally {
            // Si el usuario no se creó, no dejamos la dirección huérfana.
            if (!created && savedAddress != null) {
                try {
                    addressService.delete(savedAddress.getAddressId());
                } catch (Exception ignored) {
                }
            }
        }

        if (!created) {
            throw new ServiceException("No se pudo crear el usuario.");
        }
    }
    
}