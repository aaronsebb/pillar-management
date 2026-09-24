/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.java.pillargroup.pillarmanagement.exception;
/**
 * Excepción personalizada para errores de conexión a la base de datos.
 * 
 * @author informatica
 */
public class DatabaseException extends Exception {

    // Constructor que recibe solo el mensaje del error
    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException() {
    }
    
}