package com.java.pillargroup.pillarmanagement.users;

import com.java.pillargroup.pillarmanagement.exception.ServiceException;
import com.java.pillargroup.pillarmanagement.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Paso 1 del registro: datos de la cuenta. No guarda nada: valida, llena el
 * RegistroDraft y pasa al paso 2 (dirección).
 */
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

    private final AuthService authService = new AuthService();
    private RegistroDraft draft = new RegistroDraft();

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