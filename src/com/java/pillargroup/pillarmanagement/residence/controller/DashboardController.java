package com.java.pillargroup.pillarmanagement.residence.controller;

import com.java.pillargroup.pillarmanagement.residence.model.Residence;
import com.java.pillargroup.pillarmanagement.residence.service.ResidenceService;
import com.java.pillargroup.pillarmanagement.users.dto.UserDto;
import com.java.pillargroup.pillarmanagement.util.SceneManager;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class DashboardController {

    private static final int STATUS_DISPONIBLE = 1;
    private static final double CARD_WIDTH = 300;
    private static final double IMAGE_HEIGHT = 150;

    @FXML
    private Label bienvenidaLabel;

    @FXML
    private Button registerButton;

    @FXML
    private Button loginButton;

    @FXML
    private Button logoutButton;

    @FXML
    private TextField searchField;

    @FXML
    private Label availableCountLabel;

    @FXML
    private TilePane residencesPane;

    @FXML
    private VBox emptyState;

    private final ResidenceService residenceService = new ResidenceService();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

    private List<Residence> allResidences = new ArrayList<>();
    private UserDto usuarioActual;

    @FXML
    private void initialize() {
        emptyState.managedProperty().bind(emptyState.visibleProperty());
        residencesPane.managedProperty().bind(residencesPane.visibleProperty());

        searchField.textProperty().addListener((obs, oldText, text) -> mostrar(filtrar(text)));

        loadResidences();
    }

    public void setUsuarioActual(UserDto usuarioActual) {
        this.usuarioActual = usuarioActual;

        if (bienvenidaLabel == null) {
            return;
        }

        boolean logueado = usuarioActual != null;

        bienvenidaLabel.setText(logueado ? "Bienvenido, " + usuarioActual.getFirstName() : "");
        mostrarNodo(bienvenidaLabel, logueado);
        mostrarNodo(logoutButton, logueado);
        mostrarNodo(loginButton, !logueado);
        mostrarNodo(registerButton, !logueado);
    }

    private void mostrarNodo(Node nodo, boolean visible) {
        nodo.setVisible(visible);
        nodo.setManaged(visible);
    }
    
    private void loadResidences() {
        Task<List<Residence>> task = new Task<>() {
            @Override
            protected List<Residence> call() throws Exception {
                List<Residence> disponibles = new ArrayList<>();
                for (Residence r : residenceService.findAll()) {
                    if (r.getStatusId() == STATUS_DISPONIBLE) {
                        disponibles.add(r);
                    }
                }
                return disponibles;
            }
        };

        task.setOnSucceeded(e -> {
            allResidences = task.getValue();
            mostrar(filtrar(searchField.getText()));
        });
        task.setOnFailed(e -> {
            allResidences = new ArrayList<>();
            mostrar(allResidences);
            availableCountLabel.setText("No se pudieron cargar las residencias");
        });

        Thread thread = new Thread(task, "load-residences");
        thread.setDaemon(true);
        thread.start();
    }

    private List<Residence> filtrar(String text) {
        if (text == null || text.isBlank()) {
            return allResidences;
        }
        String[] words = normalizar(text).trim().split("\\s+");
        List<Residence> result = new ArrayList<>();
        for (Residence r : allResidences) {
            String haystack = normalizar(valor(r.getResidenceName()) + " " + valor(r.getDepiction()));
            boolean todas = true;
            for (String w : words) {
                if (!haystack.contains(w)) {
                    todas = false;
                    break;
                }
            }
            if (todas) {
                result.add(r);
            }
        }
        return result;
    }

    private String normalizar(String s) {
        return Normalizer.normalize(s.toLowerCase(), Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }

    private String valor(String s) {
        return s == null ? "" : s;
    }

    private void mostrar(List<Residence> residences) {
        residencesPane.getChildren().clear();
        for (Residence r : residences) {
            residencesPane.getChildren().add(createCard(r));
        }

        boolean vacio = residences.isEmpty();
        residencesPane.setVisible(!vacio);
        emptyState.setVisible(vacio);

        int n = residences.size();
        availableCountLabel.setText(n + (n == 1 ? " residencia disponible" : " residencias disponibles"));
    }

    private VBox createCard(Residence residence) {
        VBox card = new VBox(8);
        card.getStyleClass().add("residence-card");
        card.setPrefWidth(CARD_WIDTH);
        card.setMaxWidth(CARD_WIDTH);
        card.setOnMouseClicked(e -> SceneManager.getInstance().showDetalleResidenciaView(residence, usuarioActual));

        Label name = new Label(residence.getResidenceName());
        name.getStyleClass().add("card-title");
        name.setWrapText(true);
        name.setAlignment(Pos.TOP_LEFT);
        name.setMinHeight(40);

        Label description = new Label(residence.getDepiction());
        description.getStyleClass().add("card-description");
        description.setWrapText(true);
        description.setAlignment(Pos.TOP_LEFT);
        description.setMinHeight(54);
        description.setMaxHeight(54);

        Label total = new Label(currencyFormat.format(residence.getLumpSum()));
        total.getStyleClass().add("card-price");
        Label monthly = new Label(currencyFormat.format(residence.getMonthlyPayment()) + " / mes");
        monthly.getStyleClass().add("card-price-caption");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox prices = new HBox(8, total, spacer, monthly);
        prices.setAlignment(Pos.BASELINE_LEFT);

        VBox body = new VBox(6, name, description, prices);
        body.getStyleClass().add("card-body");

        card.getChildren().addAll(createImage(residence.getUrlImage()), body);
        return card;
    }

    private StackPane createImage(String url) {
        StackPane container = new StackPane();
        container.getStyleClass().add("card-image");
        container.setPrefSize(CARD_WIDTH, IMAGE_HEIGHT);
        container.setMinHeight(IMAGE_HEIGHT);
        container.setMaxHeight(IMAGE_HEIGHT);

        Label placeholder = new Label("⌂");
        placeholder.getStyleClass().add("card-image-placeholder");
        container.getChildren().add(placeholder);

        if (url != null && !url.isBlank()) {
            try {
                Image image = new Image(url, CARD_WIDTH, IMAGE_HEIGHT, false, true, true);
                ImageView view = new ImageView(image);
                view.setFitWidth(CARD_WIDTH);
                view.setFitHeight(IMAGE_HEIGHT);
                Runnable showIfLoaded = () -> {
                    if (image.getProgress() >= 1.0 && !image.isError()) {
                        container.getChildren().setAll(view);
                    }
                };
                image.progressProperty().addListener((obs, o, p) -> showIfLoaded.run());
                showIfLoaded.run();
            } catch (IllegalArgumentException e) {
            }
        }
        return container;
    }

    @FXML
    private void handleIrALogin() {
        SceneManager.getInstance().showLoginView();
    }

    @FXML
    private void handleIrARegistro() {
        SceneManager.getInstance().showRegistroView();
    }

    @FXML
    private void handleCerrarSesion() {
        SceneManager.getInstance().showDashboardView();
    }

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
        SceneManager.getInstance().showCrearResidenciaView(usuarioActual);
    }

}