package com.rafael;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.List;

public class ReaderView {
    private int currentPage = 0;
    private List<String> pages;
    private Label textLabel = new Label();
    private Story currentStory;
    private Pane effectsLayer;

    public StackPane getView(Story story, BorderPane root) {
        this.currentStory = story;
        applyGenreTheme(root, story.getGenre());

        pages = StoryPageService.paginate(story.getFullText());

        StackPane rootStack = new StackPane();
        effectsLayer = new Pane(); // Now assigned to the class field

        effectsLayer.setMouseTransparent(true);

        // Trigger the unified effects handler
        addBackgroundEffects(effectsLayer, story.getGenre());

        VBox readerContainer = new VBox(20);
        readerContainer.setUserData(this);
        readerContainer.setPadding(new Insets(30));
        readerContainer.setAlignment(Pos.TOP_CENTER);

        Label title = new Label(story.getTitle());
        title.getStyleClass().add("section-title");

        textLabel.setText(pages.get(0));
        textLabel.setWrapText(true);
        textLabel.setMaxWidth(800);
        textLabel.setStyle("-fx-font-size: 16px; -fx-line-spacing: 5; -fx-text-alignment: justify;");
        textLabel.getStyleClass().add("reader-text-label");

        HBox navControls = new HBox(20);
        navControls.setAlignment(Pos.CENTER);

        Button prevBtn = new Button("Previous");
        Button nextBtn = new Button("Next");
        Button backBtn = new Button("Back to Home");
        Label pageIndicator = new Label("Page 1 of " + pages.size());

        backBtn.setOnAction(e -> {
            root.getStyleClass().removeIf(style -> style.startsWith("genre-"));
            Main.setCenterContent(new HomeView().getView());
        });

        prevBtn.setOnAction(e -> changePage(-1, pageIndicator));
        nextBtn.setOnAction(e -> changePage(1, pageIndicator));

        navControls.getChildren().addAll(prevBtn, pageIndicator, nextBtn, backBtn);
        readerContainer.getChildren().addAll(title, textLabel, navControls);

        rootStack.getChildren().addAll(effectsLayer, readerContainer);
        return rootStack;
    }

    private void addBackgroundEffects(Pane layer, String genre) {
        layer.getChildren().clear();
        if (!Main.isThemeCustomizationEnabled()) return;

        switch (genre.toLowerCase()) {
            case "horror": applyHorrorEffects(layer); System.out.println("horror"); break;
            case "fantasy": applyFantasyEffects(layer); break;
            case "sci-fi": applySciFiEffects(layer); break;
            case "romance": applyRomanceEffects(layer); break;
        }
    }

    private void applyRomanceEffects(Pane layer) {
        for (int i = 0; i < 15; i++) {
            Label heart = new Label("❤");
            heart.setStyle("-fx-text-fill: -app-accent-color; -fx-font-size: 20px; -fx-opacity: 0.3;");
            heart.setLayoutX(Math.random() * 1000);
            heart.setLayoutY(Math.random() * 800);
            layer.getChildren().add(heart);
            TranslateTransition tt = new TranslateTransition(Duration.seconds(8 + Math.random() * 4), heart);
            tt.setByY(-100); tt.setCycleCount(Animation.INDEFINITE); tt.setAutoReverse(true); tt.play();
        }
    }

    private void applyHorrorEffects(Pane layer) {
        // --- TUNING AREA ---
        double fogOpacity = 0.4;     // How visible the fog is (0.0 to 1.0)
        double flickerBase = 0.05;   // Base darkness of flicker
        double flickerPulse = 0.2;  // How intense the flicker surge is
        int fogDriftSpeed1 = 20;     // Seconds for first fog layer (lower = faster)
        int fogDriftSpeed2 = 30;     // Seconds for second fog layer
        // -------------------

        // 1. Create two layered fog elements
        Rectangle fog1 = createFogRectangle(fogOpacity);
        Rectangle fog2 = createFogRectangle(fogOpacity);
        layer.getChildren().addAll(fog1, fog2);

        // 2. Animate movement (using your new speed variables)
        animateFog(fog1, fogDriftSpeed1, 0);
        animateFog(fog2, fogDriftSpeed2, 100);

        // 3. Pulse (Density drifting)
        Timeline pulse = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(fog1.opacityProperty(), fogOpacity * 0.5)),
                new KeyFrame(Duration.seconds(4), new KeyValue(fog1.opacityProperty(), fogOpacity)),
                new KeyFrame(Duration.seconds(8), new KeyValue(fog1.opacityProperty(), fogOpacity * 0.5))
        );
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.play();

        // 4. Flicker Overlay
        Rectangle flicker = new Rectangle(1280, 800, Color.BLACK);
        flicker.setOpacity(flickerBase);
        layer.getChildren().add(flicker);

        Timeline flickerTimeline = new Timeline(
                new KeyFrame(Duration.millis(500), new KeyValue(flicker.opacityProperty(), flickerBase + flickerPulse)),
                new KeyFrame(Duration.millis(1000), new KeyValue(flicker.opacityProperty(), flickerBase))
        );
        flickerTimeline.setCycleCount(Animation.INDEFINITE);
        flickerTimeline.play();
    }

    // Update helper to accept opacity
    private Rectangle createFogRectangle(double opacity) {
        Rectangle rect = new Rectangle(0, 0, 1600, 800);
        rect.setFill(Color.web("#808080", opacity));
        rect.setEffect(new javafx.scene.effect.GaussianBlur(100));
        return rect;
    }
    private void animateFog(Rectangle rect, int durationSeconds, double yOffset) {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(durationSeconds), rect);
        tt.setFromX(-400);
        tt.setToX(0);
        tt.setCycleCount(Animation.INDEFINITE);
        tt.setAutoReverse(true);
        tt.play();
    }

    private void applyFantasyEffects(Pane layer) {
        for (int i = 0; i < 20; i++) {
            Circle p = new Circle(3, Color.web("#d4af37")); // Gold glow
            p.setOpacity(0.3); p.setLayoutX(Math.random() * 1000); p.setLayoutY(Math.random() * 800);
            layer.getChildren().add(p);
            FadeTransition ft = new FadeTransition(Duration.seconds(3), p);
            ft.setFromValue(0.1); ft.setToValue(0.6); ft.setCycleCount(Animation.INDEFINITE); ft.setAutoReverse(true); ft.play();
        }
    }

    private void applySciFiEffects(Pane layer) {
        // TWEAK THESE TWO VARIABLES
        double particleSize = 9.0;   // Size of the dots
        double scanlineWidth = 5.0;  // Thickness of the scanning lines

        // 1. Horizontal Particles (Circles)
        for (int i = 0; i < 40; i++) {
            double randomRadius = 0.5 + (Math.random() * particleSize);
            Circle dataPoint = new Circle(randomRadius, Color.web("#00e5ff"));
            dataPoint.setOpacity(0.2);
            dataPoint.setLayoutX(Math.random() * 1280);
            dataPoint.setLayoutY(Math.random() * 800);
            layer.getChildren().add(dataPoint);

            TranslateTransition drift = new TranslateTransition(Duration.seconds(15 + Math.random() * 10), dataPoint);
            drift.setByX(200);
            drift.setCycleCount(Animation.INDEFINITE);
            drift.setAutoReverse(true);
            drift.play();
        }

        // 2. Vertical Scanning Bars (Rectangles)
        for (int i = 0; i < 5; i++) {
            Rectangle scanBar = new Rectangle(scanlineWidth, 400, Color.web("#00e5ff"));
            scanBar.setOpacity(0.1);
            scanBar.setLayoutX(100 + (Math.random() * 1000));
            scanBar.setLayoutY(-400);
            layer.getChildren().add(scanBar);

            TranslateTransition fall = new TranslateTransition(Duration.seconds(5 + Math.random() * 3), scanBar);
            fall.setByY(1200);
            fall.setCycleCount(Animation.INDEFINITE);
            fall.play();
        }
    }

    private void changePage(int direction, Label indicator) {
        int newPage = currentPage + direction;
        if (newPage >= 0 && newPage < pages.size()) {
            currentPage = newPage;
            textLabel.setText(pages.get(currentPage));
            indicator.setText("Page " + (currentPage + 1) + " of " + pages.size());
        }
    }

    public void applyGenreTheme(BorderPane root, String genre) {
        // Remove all old genre classes
        root.getStyleClass().removeIf(style -> style.startsWith("genre-"));

        // Only add new class if toggle is ON and we are in dark mode
        if (Main.isThemeCustomizationEnabled() && root.getStyleClass().contains("dark-theme")) {
            String cleanGenre = genre.toLowerCase().replaceAll("[^a-z]", "");
            root.getStyleClass().add("genre-" + cleanGenre);
        }
    }

    public void refreshEffects(String genre) {
        // 1. Always clear existing nodes
        effectsLayer.getChildren().clear();

        // 2. Only re-populate if enabled
        if (Main.isThemeCustomizationEnabled()) {
            addBackgroundEffects(effectsLayer, genre);
        }
    }

    public String getCurrentStoryGenre() { return (currentStory != null) ? currentStory.getGenre() : ""; }
}