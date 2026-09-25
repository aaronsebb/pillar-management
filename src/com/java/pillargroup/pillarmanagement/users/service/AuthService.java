package com.java.pillargroup.pillarmanagement.users.service;

import com.java.pillargroup.pillarmanagement.users.respository.AuthRepository;
import com.java.pillargroup.pillarmanagement.users.model.User;
import com.java.pillargroup.pillarmanagement.addresses.model.Address;
import com.java.pillargroup.pillarmanagement.addresses.service.AddressService;
import com.java.pillargroup.pillarmanagement.exception.ServiceException;
import com.java.pillargroup.pillarmanagement.users.dto.UserDto;
import java.sql.SQLException;
import main.java.dev.alpha.alphalogin.security.jbcrypt.BCrypt;


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
            user = authRepository.findUserByEmail(email.trim());
        } catch (SQLException e) {
            throw new ServiceException("No se pudo conectar con la base de datos.");
        }

        if (user == null || !passwordMatches(password, user.getPasswordHash())) {
            throw new ServiceException("Correo o contraseña incorrectos.");
        }

        user.setPasswordHash(null);
        return user;
    }

    private boolean passwordMatches(String plainPassword, String hash) {
        try {
            return BCrypt.checkpw(plainPassword, hash);
        } catch (Exception e) {
            return false;
        }
    }

    public void validarDatosCuenta(String firstName, String lastName, String email,
            String password, String confirmPassword) throws ServiceException {
        if (firstName == null || firstName.isBlank()
                || lastName == null || lastName.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()) {
            throw new ServiceException("Todos los campos son obligatorios.");
        }
        if (!email.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
            throw new ServiceException("Ingresa un correo electrónico válido.");
        }
        if (!password.equals(confirmPassword)) {
            throw new ServiceException("Las contraseñas no coinciden.");
        }
    }
    
    public void register(String firstName, String lastName, String email, String password, Address address)
            throws ServiceException {

        validarDatosCuenta(firstName, lastName, email, password, password);

        Address savedAddress = null;
        boolean created = false;

        try {
            String addressId = null;
            if (address != null) {
                savedAddress = addressService.create(address);
                addressId = savedAddress.getAddressId();
            }

            String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
            User nuevoUsuario = new User(null, firstName.trim(), lastName.trim(), email.trim(), passwordHash, addressId, 0);
            created = authRepository.save(nuevoUsuario, addressId);

        } catch (IllegalArgumentException e) {
            throw new ServiceException(e.getMessage());
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new ServiceException("Ya existe una cuenta con ese correo.");
            }
            throw new ServiceException("No se pudo conectar con la base de datos.");
        } finally {
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
    
    public String findFullNameByUserId(String userId) {
        try {
            return authRepository.findFullNameByUserId(userId);
        } catch (SQLException e) {
            return null;
        }
    }

    public Address getUserAddress(String userId) {
        try {
            String addressId = authRepository.findAddressIdByUserId(userId);
            if (addressId == null || addressId.isBlank()) {
                return null;
            }
            return addressService.findById(addressId).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public void updateUserAddress(String userId, Address address) throws ServiceException {
        if (userId == null || userId.isBlank()) {
            throw new ServiceException("Debes iniciar sesión para actualizar tu dirección.");
        }

        try {
            String addressId = authRepository.findAddressIdByUserId(userId);

            if (addressId == null || addressId.isBlank()) {
                Address saved = addressService.create(address);
                if (!authRepository.updateUserAddress(userId, saved.getAddressId())) {
                    throw new ServiceException("No se pudo asociar la dirección a tu cuenta.");
                }
            } else {
                address.setAddressId(addressId);
                addressService.update(address);
            }
        } catch (IllegalArgumentException e) {
            throw new ServiceException(e.getMessage());
        } catch (SQLException e) {
            throw new ServiceException("No se pudo conectar con la base de datos.");
        }
    }
}