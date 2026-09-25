package com.java.pillargroup.pillarmanagement.users.controller;

import com.java.pillargroup.pillarmanagement.addresses.model.Address;
import com.java.pillargroup.pillarmanagement.exception.ServiceException;
import com.java.pillargroup.pillarmanagement.users.dto.UserDto;
import com.java.pillargroup.pillarmanagement.users.service.AuthService;
import com.java.pillargroup.pillarmanagement.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ProfileController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private ComboBox<String> countryCombo;

    @FXML
    private ComboBox<String> cityCombo;

    @FXML
    private ComboBox<String> districtCombo;

    @FXML
    private TextField avenueField;

    @FXML
    private TextField streetField;

    @FXML
    private TextField houseField;

    @FXML
    private Label errorLabel;

    private final AuthService authService = new AuthService();

    private UserDto usuarioActual;

    @FXML
    private void initialize() {
        configurarUbicaciones();
    }

    public void setUsuarioActual(UserDto usuarioActual) {
        this.usuarioActual = usuarioActual;

        if (usuarioActual == null) {
            SceneManager.getInstance().showLoginView();
            return;
        }

        nameLabel.setText(usuarioActual.getFirstName() + " " + usuarioActual.getLastName());
        emailLabel.setText(usuarioActual.getEmail());

        cargarDireccionActual();
    }

    private void configurarUbicaciones() {
        countryCombo.getItems().addAll(RegistroDireccionController.UBICACIONES.keySet());
        cityCombo.setDisable(true);
        districtCombo.setDisable(true);

        countryCombo.valueProperty().addListener((obs, old, country) -> {
            cityCombo.getItems().clear();
            if (country != null) {
                cityCombo.getItems().addAll(RegistroDireccionController.UBICACIONES.get(country).keySet());
            }
            if (old != null && !old.equals(country)) {
                cityCombo.setValue(null);
            }
            cityCombo.setDisable(country == null);
        });

        cityCombo.valueProperty().addListener((obs, old, city) -> {
            districtCombo.getItems().clear();
            if (city != null && countryCombo.getValue() != null) {
                districtCombo.getItems().addAll(
                        RegistroDireccionController.UBICACIONES.get(countryCombo.getValue()).get(city));
            }
            if (old != null && !old.equals(city)) {
                districtCombo.setValue(null);
            }
            districtCombo.setDisable(city == null);
        });
    }

    private void cargarDireccionActual() {
        Address direccion = authService.obtenerDireccion(usuarioActual.getUserId());
        if (direccion == null) {
            return;
        }

        countryCombo.setValue(direccion.getCountry());
        cityCombo.setValue(direccion.getCity());
        districtCombo.setValue(direccion.getDistrict());
        avenueField.setText(direccion.getAvenue());
        streetField.setText(direccion.getStreet());
        houseField.setText(direccion.getHouse());
    }

    @FXML
    private void handleGuardar() {
        try {
            if (usuarioActual == null || usuarioActual.getUserId() == null) {
                throw new ServiceException("Debes iniciar sesión para actualizar tu dirección.");
            }

            Address address = new Address(null,
                    countryCombo.getValue(), cityCombo.getValue(), districtCombo.getValue(),
                    avenueField.getText().trim(), streetField.getText().trim(), houseField.getText().trim());

            authService.actualizarDireccion(usuarioActual.getUserId(), address);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Tu dirección se actualizó correctamente.");
            alert.setHeaderText(null);
            alert.showAndWait();

            SceneManager.getInstance().showDashboardView(usuarioActual);

        } catch (ServiceException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleCancelar() {
        SceneManager.getInstance().showDashboardView(usuarioActual);
    }
}