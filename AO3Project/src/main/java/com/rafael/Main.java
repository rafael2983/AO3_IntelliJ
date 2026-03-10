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

public class Main extends Application {

    private BorderPane root;
    private VBox sidebar;
    private StackPane centerWrapper;
    private boolean isSidebarVisible = false; // Initially hidden
    private Node homeContent;
    private static final double SIDEBAR_WIDTH = 220;

    @Override
    public void start(Stage stage) {
        root = new BorderPane();
        centerWrapper = new StackPane();
        centerWrapper.setAlignment(Pos.TOP_CENTER);

        homeContent = new HomeView().getView();
        sidebar = createSidebar();

        // The main content area that will be overlaid by the sidebar
        StackPane contentArea = new StackPane();
        contentArea.getChildren().addAll(centerWrapper, sidebar);
        root.setCenter(contentArea);

        root.setTop(createTopBar());
        setCenterContent(homeContent);
        root.setBottom(createFooter());

        // Initial state: sidebar is hidden
        sidebar.setTranslateX(-SIDEBAR_WIDTH);
        sidebar.setVisible(false);


        Scene scene = new Scene(root, 1280, 800);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setTitle("AO3 UI Mock - Professional");
        stage.setScene(scene);
        stage.show();
    }

    private HBox createTopBar() {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(10, 25, 10, 25));
        topBar.getStyleClass().add("top-bar");
        topBar.setAlignment(Pos.CENTER_LEFT);

        Button menuButton = new Button("☰");
        menuButton.getStyleClass().add("menu-button");
        menuButton.setOnAction(e -> toggleSidebar());

        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/logo.png")));
        logo.setFitHeight(28);
        logo.setPreserveRatio(true);

        Label title = new Label("Archive of Our Own");
        title.getStyleClass().add("logo-text");
        title.setOnMouseClicked(e -> setCenterContent(homeContent));
        title.setCursor(javafx.scene.Cursor.HAND);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ToggleButton themeToggle = createThemeToggle();

        Hyperlink fandoms = new Hyperlink("Fandoms");
        fandoms.setOnAction(e -> setCenterContent(homeContent));

        Hyperlink browse = new Hyperlink("Browse");
        browse.setOnAction(e -> setCenterContent(new BrowseView().getView()));

        Hyperlink search = new Hyperlink("Search");
        search.setOnAction(e -> setCenterContent(new SearchView().getView()));

        Hyperlink about = new Hyperlink("About");
        about.setOnAction(e -> setCenterContent(new AboutView().getView()));

        TextField searchField = new TextField();
        searchField.setPromptText("Search works, tags, etc.");
        searchField.getStyleClass().add("search-field");

        topBar.getChildren().addAll(menuButton, logo, title, spacer, themeToggle, fandoms, browse, search, about, searchField);
        return topBar;
    }

    private VBox createSidebar() {
        VBox newSidebar = new VBox(10);
        newSidebar.setPadding(new Insets(20, 0, 20, 0));
        newSidebar.setPrefWidth(SIDEBAR_WIDTH);
        newSidebar.setMaxWidth(SIDEBAR_WIDTH);
        newSidebar.getStyleClass().add("sidebar");
        StackPane.setAlignment(newSidebar, Pos.CENTER_LEFT); // Align to the left of the StackPane

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
            if (newVal) {
                transition.setToX(20);
                root.getStyleClass().add("dark-theme");
                toggleContainer.getStyleClass().add("toggle-on");
            } else {
                transition.setToX(0);
                root.getStyleClass().remove("dark-theme");
                toggleContainer.getStyleClass().remove("toggle-on");
            }
            transition.play();
        });
        return themeToggle;
    }

    private void setCenterContent(Node content) {
        centerWrapper.getChildren().setAll(content);
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

    public static void main(String[] args) {
        launch(args);
    }
}
