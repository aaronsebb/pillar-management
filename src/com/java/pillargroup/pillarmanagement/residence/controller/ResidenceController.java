package com.java.pillargroup.pillarmanagement.residence.controller;

import com.java.pillargroup.pillarmanagement.addresses.model.Address;
import com.java.pillargroup.pillarmanagement.addresses.service.AddressService;
import com.java.pillargroup.pillarmanagement.categories.model.Category;
import com.java.pillargroup.pillarmanagement.categories.service.CategoryService;
import com.java.pillargroup.pillarmanagement.exception.ServiceException;
import com.java.pillargroup.pillarmanagement.residence.model.Residence;
import com.java.pillargroup.pillarmanagement.residence.service.ResidenceService;
import com.java.pillargroup.pillarmanagement.users.controller.RegistroDireccionController;
import com.java.pillargroup.pillarmanagement.users.dto.UserDto;
import com.java.pillargroup.pillarmanagement.util.SceneManager;
import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.UnaryOperator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResidenceController {

    private static final int STATUS_DISPONIBLE = 1;
    private static final double MAX_MONEY = 99_999_999.99;
    private static final double PREVIEW_SIZE = 120;

    @FXML
    private Label subtitleLabel;

    @FXML
    private Label formTitleLabel;

    @FXML
    private Button guardarButton;

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
    private Button buscarGoogleButton;

    @FXML
    private ImageView imagePreview;

    @FXML
    private Label imageStatusLabel;

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
    private final AddressService addressService = new AddressService();

    private UserDto usuarioActual;
    private Residence residenciaEditar;
    private Address direccionEditar;

    @FXML
    private void initialize() {
        limitar(nameField, 80);
        limitar(imageField, 120);
        descriptionArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 160 ? change : null));

        cargarCategorias();
        configurarUbicaciones();

        imageField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) {
                previsualizarImagen();
            }
        });
    }

    public void setUsuarioActual(UserDto usuarioActual) {
        this.usuarioActual = usuarioActual;
    }
    
    public void setResidenciaEditar(Residence residencia) {
        this.residenciaEditar = residencia;

        if (subtitleLabel != null) {
            subtitleLabel.setText("Actualizar residencia");
        }
        if (formTitleLabel != null) {
            formTitleLabel.setText("Editar datos de la residencia");
        }
        if (guardarButton != null) {
            guardarButton.setText("Actualizar residencia");
        }

        nameField.setText(residencia.getResidenceName());
        descriptionArea.setText(residencia.getDepiction());
        priceField.setText(String.valueOf(residencia.getLumpSum()));
        monthlyField.setText(String.valueOf(residencia.getMonthlyPayment()));
        imageField.setText(residencia.getUrlImage());

        for (Category c : categoryCombo.getItems()) {
            if (Integer.parseInt(c.getCategoryId()) == residencia.getCategoryId()) {
                categoryCombo.setValue(c);
                break;
            }
        }

        try {
            direccionEditar = addressService.findById(residencia.getAddressId()).orElse(null);
        } catch (Exception e) {
            direccionEditar = null;
        }

        if (direccionEditar != null) {
            countryCombo.setValue(direccionEditar.getCountry());
            cityCombo.setValue(direccionEditar.getCity());
            districtCombo.setValue(direccionEditar.getDistrict());
            avenueField.setText(direccionEditar.getAvenue());
            streetField.setText(direccionEditar.getStreet());
            houseField.setText(direccionEditar.getHouse());
        }
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
    private void handleBuscarGoogle() {
        String termino = nameField.getText().trim();
        String query = termino.isEmpty() ? "casa en venta" : termino;
        try {
            String url = "https://www.google.com/search?tbm=isch&q="
                    + URLEncoder.encode(query, StandardCharsets.UTF_8);
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                imageStatusLabel.setText("No se pudo abrir el navegador en este equipo. Copia el link manualmente.");
            }
        } catch (Exception e) {
            imageStatusLabel.setText("No se pudo abrir el navegador para buscar imágenes.");
        }
    }

    private void previsualizarImagen() {
        imagePreview.setImage(null);
        imageStatusLabel.setText("");
        String url = imageField.getText().trim();
        if (url.isEmpty()) {
            return;
        }
        try {
            validarUrlImagen(url);
        } catch (ServiceException e) {
            imageStatusLabel.setText(e.getMessage());
            return;
        }

        Image image = new Image(url, PREVIEW_SIZE, PREVIEW_SIZE, true, true, true);
        image.errorProperty().addListener((obs, old, isError) -> {
            if (isError) {
                Platform.runLater(() -> {
                    imagePreview.setImage(null);
                    imageStatusLabel.setText("No se pudo cargar la imagen. Verifica que sea el link directo "
                            + "a la imagen (clic derecho → 'Copiar dirección de la imagen') y no el de la página web.");
                });
            }
        });
        image.progressProperty().addListener((obs, old, progress) -> {
            if (progress.doubleValue() >= 1.0 && !image.isError()) {
                Platform.runLater(() -> {
                    imagePreview.setImage(image);
                    imageStatusLabel.setText("Vista previa cargada correctamente.");
                });
            }
        });
    }

    private void validarUrlImagen(String url) throws ServiceException {
        if (!url.matches("(?i)^https?://.+\\.(jpg|jpeg|png|gif|webp)(\\?.*)?$")) {
            throw new ServiceException("El link debe ser directo a una imagen (terminar en .jpg, .png, .gif o "
                    + ".webp), no el link de una página de resultados de Google.");
        }
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
            if (!image.isEmpty()) {
                validarUrlImagen(image);
            }

            if (residenciaEditar != null) {
                actualizarResidencia(name, descriptionArea.getText().trim(), total, monthly, categoryId, image);
            } else {
                crearResidencia(name, descriptionArea.getText().trim(), total, monthly, categoryId, image);
            }

        } catch (ServiceException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void crearResidencia(String name, String description, double total, double monthly,
            int categoryId, String image) throws ServiceException {

        Residence residence = new Residence(null, categoryId, STATUS_DISPONIBLE,
                image.isEmpty() ? null : image, name, description,
                total, monthly, null, usuarioActual.getUserId());

        Address address = new Address(null,
                countryCombo.getValue(), cityCombo.getValue(), districtCombo.getValue(),
                avenueField.getText().trim(), streetField.getText().trim(), houseField.getText().trim());

        residenceService.createWithAddress(residence, address);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "La residencia se agregó correctamente.");
        alert.setHeaderText(null);
        alert.showAndWait();

        SceneManager.getInstance().showDashboardView(usuarioActual);
    }

    private void actualizarResidencia(String name, String description, double total, double monthly,
            int categoryId, String image) throws ServiceException {
        try {
            residenciaEditar.setResidenceName(name);
            residenciaEditar.setDepiction(description);
            residenciaEditar.setLumpSum(total);
            residenciaEditar.setMonthlyPayment(monthly);
            residenciaEditar.setCategoryId(categoryId);
            residenciaEditar.setUrlImage(image.isEmpty() ? null : image);

            residenceService.update(residenciaEditar);

            Address address = new Address(
                    direccionEditar != null ? direccionEditar.getAddressId() : residenciaEditar.getAddressId(),
                    countryCombo.getValue(), cityCombo.getValue(), districtCombo.getValue(),
                    avenueField.getText().trim(), streetField.getText().trim(), houseField.getText().trim());
            addressService.update(address);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "La residencia se actualizó correctamente.");
            alert.setHeaderText(null);
            alert.showAndWait();

            SceneManager.getInstance().showDashboardView(usuarioActual);

        } catch (IllegalArgumentException e) {
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("No se pudo actualizar la residencia. Intenta de nuevo.");
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

        public void printInvoiceToTxt(String invoiceId, Date invoiceDate, String userId, double interests, String residenceId, double payment, String paymentType) throws IOException {
            String fileName = "../invoices/invoice_" + invoiceId + ".txt";
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
                
        writer.println("===== FACTURA =====");
        writer.println("ID Factura: " + invoiceId);
        writer.println("Fecha: " + dateFormat.format(invoiceDate));
        writer.println("ID Usuario: " + userId);
        writer.println("ID Residencia: " + residenceId);
        writer.println("Intereses: " + String.format(Locale.US, "%.2f", interests));
        writer.println("Pago: " + String.format(Locale.US, "%.2f", payment));
        writer.println("Tipo de Pago: " + paymentType);
        writer.println("====================");
        
    }
}

}