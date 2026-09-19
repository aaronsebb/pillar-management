/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java.com.pillargroup.pillarmanagement.exception;
/**
 * Excepción personalizada para errores de conexión a la base de datos.
 * 
 * @author informatica
 */
public class DatabaseConnectionException extends Exception {

    // Constructor que recibe solo el mensaje del error
    public DatabaseConnectionException(String message) {
        super(message);
    }
}