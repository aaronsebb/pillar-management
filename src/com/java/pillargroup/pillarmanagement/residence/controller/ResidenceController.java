package com.java.pillargroup.pillarmanagement.residence.controller;

import com.java.pillargroup.pillarmanagement.addresses.model.Address;
import com.java.pillargroup.pillarmanagement.categories.model.Category;
import com.java.pillargroup.pillarmanagement.categories.service.CategoryService;
import com.java.pillargroup.pillarmanagement.exception.ServiceException;
import com.java.pillargroup.pillarmanagement.residence.model.Residence;
import com.java.pillargroup.pillarmanagement.residence.service.ResidenceService;
import com.java.pillargroup.pillarmanagement.users.RegistroDireccionController;
import com.java.pillargroup.pillarmanagement.users.UserDto;
import com.java.pillargroup.pillarmanagement.util.SceneManager;
import java.util.function.UnaryOperator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

/**
 * Vista para crear una residencia. Solo usuarios con sesión iniciada.
 */
public class ResidenceController {

    private static final int STATUS_DISPONIBLE = 1; // residence_status: 1 = Disponible
    private static final double MAX_MONEY = 99_999_999.99; // decimal(10,2)

    @FXML
    private TextField nameField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private ComboBox<Category> categoryCombo;

    @FXML
    private TextField priceField;

    @FXML
    private TextField monthlyField;

    @FXML
    private TextField imageField;

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

    private final ResidenceService residenceService = new ResidenceService();
    private final CategoryService categoryService = new CategoryService();

    private UserDto usuarioActual;

    @FXML
    private void initialize() {
        // Límites de las columnas de la BD
        limitar(nameField, 80);
        limitar(imageField, 120);
        descriptionArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 160 ? change : null));

        cargarCategorias();
        configurarUbicaciones();
    }

    public void setUsuarioActual(UserDto usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    private void limitar(TextField field, int max) {
        UnaryOperator<TextFormatter.Change> filter = c -> c.getControlNewText().length() <= max ? c : null;
        field.setTextFormatter(new TextFormatter<String>(filter));
    }

    private void cargarCategorias() {
        categoryCombo.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category c) {
                return c == null ? "" : c.getCategoryName();
            }

            @Override
            public Category fromString(String s) {
                return null;
            }
        });
        try {
            categoryCombo.getItems().setAll(categoryService.listarCategorias());
        } catch (Exception e) {
            // Sin categorías: el campo es opcional, se deja vacío.
        }
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
            cityCombo.setValue(null);
            cityCombo.setDisable(country == null);
        });

        cityCombo.valueProperty().addListener((obs, old, city) -> {
            districtCombo.getItems().clear();
            if (city != null) {
                districtCombo.getItems().addAll(
                        RegistroDireccionController.UBICACIONES.get(countryCombo.getValue()).get(city));
            }
            districtCombo.setValue(null);
            districtCombo.setDisable(city == null);
        });
    }

    @FXML
    private void handleGuardar() {
        try {
            if (usuarioActual == null || usuarioActual.getUserId() == null) {
                throw new ServiceException("Debes iniciar sesión para agregar una residencia.");
            }

            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                throw new ServiceException("El nombre de la residencia es obligatorio.");
            }

            double total = leerMonto(priceField, "El precio total");
            double monthly = leerMonto(monthlyField, "El pago mensual");

            int categoryId = categoryCombo.getValue() == null
                    ? 0 : Integer.parseInt(categoryCombo.getValue().getCategoryId());

            String image = imageField.getText().trim();

            Residence residence = new Residence(null, categoryId, STATUS_DISPONIBLE,
                    image.isEmpty() ? null : image, name, descriptionArea.getText().trim(),
                    total, monthly, null, usuarioActual.getUserId());

            Address address = new Address(null,
                    countryCombo.getValue(), cityCombo.getValue(), districtCombo.getValue(),
                    avenueField.getText().trim(), streetField.getText().trim(), houseField.getText().trim());

            residenceService.createWithAddress(residence, address);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "La residencia se agregó correctamente.");
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

    private double leerMonto(TextField field, String etiqueta) throws ServiceException {
        try {
            double value = Double.parseDouble(field.getText().trim().replace(",", ""));
            if (value <= 0 || value > MAX_MONEY) {
                throw new NumberFormatException();
            }
            return value;
        } catch (NumberFormatException e) {
            throw new ServiceException(etiqueta + " debe ser un número mayor a 0.");
        }
    }
}