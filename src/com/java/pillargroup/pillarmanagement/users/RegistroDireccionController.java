package com.java.pillargroup.pillarmanagement.users;

import com.java.pillargroup.pillarmanagement.addresses.model.Address;
import com.java.pillargroup.pillarmanagement.exception.ServiceException;
import com.java.pillargroup.pillarmanagement.util.SceneManager;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RegistroDireccionController {

    private static final Map<String, Map<String, List<String>>> UBICACIONES = Map.of(
            "Panamá", Map.of(
                    "Ciudad de Panamá", List.of("Bella Vista", "Betania", "San Francisco"),
                    "Colón", List.of("Colón", "Cristóbal")),
            "Guatemala", Map.of(
                    "Ciudad de Guatemala", List.of("Zona 1", "Zona 10", "Zona 14"),
                    "Mixco", List.of("Zona 1", "Zona 3")));

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

    private RegistroDraft draft;

    @FXML
    private void initialize() {
        countryCombo.getItems().addAll(UBICACIONES.keySet());
        cityCombo.setDisable(true);
        districtCombo.setDisable(true);

        countryCombo.valueProperty().addListener((obs, old, country) -> {
            cityCombo.getItems().clear();
            if (country != null) {
                cityCombo.getItems().addAll(UBICACIONES.get(country).keySet());
            }
            cityCombo.setValue(null);
            cityCombo.setDisable(country == null);
        });

        cityCombo.valueProperty().addListener((obs, old, city) -> {
            districtCombo.getItems().clear();
            if (city != null) {
                districtCombo.getItems().addAll(UBICACIONES.get(countryCombo.getValue()).get(city));
            }
            districtCombo.setValue(null);
            districtCombo.setDisable(city == null);
        });
    }

    public void setDraft(RegistroDraft draft) {
        this.draft = draft;
    }

    @FXML
    private void handleRegresar() {
        SceneManager.getInstance().showRegistroView(draft);
    }

    @FXML
    private void handleOmitir() {
        crearCuenta(null);
    }

    @FXML
    private void handleCrearCuenta() {
        crearCuenta(direccionVacia() ? null : new Address(
                null,
                countryCombo.getValue(),
                cityCombo.getValue(),
                districtCombo.getValue(),
                avenueField.getText().trim(),
                streetField.getText().trim(),
                houseField.getText().trim()));
    }

    private void crearCuenta(Address address) {
        try {
            authService.register(draft.getFirstName(), draft.getLastName(),
                    draft.getEmail(), draft.getPassword(), address);

            UserDto usuario = new UserDto(draft.getFirstName(), draft.getLastName(), draft.getEmail(), null);
            SceneManager.getInstance().showDashboardView(usuario);

        } catch (ServiceException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private boolean direccionVacia() {
        return countryCombo.getValue() == null
                && cityCombo.getValue() == null
                && districtCombo.getValue() == null
                && avenueField.getText().isBlank()
                && streetField.getText().isBlank()
                && houseField.getText().isBlank();
    }
}