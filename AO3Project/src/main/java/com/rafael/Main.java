package com.rafael;

import javafx.animation.*;
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

import java.net.URL;

public class Main extends Application {

    private BorderPane root;
    private VBox sidebar;
    private static StackPane centerWrapper;
    private static Main instance;
    private boolean isSidebarVisible = false;
    private Node homeContent;
    private static final double SIDEBAR_WIDTH = 250;
    private static ToggleButton themeToggleReference;
    private static boolean immersionModeEnabled = true;
    private static boolean isLoggedIn = false;
    private static String currentUsername = null;

    public static boolean isImmersionModeEnabled() {
        return immersionModeEnabled;
    }

    public static void setImmersionModeEnabled(boolean value) {
        immersionModeEnabled = value;
    }

    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static void setLoggedIn(boolean value) {
        isLoggedIn = value;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    public static void logout() {
        isLoggedIn = false;
        currentUsername = null;
    }

    @Override
    public void start(Stage stage) {
        instance = this;
        root = new BorderPane();
        centerWrapper = new StackPane();
        centerWrapper.setAlignment(Pos.TOP_CENTER);

        homeContent = new HomeView().getView();
        sidebar = createSidebar();

        StackPane contentArea = new StackPane();
        contentArea.getChildren().addAll(centerWrapper, sidebar);
        root.setCenter(contentArea);

        root.setTop(createTopBar());
        centerWrapper.getChildren().setAll(homeContent);
        root.setBottom(createFooter());

        sidebar.setTranslateX(-SIDEBAR_WIDTH);
        sidebar.setVisible(false);

        root.getStyleClass().add("default-theme");

        Scene scene = new Scene(root, 1280, 800);
        URL cssUrl = getClass().getResource("/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        stage.setTitle("AO3 UI Mock - Modernized & Animated");
        stage.setScene(scene);
        stage.show();
    }

    public static BorderPane getRoot() {
        return instance.root;
    }

    private HBox createTopBar() {
        HBox topBar = new HBox(20);
        topBar.getStyleClass().add("top-bar");
        topBar.setAlignment(Pos.CENTER_LEFT);

        Button menuButton = new Button("☰");
        menuButton.getStyleClass().add("menu-button");
        menuButton.setOnAction(e -> toggleSidebar());

        HBox logoBox = new HBox(12);
        logoBox.setAlignment(Pos.CENTER_LEFT);

        java.io.InputStream logoStream = getClass().getResourceAsStream("/logo.png");
        if (logoStream != null) {
            ImageView logo = new ImageView(new Image(logoStream));
            logo.setFitHeight(34);
            logo.setPreserveRatio(true);
            logoBox.getChildren().add(logo);
        }

        Label title = new Label("Archive of Our Own");
        title.getStyleClass().add("logo-text");
        title.setOnMouseClicked(e -> setCenterContent(new HomeView().getView()));
        title.setCursor(javafx.scene.Cursor.HAND);
        logoBox.getChildren().add(title);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox navLinks = new HBox(8);
        navLinks.setAlignment(Pos.CENTER);
        navLinks.getChildren().addAll(
                createHyperlink("Home", e -> setCenterContent(new HomeView().getView())),
                createHyperlink("Fandoms", e -> setCenterContent(new FandomPage().getView())),
                createHyperlink("Browse", e -> setCenterContent(new BrowseView().getView())),
                createHyperlink("Search", e -> setCenterContent(new SearchView().getView())),
                createHyperlink("About", e -> setCenterContent(new AboutView().getView()))
        );

        topBar.getChildren().addAll(
                menuButton,
                logoBox,
                spacer,
                navLinks,
                createThemeToggle(),
                createImmersionToggle()
        );

        return topBar;
    }

    private Hyperlink createHyperlink(String text, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Hyperlink link = new Hyperlink(text);
        link.setOnAction(action);
        link.getStyleClass().add("hyperlink");
        return link;
    }

    private VBox createSidebar() {
        VBox newSidebar = new VBox(10);
        newSidebar.setPrefWidth(SIDEBAR_WIDTH);
        newSidebar.setMaxWidth(SIDEBAR_WIDTH);
        newSidebar.getStyleClass().add("sidebar");
        StackPane.setAlignment(newSidebar, Pos.CENTER_LEFT);

        newSidebar.getChildren().addAll(
                createSidebarButton("My Works"),
                createSidebarButton("Bookmarks"),
                createSidebarButton("Profile")
        );
        return newSidebar;
    }

    private Button createSidebarButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("sidebar-button");
        button.setMaxWidth(Double.MAX_VALUE);

        switch (text) {
            case "My Works" -> button.setOnAction(e -> {
                Main.setCenterContent(new MyWorks().getView());
                hideSidebar();
            });

            case "Bookmarks" -> button.setOnAction(e -> {
                Main.setCenterContent(new BookmarksView().getView());
                hideSidebar();
            });

            case "Profile" -> button.setOnAction(e -> {
                if (Main.isLoggedIn()) {
                    Main.setCenterContent(new ProfileView().getView());
                } else {
                    Main.setCenterContent(new LoginView().getView());
                }
                hideSidebar();
            });
        }

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
        ToggleButton themeToggle = new ToggleButton("Light Mode");

        themeToggleReference = themeToggle;
        themeToggle.getStyleClass().add("theme-toggle");

        StackPane toggleContainer = new StackPane();
        toggleContainer.getStyleClass().add("toggle-container");
        Circle knob = new Circle(12);
        knob.getStyleClass().add("toggle-knob");
        StackPane.setAlignment(knob, Pos.CENTER_LEFT);
        StackPane.setMargin(knob, new Insets(0, 0, 0, 4));
        toggleContainer.getChildren().add(knob);
        themeToggle.setGraphic(toggleContainer);

        themeToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            TranslateTransition transition = new TranslateTransition(Duration.millis(300), knob);
            transition.setInterpolator(Interpolator.SPLINE(0.25, 0.1, 0.25, 1));
            themeToggle.setText(newVal ? "Dark Mode" : "Light Mode");

            if (newVal) {
                transition.setToX(24);
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

            refreshProfileIfOpen();

            if (centerWrapper != null && !centerWrapper.getChildren().isEmpty()) {
                Node currentView = centerWrapper.getChildren().get(0);

                if (currentView instanceof StackPane stack &&
                        stack.getUserData() instanceof ReaderView reader) {

                    if (immersionModeEnabled) {
                        Story story = reader.getCurrentStory();

                        ReaderView newReader = new ReaderView();
                        ScrollPane newView = newReader.getView(story, root);

                        setCenterContent(newView);
                    } else {
                        reader.refreshEffects("");
                        root.getStyleClass().removeIf(s -> s.startsWith("genre-"));
                    }
                }
            }
        });
        return themeToggle;
    }

    private ToggleButton createImmersionToggle() {
        ToggleButton immersionToggle = new ToggleButton("Immersion ON");
        immersionToggle.setSelected(true);

        immersionToggle.setOnAction(e -> {
            boolean current = immersionToggle.isSelected();

            setImmersionModeEnabled(current);

            if (!current && centerWrapper != null && !centerWrapper.getChildren().isEmpty()) {
                Node currentView = centerWrapper.getChildren().get(0);

                if (currentView instanceof StackPane stack &&
                        stack.getUserData() instanceof ReaderView reader) {

                    reader.refreshEffects("");
                }
            }

            immersionToggle.setText(current ? "Immersion ON" : "Immersion OFF");

            refreshProfileIfOpen();

            if (centerWrapper != null && !centerWrapper.getChildren().isEmpty()) {
                Node currentView = centerWrapper.getChildren().get(0);

                if (currentView instanceof StackPane stack &&
                        stack.getUserData() instanceof ReaderView reader) {

                    Story story = reader.getCurrentStory();

                    ReaderView newReader = new ReaderView();
                    ScrollPane newView = newReader.getView(story, root);

                    setCenterContent(newView);
                }
            }
        });

        return immersionToggle;
    }

    public static boolean isThemeCustomizationEnabled() {
        return themeToggleReference != null && themeToggleReference.isSelected();
    }

    public static void refreshProfileIfOpen() {
        if (centerWrapper != null && !centerWrapper.getChildren().isEmpty()) {
            Node currentView = centerWrapper.getChildren().get(0);

            if (currentView instanceof ScrollPane scrollPane) {
                Object userData = scrollPane.getUserData();
                if ("profile-view".equals(userData)) {
                    setCenterContent(new ProfileView().getView());
                }
            }
        }
    }

    public static void setCenterContent(Node content) {
        if (centerWrapper != null) {
            content.setOpacity(0);
            content.setTranslateY(30);

            if (!centerWrapper.getChildren().isEmpty()) {
                Node oldContent = centerWrapper.getChildren().get(0);

                FadeTransition fadeOut = new FadeTransition(Duration.millis(150), oldContent);
                fadeOut.setToValue(0);

                TranslateTransition slideDown = new TranslateTransition(Duration.millis(150), oldContent);
                slideDown.setByY(10);

                ParallelTransition ptOut = new ParallelTransition(fadeOut, slideDown);
                ptOut.setOnFinished(e -> {
                    centerWrapper.getChildren().setAll(content);
                    playEntryAnimation(content);
                });
                ptOut.play();
            } else {
                centerWrapper.getChildren().setAll(content);
                playEntryAnimation(content);
            }
        }
    }

    private static void playEntryAnimation(Node content) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(400), content);
        fadeIn.setToValue(1);

        TranslateTransition slideUp = new TranslateTransition(Duration.millis(400), content);
        slideUp.setToY(0);
        slideUp.setInterpolator(Interpolator.SPLINE(0.25, 0.1, 0.25, 1));

        ParallelTransition ptIn = new ParallelTransition(fadeIn, slideUp);
        ptIn.play();
    }

    private void toggleSidebar() {
        TranslateTransition transition = new TranslateTransition(Duration.millis(350), sidebar);
        transition.setInterpolator(Interpolator.SPLINE(0.25, 0.1, 0.25, 1));

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

    private void hideSidebar() {
        if (!isSidebarVisible) {
            return;
        }

        TranslateTransition transition = new TranslateTransition(Duration.millis(250), sidebar);
        transition.setInterpolator(Interpolator.SPLINE(0.25, 0.1, 0.25, 1));
        transition.setToX(-SIDEBAR_WIDTH);
        transition.setOnFinished(e -> sidebar.setVisible(false));
        transition.play();

        isSidebarVisible = false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}