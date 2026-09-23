/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.java.pillargroup.pillarmanagement.users;

import com.java.pillargroup.pillarmanagement.util.SceneManager;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistroController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final AuthRepository authRepository = new AuthRepository();

    @FXML
    private void handleRegistro() {
        // TODO: cuando exista AuthService.register(...), mover esta llamada
        // ahi y quitar la dependencia directa a AuthRepository desde el controller.
        User nuevoUsuario = new User(
                null,
                firstNameField.getText(),
                lastNameField.getText(),
                emailField.getText(),
                passwordField.getText(),
                null,
                0
        );

        /*try {
            authRepository.save(nuevoUsuario);
            errorLabel.setText("");
            SceneManager.getInstance().showLoginView();
        } catch (SQLException e) {
            errorLabel.setText("No se pudo crear el usuario.");
        }
        NOTA: Descomentar cuando le hagan merge al guardado de usuarios de aaron
        */ 
    }

    @FXML
    private void handleIrALogin() {
        SceneManager.getInstance().showLoginView();
    }
}