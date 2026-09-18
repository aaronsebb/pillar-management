/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.java.pillargroup.pillarmanagement.users;

import java.com.pillargroup.pillarmanagement.exception.ServiceException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class AuthService {

    private final AuthRepository authRepository;

    public AuthService() {
        this.authRepository = new AuthRepository();
    }

    // Útil para pruebas unitarias con un repositorio simulado
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

        if (user == null || !hashPassword(password).equals(user.getPasswordHash())) {
            // mismo mensaje en ambos casos: no revelar si el correo existe o no
            throw new ServiceException("Correo o contraseña incorrectos.");
        }

        return user;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible en esta JVM.", e);
        }
    }
}