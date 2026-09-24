/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.java.pillargroup.pillarmanagement.users;

import com.java.pillargroup.pillarmanagement.exception.ServiceException;
import com.java.pillargroup.pillarmanagement.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txtFieldEmail;

    @FXML
    private PasswordField fieldPassword;

    private final AuthService authService = new AuthService();

    // Metodo para que implementen el onAction.
    @FXML
    private void handleLogin() {
        String email = txtFieldEmail.getText();
        String password = fieldPassword.getText();

        try {
            UserDto usuario = authService.login(email, password);
            SceneManager.getInstance().showDashboardView(usuario);
        } catch (ServiceException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void handleIrARegistro() {
        SceneManager.getInstance().showRegistroView();
    }
    
        // Vuelve al dashboard sin iniciar sesión (modo invitado).
    @FXML
    private void handleVolverAlDashboard() {
        SceneManager.getInstance().showDashboardView();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de inicio de sesión");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}