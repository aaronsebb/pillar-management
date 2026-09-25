package com.java.pillargroup.pillarmanagement.residence.controller;

import com.java.pillargroup.pillarmanagement.addresses.model.Address;
import com.java.pillargroup.pillarmanagement.addresses.service.AddressService;
import com.java.pillargroup.pillarmanagement.categories.service.CategoryService;
import com.java.pillargroup.pillarmanagement.residence.model.Residence;
import com.java.pillargroup.pillarmanagement.residence.service.ResidenceService;
import com.java.pillargroup.pillarmanagement.users.service.AuthService;
import com.java.pillargroup.pillarmanagement.users.dto.UserDto;
import com.java.pillargroup.pillarmanagement.util.SceneManager;
import java.text.NumberFormat;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class DetalleResidenceController {

    private static final double IMAGE_WIDTH = 460;
    private static final double IMAGE_HEIGHT = 300;

    @FXML
    private StackPane imageContainer;

    @FXML
    private HBox ownerActions;

    @FXML
    private Label nameLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label monthlyLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label publisherLabel;

    @FXML
    private Label addressLabel;

    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    private final ResidenceService residenceService = new ResidenceService();

    private UserDto usuarioActual;
    private Residence residenciaActual;

    public void setDatos(Residence residence, UserDto usuarioActual) {
        this.usuarioActual = usuarioActual;
        this.residenciaActual = residence;

        nameLabel.setText(residence.getResidenceName());
        descriptionLabel.setText(vacioSiNulo(residence.getDepiction(), "Esta residencia no tiene descripción."));
        priceLabel.setText(currencyFormat.format(residence.getLumpSum()));
        monthlyLabel.setText(currencyFormat.format(residence.getMonthlyPayment()) + " / mes");
        statusLabel.setText(nombreEstado(residence.getStatusId()));

        cargarImagen(residence.getUrlImage());
        cargarCategoria(residence.getCategoryId());
        cargarDireccion(residence.getAddressId());
        cargarPublicador(residence.getUserId());

        boolean esPropietario = usuarioActual != null && usuarioActual.getUserId() != null
                && usuarioActual.getUserId().equals(residence.getUserId());
        ownerActions.setVisible(esPropietario);
        ownerActions.setManaged(esPropietario);
    }

    private String nombreEstado(int statusId) {
        switch (statusId) {
            case 1: return "Disponible";
            case 2: return "Vendida";
            case 3: return "Reservada";
            default: return "Sin estado";
        }
    }

    private void cargarImagen(String url) {
        Label placeholder = new Label("⌂");
        placeholder.getStyleClass().add("card-image-placeholder");
        placeholder.setStyle("-fx-font-size: 90px;");
        imageContainer.getChildren().setAll(placeholder);

        if (url == null || url.isBlank()) {
            return;
        }
        try {
            Image image = new Image(url, IMAGE_WIDTH, IMAGE_HEIGHT, false, true, true);
            ImageView view = new ImageView(image);
            view.setFitWidth(IMAGE_WIDTH);
            view.setFitHeight(IMAGE_HEIGHT);
            Runnable showIfLoaded = () -> {
                if (image.getProgress() >= 1.0 && !image.isError()) {
                    imageContainer.getChildren().setAll(view);
                }
            };
            image.progressProperty().addListener((obs, o, p) -> showIfLoaded.run());
            showIfLoaded.run();
        } catch (IllegalArgumentException e) {
        }
    }

    private void cargarCategoria(int categoryId) {
        String text = "Sin categoría";
        if (categoryId > 0) {
            try {
                text = new CategoryService().obtenerPorId(String.valueOf(categoryId)).getCategoryName();
            } catch (Exception e) {
            }
        }
        categoryLabel.setText(text);
    }

    private void cargarDireccion(String addressId) {
        String text = "Dirección no disponible";
        try {
            Address a = new AddressService().findById(addressId).orElse(null);
            if (a != null) {
                text = a.getAvenue() + ", " + a.getStreet() + ", casa " + a.getHouse()
                        + "\n" + a.getDistrict() + ", " + a.getCity() + ", " + a.getCountry();
            }
        } catch (Exception e) {
        }
        addressLabel.setText(text);
    }

    private void cargarPublicador(String userId) {
        String name = new AuthService().findFullNameByUserId(userId);
        publisherLabel.setText(name == null ? "Desconocido" : name);
    }

    private String vacioSiNulo(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }

    @FXML
    private void handleVolver() {
        SceneManager.getInstance().showDashboardView(usuarioActual);
    }

    @FXML
    private void handleActualizar() {
        SceneManager.getInstance().showEditarResidenciaView(residenciaActual, usuarioActual);
    }

    @FXML
    private void handleEliminar() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Seguro que deseas eliminar la residencia \"" + residenciaActual.getResidenceName() + "\"? "
                + "Esta acción no se puede deshacer.", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().filter(b -> b == ButtonType.YES).ifPresent(b -> {
            try {
                residenceService.delete(residenciaActual.getResidenceId());

                Alert ok = new Alert(Alert.AlertType.INFORMATION, "La residencia se eliminó correctamente.");
                ok.setHeaderText(null);
                ok.showAndWait();

                SceneManager.getInstance().showDashboardView(usuarioActual);
            } catch (Exception e) {
                Alert error = new Alert(Alert.AlertType.ERROR, "No se pudo eliminar la residencia. Intenta de nuevo.");
                error.setHeaderText(null);
                error.showAndWait();
            }
        });
    }
}