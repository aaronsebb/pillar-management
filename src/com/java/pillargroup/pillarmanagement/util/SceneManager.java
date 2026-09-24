package com.java.pillargroup.pillarmanagement.util;

import com.java.pillargroup.pillarmanagement.users.dto.UserDto;
import com.java.pillargroup.pillarmanagement.residence.controller.DashboardController;
import com.java.pillargroup.pillarmanagement.users.controller.RegistroController;
import com.java.pillargroup.pillarmanagement.users.controller.RegistroDireccionController;
import com.java.pillargroup.pillarmanagement.users.draft.RegistroDraft;
import com.java.pillargroup.pillarmanagement.residence.controller.ResidenceController;
import com.java.pillargroup.pillarmanagement.residence.controller.DetalleResidenceController;
import com.java.pillargroup.pillarmanagement.residence.model.Residence;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static SceneManager instance;

    private Stage stage;

    private SceneManager() {
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void init(Stage stage) {
        this.stage = stage;
    }

    public void showLoginView() {
        FXMLLoader loader = load("login-view.fxml");
        setRoot(loader, "Iniciar sesión");
    }

    public void showRegistroView() {
        FXMLLoader loader = load("registro-view.fxml");
        setRoot(loader, "Crear cuenta");
    }
    
    public void showRegistroView(RegistroDraft draft) {
        FXMLLoader loader = load("registro-view.fxml");
        RegistroController controller = loader.getController();
        controller.setDraft(draft);
        setRoot(loader, "Crear cuenta");
    }

    public void showRegistroDireccionView(RegistroDraft draft) {
        FXMLLoader loader = load("registro-direccion-view.fxml");
        RegistroDireccionController controller = loader.getController();
        controller.setDraft(draft);
        setRoot(loader, "Crear cuenta - Dirección");
    }


    public void showDashboardView() {
    showDashboardView(null);
    }
    
    public void showDashboardView(UserDto usuarioActual) {
        FXMLLoader loader = load("dashboard-view.fxml");
        DashboardController controller = loader.getController();
        controller.setUsuarioActual(usuarioActual);
        setRoot(loader, "Dashboard");
        }
    
        public void showCrearResidenciaView(UserDto usuarioActual) {
        FXMLLoader loader = load("residence-view.fxml");
        ResidenceController controller = loader.getController();
        controller.setUsuarioActual(usuarioActual);
        setRoot(loader, "Agregar residencia");
    }

    public void showDetalleResidenciaView(Residence residence, UserDto usuarioActual) {
        FXMLLoader loader = load("detalle-residence-view.fxml");
        DetalleResidenceController controller = loader.getController();
        controller.setDatos(residence, usuarioActual);
        setRoot(loader, residence.getResidenceName());
    }

    private FXMLLoader load(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/resources/view/" + fxmlFile));
            loader.load();
            return loader;
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo cargar la vista: " + fxmlFile, e);
        }
    }

    private void setRoot(FXMLLoader loader, String title) {
        Parent root = loader.getRoot();

        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }

        stage.setTitle(title);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }
    
}