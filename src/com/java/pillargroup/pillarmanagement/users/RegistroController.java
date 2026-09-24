/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.java.pillargroup.pillarmanagement.users;

/**
 *
 * @author informatica
 */
public class RegistroController {
    
}
package com.java.pillargroup.pillarmanagement.users;

import com.java.pillargroup.pillarmanagement.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.java.pillargroup.pillarmanagement.exception.ServiceException;
import javafx.scene.control.Alert;

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
    private PasswordField confirmpasswordField;

    @FXML
    private Label errorLabel;

    private final AuthRepository authRepository = new AuthRepository();
    private final AuthService authService = new AuthService();
    private RegistroDraft draft = new RegistroDraft();
    
    

    
    //Esto lo voy a dejar para otra vista, una es para los datos del usuario y la otra es para la dirección
    //Aunque esa va a ser opcional porque si no quieren meter la dirección, no los podemos obligar, entonces yo voy a dejar la lógica en eso.
    /*@FXML
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

        try {
            authRepository.save(nuevoUsuario);
            errorLabel.setText("");
            SceneManager.getInstance().showLoginView();
        } catch (SQLException e) {
            errorLabel.setText("No se pudo crear el usuario.");
        }
        
    }*/
    
        // Lo llama SceneManager cuando el usuario regresa del paso 2.
    public void setDraft(RegistroDraft draft) {
        this.draft = draft;
        firstNameField.setText(draft.getFirstName());
        lastNameField.setText(draft.getLastName());
        emailField.setText(draft.getEmail());
        passwordField.setText(draft.getPassword());
        confirmpasswordField.setText(draft.getPassword());
    }

    // Botón "Continuar": valida y pasa al paso 2. Todavía no guarda nada.
    @FXML
    private void handleContinuar() {
        try {
            authService.validarDatosCuenta(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText(),
                    passwordField.getText(),
                    confirmpasswordField.getText());

            draft.setFirstName(firstNameField.getText().trim());
            draft.setLastName(lastNameField.getText().trim());
            draft.setEmail(emailField.getText().trim());
            draft.setPassword(passwordField.getText());

            SceneManager.getInstance().showRegistroDireccionView(draft);
        } catch (ServiceException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registro");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleIrALogin() {
        SceneManager.getInstance().showLoginView();
    }
}
