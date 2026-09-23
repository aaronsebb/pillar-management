/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.java.pillargroup.pillarmanagement.users;

import com.java.pillargroup.pillarmanagement.exception.ServiceException;
import java.sql.SQLException;

public class AuthService {

    private final AuthRepository authRepository;

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
}