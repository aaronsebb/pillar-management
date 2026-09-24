package com.java.pillargroup.pillarmanagement.residence;

import com.java.pillargroup.pillarmanagement.users.UserDto;
import com.java.pillargroup.pillarmanagement.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label bienvenidaLabel;

    @FXML
    private Button registerButton;

    @FXML
    private Button loginButton;

    @FXML
    private Button logoutButton;

    private UserDto usuarioActual;

    public void setUsuarioActual(UserDto usuarioActual) {
        this.usuarioActual = usuarioActual;

        if (bienvenidaLabel == null) {
            return;
        }

        boolean logueado = usuarioActual != null;

        bienvenidaLabel.setText(logueado ? "Bienvenido, " + usuarioActual.getNombre() : "");
        mostrar(bienvenidaLabel, logueado);
        mostrar(logoutButton, logueado);
        mostrar(loginButton, !logueado);
        mostrar(registerButton, !logueado);
    }

    private void mostrar(Node nodo, boolean visible) {
        nodo.setVisible(visible);
        nodo.setManaged(visible);
    }

    // Para el boton "Iniciar sesion" del dashboard en modo invitado
    @FXML
    private void handleIrALogin() {
        SceneManager.getInstance().showLoginView();
    }

    @FXML
    private void handleIrARegistro() {
        SceneManager.getInstance().showRegistroView();
    }

    // Cerrar sesión = volver al dashboard como invitado.
    @FXML
    private void handleCerrarSesion() {
        SceneManager.getInstance().showDashboardView();
    }

    // Ejemplo de acción que requiere cuenta: al invitado se le ofrece iniciar sesión.
    @FXML
    private void handleAgregarResidencia() {
        if (usuarioActual == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Para agregar una residencia necesitas iniciar sesión. ¿Quieres hacerlo ahora?",
                    ButtonType.YES, ButtonType.NO);
            alert.setHeaderText(null);
            alert.showAndWait()
                    .filter(b -> b == ButtonType.YES)
                    .ifPresent(b -> SceneManager.getInstance().showLoginView());
            return;
        }
        // TODO: abrir la vista de agregar residencia
    }

}