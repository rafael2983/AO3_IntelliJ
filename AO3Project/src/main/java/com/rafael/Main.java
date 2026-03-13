package com.rafael;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

// For the auto-toggle based on system time
import java.time.LocalTime;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

public class Main extends Application {

    private BorderPane root;
    private VBox sidebar;
    private static StackPane centerWrapper;
    private static Main instance; // Static instance for easy access
    private boolean isSidebarVisible = false;
    private Node homeContent;
    private static final double SIDEBAR_WIDTH = 220;
    private static ToggleButton themeToggleReference;


    @Override
    public void start(Stage stage) {
        instance = this; // Initialize singleton instance
        root = new BorderPane();
        centerWrapper = new StackPane();
        centerWrapper.setAlignment(Pos.TOP_CENTER);

        homeContent = new HomeView().getView();
        sidebar = createSidebar();

        StackPane contentArea = new StackPane();
        contentArea.getChildren().addAll(centerWrapper, sidebar);
        root.setCenter(contentArea);

        root.setTop(createTopBar());
        setCenterContent(homeContent);
        root.setBottom(createFooter());

        sidebar.setTranslateX(-SIDEBAR_WIDTH);
        sidebar.setVisible(false);

        root.getStyleClass().add("default-theme");

        setupAutoToggle(); // the system time will cause auto-toggle

        Scene scene = new Scene(root, 1280, 800);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setTitle("AO3 UI Mock - Professional");
        stage.setScene(scene);
        stage.show();
    }

    // Static getter for root, needed by ReaderView
    public static BorderPane getRoot() {
        return instance.root;
    }

    private HBox createTopBar() {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(10, 25, 10, 25));
        topBar.getStyleClass().add("top-bar");
        topBar.setAlignment(Pos.CENTER_LEFT);

        Button menuButton = new Button("☰");
        menuButton.getStyleClass().add("menu-button");
        menuButton.setOnAction(e -> toggleSidebar());

        // Note: Ensure your resources exist in the classpath
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/logo.png")));
        logo.setFitHeight(28);
        logo.setPreserveRatio(true);

        Label title = new Label("Archive of Our Own");
        title.getStyleClass().add("logo-text");
        title.setOnMouseClicked(e -> setCenterContent(homeContent));
        title.setCursor(javafx.scene.Cursor.HAND);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(menuButton, logo, title, spacer, createThemeToggle(),
                createHyperlink("Home", e -> setCenterContent(new HomeView().getView())),
                createHyperlink("Fandoms", e -> setCenterContent(new FandomPage().getView())),
                createHyperlink("Browse", e -> setCenterContent(new BrowseView().getView())),
                createHyperlink("Search", e -> setCenterContent(new SearchView().getView())),
                createHyperlink("About", e -> setCenterContent(new AboutView().getView()))
        );
        return topBar;
    }

    private Hyperlink createHyperlink(String text, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Hyperlink link = new Hyperlink(text);
        link.setOnAction(action);
        return link;
    }

    private VBox createSidebar() {
        VBox newSidebar = new VBox(10);
        newSidebar.setPadding(new Insets(20, 0, 20, 0));
        newSidebar.setPrefWidth(SIDEBAR_WIDTH);
        newSidebar.setMaxWidth(SIDEBAR_WIDTH);
        newSidebar.getStyleClass().add("sidebar");
        StackPane.setAlignment(newSidebar, Pos.CENTER_LEFT);

        newSidebar.getChildren().addAll(
                createSidebarButton("My Works"),
                createSidebarButton("Bookmarks"),
                createSidebarButton("History"),
                createSidebarButton("Profile")
        );
        return newSidebar;
    }

    private Button createSidebarButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("sidebar-button");
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private HBox createFooter() {
        HBox footer = new HBox();
        footer.getStyleClass().add("footer");
        Label footerText = new Label("The Archive of Our Own is a project of the Organization for Transformative Works.");
        footerText.getStyleClass().add("footer-text");
        footer.getChildren().add(footerText);
        return footer;
    }

    private ToggleButton createThemeToggle() {
        ToggleButton themeToggle = new ToggleButton();
        themeToggleReference = themeToggle;
        themeToggle.getStyleClass().add("theme-toggle");

        StackPane toggleContainer = new StackPane();
        toggleContainer.getStyleClass().add("toggle-container");
        Circle knob = new Circle(10);
        knob.getStyleClass().add("toggle-knob");
        StackPane.setAlignment(knob, Pos.CENTER_LEFT);
        toggleContainer.getChildren().add(knob);
        themeToggle.setGraphic(toggleContainer);

        themeToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            TranslateTransition transition = new TranslateTransition(Duration.millis(200), knob);

            // UI Theme Switching
            if (newVal) {
                transition.setToX(20);
                root.getStyleClass().removeAll("default-theme", "dark-theme");
                root.getStyleClass().add("dark-theme");
                toggleContainer.getStyleClass().add("toggle-on");
            } else {
                transition.setToX(0);
                root.getStyleClass().removeAll("default-theme", "dark-theme");
                root.getStyleClass().removeIf(style -> style.startsWith("genre-"));
                root.getStyleClass().add("default-theme");
                toggleContainer.getStyleClass().remove("toggle-on");
            }
            transition.play();

            // LIVE UPDATE:
            if (centerWrapper != null && !centerWrapper.getChildren().isEmpty()) {
                Node currentView = centerWrapper.getChildren().get(0);
                // Ensure we are talking to the root container of the ReaderView
                if (currentView instanceof StackPane && currentView.getUserData() instanceof ReaderView) {
                    ReaderView reader = (ReaderView) currentView.getUserData();
                    String genre = reader.getCurrentStoryGenre();

                    // Re-apply styles based on state
                    reader.applyGenreTheme(root, genre);
                    reader.refreshEffects(genre);
                }
            }
        });
        return themeToggle;
    }

    public static boolean isThemeCustomizationEnabled() {
        return themeToggleReference != null && themeToggleReference.isSelected();
    }
    public static void setCenterContent(Node content) {
        if (centerWrapper != null) {
            centerWrapper.getChildren().setAll(content);
        }
    }

    private void toggleSidebar() {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), sidebar);
        if (isSidebarVisible) {
            transition.setToX(-SIDEBAR_WIDTH);
            transition.setOnFinished(e -> sidebar.setVisible(false));
        } else {
            sidebar.setVisible(true);
            transition.setToX(0);
        }
        transition.play();
        isSidebarVisible = !isSidebarVisible;
    }

    private void setupAutoToggle() {
        Timeline timer = new Timeline(new KeyFrame(Duration.minutes(1), e -> {
            LocalTime now = LocalTime.now();
            // Check if it's 10:00 PM (22:00) or later
            boolean isNightTime = now.isAfter(LocalTime.of(22, 0));

            // Only toggle if the current state doesn't match the time-based preference
            // This ensures the user can manually override the toggle without it flipping back
            if (themeToggleReference != null && themeToggleReference.isSelected() != isNightTime) {
                themeToggleReference.setSelected(isNightTime);
            }
        }));

        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}