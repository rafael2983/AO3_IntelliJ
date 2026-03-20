package com.rafael;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.List;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ReaderView {
    private int currentPage = 0;
    private List<String> pages;
    private Story currentStory;
    private Pane effectsLayer;
    private StackPane rootStack;

    public Story getCurrentStory() {
        return currentStory;
    }

    public ScrollPane getView(Story story, BorderPane root) {
        this.currentStory = story;
        if (Main.isImmersionModeEnabled()) {
            applyGenreTheme(root, story.getGenre());
        } else {
            root.getStyleClass().removeIf(s -> s.startsWith("genre-"));
        }


        rootStack = new StackPane();
        rootStack.setUserData(this);
        effectsLayer = new Pane(); // Now assigned to the class field
        effectsLayer.prefWidthProperty().bind(rootStack.widthProperty());
        effectsLayer.prefHeightProperty().bind(rootStack.heightProperty());


        effectsLayer.setMouseTransparent(true);

        // Trigger ONLY if immersion is ON
        if (Main.isImmersionModeEnabled()) {
            addBackgroundEffects(effectsLayer, story.getGenre());
        }

        VBox readerContainer = new VBox(20);
        rootStack.setUserData(this);
        readerContainer.setPadding(new Insets(40));
        readerContainer.setAlignment(Pos.TOP_CENTER);

        // Wrap text in a card for better readability like a page
        VBox textCard = new VBox(20);
        textCard.getStyleClass().add("card");
        textCard.setMaxWidth(850);
        textCard.setAlignment(Pos.TOP_CENTER);

        Label title = new Label(story.getTitle());
        VBox textContainer = new VBox(20);
        textContainer.setMaxWidth(750);
        textContainer.setAlignment(Pos.TOP_LEFT);

        String fullText = story.getFullText();

        // Split into paragraphs
        String[] paragraphs = fullText.split("(?<=\\.)\\s+");


        for (String p : paragraphs) {

            TextFlow flow = new TextFlow();
            flow.setMaxWidth(750);

            String[] words = p.split(" ");

            for (String word : words) {
                Text t = new Text(word + " ");
                t.setUnderline(false);

                t.setStyle("-fx-fill: -app-text-color; -fx-font-size: 16px;");

                // KEY WORD DETECTION
                String w = normalize(word);
                String genre = currentStory.getGenre().toLowerCase();
                boolean isKeyWord = false;
                String color = "#ff6b9a"; // default

                //  ROMANCE
                if (genre.contains("romance")) {
                    if (w.contains("heartbeat") || w.contains("pulse") ||
                            w.contains("soft") || w.contains("smile") ||
                            w.contains("warm") || w.contains("together") ||
                            w.contains("quiet") || w.contains("night") ||
                            w.contains("light")) {

                        isKeyWord = true;
                        color = "#ff6b9a";
                    }
                }

                //  HORROR
                else if (genre.contains("horror")) {
                    if (w.contains("dark") || w.contains("shadow") ||
                            w.contains("shadows") ||
                            w.contains("blood") || w.contains("red") || w.contains("cold") ||
                            w.contains("whisper") || w.contains("whispered") ||
                            w.contains("scream") || w.contains("fear") ||
                            w.contains("alone") || w.contains("watching") ||
                            w.contains("staring") || w.contains("behind") ||
                            w.contains("reflection") || w.contains("mirror") ||
                            w.contains("figure") || w.contains("presence") ||
                            w.contains("voice") || w.contains("breath") ||
                            w.contains("silence") || w.contains("still") ||
                            w.contains("creak") || w.contains("footsteps") || w.contains("creaked") ||
                            w.contains("closer") || w.contains("moved")) {

                        isKeyWord = true;
                        color = "#ff4d4d";
                    }

                }

                // FANTASY
                else if (genre.contains("fantasy")) {
                    if (w.contains("magic") || w.contains("light") ||
                            w.contains("glow") || w.contains("star") ||
                            w.contains("dream") || w.contains("wind")) {

                        isKeyWord = true;
                        color = "#c084fc";
                    }
                }
                // SCI-FI
                    else if (genre.contains("sci")) {

                        if (w.contains("signal") || w.contains("code") || w.contains("data") ||
                                w.contains("blade") || w.contains("light") || w.contains("glow") ||
                                w.contains("force") || w.contains("power") ||
                                w.contains("wind") || w.contains("sand") || w.contains("dust") ||
                                w.contains("dark") || w.contains("shadow")) {

                            isKeyWord = true;
                            color = "#38B2AC"; // neon teal
                        }
                    }


                    // ACTION
                else if (genre.contains("action")) {
                    if (w.contains("run") || w.contains("hit") ||
                            w.contains("crash") || w.contains("fight") ||
                            w.contains("explode") || w.contains("impact")) {

                        isKeyWord = true;
                        color = "#f59e0b";
                    }
                }

                if (isKeyWord && Main.isImmersionModeEnabled()) {

                    t.setUnderline(true);

                    final String finalColor = color;

                    t.setStyle("-fx-fill: " + finalColor + "; -fx-font-weight: bold;");

                    t.setOnMouseEntered(e -> {
                        t.setStyle("-fx-fill: white; -fx-font-weight: bold;");
                    });

                    t.setOnMouseExited(e -> {
                        t.setStyle("-fx-fill: " + finalColor + "; -fx-font-weight: bold;");
                    });

                    t.setOnMouseClicked(e -> {
                        if (!Main.isImmersionModeEnabled()) return;

                        // SPECIFIC EFFECTS FIRST
                        if (w.contains("heartbeat") || w.contains("pulse")) {
                            triggerHeartbeatEffect();
                        }
                        else if (w.contains("faded") || w.contains("quiet")) {
                            triggerFadeEffect();
                        }
                        else if (w.contains("light") || w.contains("stage")) {
                            triggerLightBurstEffect();
                        }
                        else if (w.contains("together") || w.contains("warm") || w.contains("smile")) {
                            triggerSoftLoveEffect();
                        }
                        else if (w.contains("night") || w.contains("tonight") || w.contains("nights")) {
                            triggerNightEffect();
                        }

                        // 👁HORROR-SPECIFIC EFFECTS

                        else if (w.contains("shadow") || w.contains("figure")) {
                            triggerShadowFigure(); // dark presence
                        }
                        else if (w.contains("mirror") || w.contains("reflection")) {
                            triggerMirrorDistortion(); // NEW
                        }
                        else if (w.contains("voice") || w.contains("voices") || w.contains("breath")) {
                            triggerWhisperEffect(); // glitch flicker
                        }
                        else if (w.contains("creak") || w.contains("creaked") || w.contains("appeared") ||  w.contains("moved") || w.contains("footsteps")) {
                            triggerFootstepEffect(); // NEW
                        }
                        else if (w.contains("staring") || w.contains("watching")) {
                            triggerWatcherEffect(); // NEW
                        }
                        else if (w.contains("blood") || w.contains("red")) {
                            triggerBloodFlash();
                        }

                        //  SCI-FI EFFECTS
                        else if (genre.contains("sci")) {


                            if (w.contains("signal") || w.contains("code") || w.contains("data")) {
                                triggerDataStreamEffect();
                            }


                            else if (w.contains("light") || w.contains("glow") || w.contains("blade")) {
                                triggerEnergyPulseEffect();
                            }


                            else if (w.contains("wind") || w.contains("sand") || w.contains("dust")) {
                                triggerFloatingCloudsEffect();
                            }


                            else if (w.contains("force") || w.contains("power")) {
                                triggerForceWaveEffect();
                            }


                            else if (w.contains("dark") || w.contains("shadow")) {
                                triggerVoidGlitchEffect();
                            }


                            else {
                                triggerSciFiClickEffect();
                            }
                        }


                        //  FALLBACK (GENRE)
                        else {
                            if (genre.contains("romance")) {
                                triggerRomanceEffect();
                            }
                            else if (genre.contains("horror")) {
                                triggerHorrorClickEffect();
                            }
                            else if (genre.contains("fantasy")) {
                                triggerFantasyClickEffect();
                            }
                            else if (genre.contains("sci")) {
                                triggerSciFiClickEffect();
                            }
                            else if (genre.contains("action")) {
                                triggerActionClickEffect();
                            }
                        }
                    });
                }

                flow.getChildren().add(t);
            }

            textContainer.getChildren().add(flow);
        }

        title.getStyleClass().add("section-title");

        textCard.getChildren().addAll(title, textContainer);
        readerContainer.getChildren().add(textCard);

        rootStack.getChildren().addAll(effectsLayer, readerContainer);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(rootStack);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("edge-to-edge");

        // Custom smooth scroll logic
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                double delta = event.getDeltaY();
                double height = scrollPane.getContent().getBoundsInLocal().getHeight();
                double viewportHeight = scrollPane.getViewportBounds().getHeight();

                if (height > viewportHeight) {
                    double scrollRange = height - viewportHeight;
                    // Cap the maximum delta to prevent huge jumps from fast trackpad flings
                    double clampedDelta = Math.max(-40, Math.min(40, delta));
                    // Reduce the multiplier for a smoother feel
                    double scrollOffset = -clampedDelta * 1.5;

                    double newVValue = scrollPane.getVvalue() + (scrollOffset / scrollRange);
                    scrollPane.setVvalue(Math.max(0, Math.min(1, newVValue)));
                    event.consume(); // Consume event to override default behavior
                }
            }
        });

        return scrollPane;
    }

    private String normalize(String word) {

        word = word.toLowerCase();

        // remove punctuation
        word = word.replaceAll("[^a-z]", "");

        // basic stemming
        if (word.endsWith("ing") && word.length() > 4) {
            word = word.substring(0, word.length() - 3);
        }
        else if (word.endsWith("ed") && word.length() > 3) {
            word = word.substring(0, word.length() - 2);
        }
        else if (word.endsWith("s") && word.length() > 3) {
            word = word.substring(0, word.length() - 1);
        }

        return word;
    }



    private void addBackgroundEffects(Pane layer, String genre) {
        layer.getChildren().clear();
        if (!Main.isImmersionModeEnabled()) return;

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
            heart.setStyle("-fx-text-fill: -app-accent-color; -fx-font-size: 20px; -fx-opacity: 0.2;");
            double width = effectsLayer.getWidth() > 0 ? effectsLayer.getWidth() : 1200;
            double height = effectsLayer.getHeight() > 0 ? effectsLayer.getHeight() : 800;

            heart.setLayoutX(Math.random() * width);
            heart.setLayoutY(Math.random() * height);

            layer.getChildren().add(heart);
            TranslateTransition tt = new TranslateTransition(Duration.seconds(8 + Math.random() * 4), heart);
            tt.setByY(-100); tt.setCycleCount(Animation.INDEFINITE); tt.setAutoReverse(true); tt.play();
        }
    }

    private void applyHorrorEffects(Pane layer) {
        // --- TUNING AREA ---
        double fogOpacity = 0.3;     // How visible the fog is (0.0 to 1.0)
        double flickerBase = 0.05;   // Base darkness of flicker
        double flickerPulse = 0.15;  // How intense the flicker surge is
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

    private void triggerRomanceEffect() {
        if (!Main.isImmersionModeEnabled()) return;

        for (int i = 0; i < 12; i++) {
            Label heart = new Label("❤");
            heart.setStyle("-fx-text-fill: #ff6b9a; -fx-font-size: 18px;");

            double width = effectsLayer.getWidth() > 0 ? effectsLayer.getWidth() : 1200;
            double height = effectsLayer.getHeight() > 0 ? effectsLayer.getHeight() : 800;

            heart.setLayoutX(Math.random() * width);
            heart.setLayoutY(Math.random() * height);


            effectsLayer.getChildren().add(heart);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(1.2), heart);
            tt.setByY(-120);

            FadeTransition ft = new FadeTransition(Duration.seconds(1.2), heart);
            ft.setFromValue(1);
            ft.setToValue(0);

            ParallelTransition pt = new ParallelTransition(tt, ft);
            pt.setOnFinished(e -> effectsLayer.getChildren().remove(heart));
            pt.play();
        }
    }

    private void triggerHeartbeatEffect() {
        if (!Main.isImmersionModeEnabled()) return;

        ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(0.15), rootStack);
        scaleUp.setToX(1.03);
        scaleUp.setToY(1.03);

        ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(0.2), rootStack);
        scaleDown.setToX(1);
        scaleDown.setToY(1);

        SequentialTransition pulse = new SequentialTransition(scaleUp, scaleDown);
        pulse.setCycleCount(2);
        pulse.play();

        // FLASH OVERLAY
        Rectangle flash = new Rectangle();
        flash.setFill(Color.web("#ff4d6d"));
        flash.widthProperty().bind(effectsLayer.widthProperty());
        flash.heightProperty().bind(effectsLayer.heightProperty());
        flash.setOpacity(0);

        effectsLayer.getChildren().add(flash);

        FadeTransition ft = new FadeTransition(Duration.seconds(0.2), flash);
        ft.setToValue(0.4);

        FadeTransition ftOut = new FadeTransition(Duration.seconds(0.4), flash);
        ftOut.setToValue(0);

        SequentialTransition seq = new SequentialTransition(ft, ftOut);
        seq.setOnFinished(e -> effectsLayer.getChildren().remove(flash));
        seq.play();
    }


    private void triggerFadeEffect() {
        if (!Main.isImmersionModeEnabled()) return;

        Rectangle darkness = new Rectangle();
        darkness.setFill(Color.BLACK);

        darkness.widthProperty().bind(effectsLayer.widthProperty());
        darkness.heightProperty().bind(effectsLayer.heightProperty());

        darkness.setOpacity(0);
        effectsLayer.getChildren().add(darkness);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), darkness);
        fadeIn.setToValue(0.6);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), darkness);
        fadeOut.setToValue(0);

        SequentialTransition seq = new SequentialTransition(fadeIn, fadeOut);
        seq.setOnFinished(e -> effectsLayer.getChildren().remove(darkness));
        seq.play();
    }

    private void triggerLightBurstEffect() {
        if (!Main.isImmersionModeEnabled()) return;

        double width = effectsLayer.getWidth() > 0 ? effectsLayer.getWidth() : 1200;
        double height = effectsLayer.getHeight() > 0 ? effectsLayer.getHeight() : 800;

        for (int i = 0; i < 15; i++) {

            Circle light = new Circle(40 + Math.random() * 80);
            light.setFill(Color.web("#ffb3c6", 0.15)); // soft pink glow

            light.setLayoutX(Math.random() * width);
            light.setLayoutY(Math.random() * height);

            // blur it for softness
            light.setEffect(new javafx.scene.effect.GaussianBlur(30));

            effectsLayer.getChildren().add(light);

            // slow float
            TranslateTransition drift = new TranslateTransition(Duration.seconds(4 + Math.random() * 3), light);
            drift.setByY(-50 + Math.random() * 100);
            drift.setByX(-30 + Math.random() * 60);

            // fade out slowly
            FadeTransition fade = new FadeTransition(Duration.seconds(4), light);
            fade.setToValue(0);

            ParallelTransition pt = new ParallelTransition(drift, fade);
            pt.setOnFinished(e -> effectsLayer.getChildren().remove(light));
            pt.play();
        }
    }


    private void triggerNightEffect() {
        if (!Main.isImmersionModeEnabled()) return;

        // DARK AMBIENT OVERLAY
        Rectangle night = new Rectangle();
        night.setFill(Color.web("#0b0f1a")); // deep blue-black

        night.widthProperty().bind(effectsLayer.widthProperty());
        night.heightProperty().bind(effectsLayer.heightProperty());

        night.setOpacity(0);
        effectsLayer.getChildren().add(night);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), night);
        fadeIn.setToValue(0.5);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), night);
        fadeOut.setToValue(0);

        // FLOATING LIGHT PARTICLES
        for (int i = 0; i < 20; i++) {
            Circle star = new Circle(2, Color.web("#ffc2d1")); // soft pink glow
            star.setOpacity(0.6);

            double width = effectsLayer.getWidth() > 0 ? effectsLayer.getWidth() : 1200;
            double height = effectsLayer.getHeight() > 0 ? effectsLayer.getHeight() : 800;

            star.setLayoutX(Math.random() * width);
            star.setLayoutY(Math.random() * height);


            effectsLayer.getChildren().add(star);

            TranslateTransition drift = new TranslateTransition(Duration.seconds(4 + Math.random() * 3), star);
            drift.setByY(-100);

            FadeTransition starFade = new FadeTransition(Duration.seconds(4), star);
            starFade.setToValue(0);

            ParallelTransition pt = new ParallelTransition(drift, starFade);
            pt.setOnFinished(e -> effectsLayer.getChildren().remove(star));
            pt.play();
        }

        SequentialTransition seq = new SequentialTransition(fadeIn, fadeOut);
        seq.setOnFinished(e -> effectsLayer.getChildren().remove(night));
        seq.play();
    }



    private void triggerSoftLoveEffect() {
        if (!Main.isImmersionModeEnabled()) return;

        Circle glow = new Circle(200);
        glow.setFill(Color.web("#ff6b9a", 0.3));

        glow.setLayoutX(effectsLayer.getWidth() / 2);
        glow.setLayoutY(effectsLayer.getHeight() / 2);

        effectsLayer.getChildren().add(glow);

        ScaleTransition scale = new ScaleTransition(Duration.seconds(1.5), glow);
        scale.setToX(3);
        scale.setToY(3);

        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), glow);
        fade.setToValue(0);

        ParallelTransition pt = new ParallelTransition(scale, fade);
        pt.setOnFinished(e -> effectsLayer.getChildren().remove(glow));
        pt.play();
    }

    private void triggerShadowFigure() {
        if (!Main.isImmersionModeEnabled()) return;

        Rectangle shadow = new Rectangle();
        shadow.setFill(Color.BLACK);

        shadow.widthProperty().bind(effectsLayer.widthProperty());
        shadow.heightProperty().bind(effectsLayer.heightProperty());

        shadow.setOpacity(0);
        effectsLayer.getChildren().add(shadow);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), shadow);
        fadeIn.setToValue(0.4);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), shadow);
        fadeOut.setToValue(0);

        SequentialTransition seq = new SequentialTransition(fadeIn, fadeOut);
        seq.setOnFinished(e -> effectsLayer.getChildren().remove(shadow));
        seq.play();
    }

    private void triggerWhisperEffect() {
        if (!Main.isImmersionModeEnabled()) return;

        FadeTransition flicker = new FadeTransition(Duration.millis(100), rootStack);
        flicker.setFromValue(1);
        flicker.setToValue(0.7);
        flicker.setCycleCount(6);
        flicker.setAutoReverse(true);
        flicker.play();
    }

    private void triggerMirrorDistortion() {
        if (!Main.isImmersionModeEnabled()) return;

        ScaleTransition stretch = new ScaleTransition(Duration.seconds(0.2), rootStack);
        stretch.setToX(1.05);

        ScaleTransition reset = new ScaleTransition(Duration.seconds(0.3), rootStack);
        reset.setToX(1);

        SequentialTransition seq = new SequentialTransition(stretch, reset);
        seq.play();
    }

    private void triggerFootstepEffect() {
        if (!Main.isImmersionModeEnabled()) return;

        TranslateTransition shake = new TranslateTransition(Duration.millis(60), rootStack);
        shake.setByX(8);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.play();
    }

    private void triggerWatcherEffect() {
        if (!Main.isImmersionModeEnabled()) return;

        Circle eye = new Circle(80, Color.BLACK);
        eye.setOpacity(0.6);

        eye.setLayoutX(effectsLayer.getWidth() / 2);
        eye.setLayoutY(effectsLayer.getHeight() / 2);

        effectsLayer.getChildren().add(eye);

        FadeTransition fade = new FadeTransition(Duration.seconds(1), eye);
        fade.setToValue(0);

        fade.setOnFinished(e -> effectsLayer.getChildren().remove(eye));
        fade.play();
    }


    private void triggerBloodFlash() {
        if (!Main.isImmersionModeEnabled()) return;

        Rectangle flash = new Rectangle();
        flash.setFill(Color.web("#8b0000"));

        flash.widthProperty().bind(effectsLayer.widthProperty());
        flash.heightProperty().bind(effectsLayer.heightProperty());

        flash.setOpacity(0);
        effectsLayer.getChildren().add(flash);

        FadeTransition ft = new FadeTransition(Duration.seconds(0.2), flash);
        ft.setToValue(0.6);

        FadeTransition out = new FadeTransition(Duration.seconds(0.5), flash);
        out.setToValue(0);

        SequentialTransition seq = new SequentialTransition(ft, out);
        seq.setOnFinished(e -> effectsLayer.getChildren().remove(flash));
        seq.play();
    }

    private void triggerFloatingCloudsEffect() {
        if (!Main.isImmersionModeEnabled()) return;

        double width = effectsLayer.getWidth() > 0 ? effectsLayer.getWidth() : 1200;
        double height = effectsLayer.getHeight() > 0 ? effectsLayer.getHeight() : 800;

        for (int i = 0; i < 12; i++) {

            Circle cloud = new Circle(80 + Math.random() * 120);
            cloud.setFill(Color.web("#38B2AC", 0.08)); // soft teal fog

            cloud.setEffect(new javafx.scene.effect.GaussianBlur(60));

            cloud.setLayoutX(Math.random() * width);
            cloud.setLayoutY(Math.random() * height);

            effectsLayer.getChildren().add(cloud);

            TranslateTransition drift = new TranslateTransition(Duration.seconds(8 + Math.random() * 5), cloud);
            drift.setByX(-100 + Math.random() * 200);
            drift.setByY(-50 + Math.random() * 100);

            FadeTransition fade = new FadeTransition(Duration.seconds(8), cloud);
            fade.setToValue(0);

            ParallelTransition pt = new ParallelTransition(drift, fade);
            pt.setOnFinished(e -> effectsLayer.getChildren().remove(cloud));
            pt.play();
        }
    }

    private void triggerEnergyPulseEffect() {
        if (!Main.isImmersionModeEnabled()) return;

        Rectangle pulse = new Rectangle();
        pulse.setFill(Color.web("#38B2AC"));

        pulse.widthProperty().bind(effectsLayer.widthProperty());
        pulse.heightProperty().bind(effectsLayer.heightProperty());

        pulse.setOpacity(0);
        effectsLayer.getChildren().add(pulse);

        FadeTransition flash = new FadeTransition(Duration.seconds(0.2), pulse);
        flash.setToValue(0.4);

        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), pulse);
        fade.setToValue(0);

        SequentialTransition seq = new SequentialTransition(flash, fade);
        seq.setOnFinished(e -> effectsLayer.getChildren().remove(pulse));
        seq.play();
    }

    private void triggerDataStreamEffect() {
        if (!Main.isImmersionModeEnabled()) return;

        double width = effectsLayer.getWidth() > 0 ? effectsLayer.getWidth() : 1200;

        for (int i = 0; i < 15; i++) {

            Rectangle line = new Rectangle(2, 40, Color.web("#38B2AC"));
            line.setOpacity(0.3);

            line.setLayoutX(Math.random() * width);
            line.setLayoutY(-50);

            effectsLayer.getChildren().add(line);

            TranslateTransition fall = new TranslateTransition(Duration.seconds(2), line);
            fall.setByY(900);

            FadeTransition fade = new FadeTransition(Duration.seconds(2), line);
            fade.setToValue(0);

            ParallelTransition pt = new ParallelTransition(fall, fade);
            pt.setOnFinished(e -> effectsLayer.getChildren().remove(line));
            pt.play();
        }
    }

    private void triggerForceWaveEffect() {
        if (!Main.isImmersionModeEnabled()) return;

        Circle wave = new Circle(50);
        wave.setFill(Color.web("#38B2AC", 0.3));

        wave.setLayoutX(effectsLayer.getWidth() / 2);
        wave.setLayoutY(effectsLayer.getHeight() / 2);

        effectsLayer.getChildren().add(wave);

        ScaleTransition expand = new ScaleTransition(Duration.seconds(1), wave);
        expand.setToX(10);
        expand.setToY(10);

        FadeTransition fade = new FadeTransition(Duration.seconds(1), wave);
        fade.setToValue(0);

        ParallelTransition pt = new ParallelTransition(expand, fade);
        pt.setOnFinished(e -> effectsLayer.getChildren().remove(wave));
        pt.play();
    }

    private void triggerVoidGlitchEffect() {
        if (!Main.isImmersionModeEnabled()) return;

        Rectangle glitch = new Rectangle();
        glitch.setFill(Color.BLACK);

        glitch.widthProperty().bind(effectsLayer.widthProperty());
        glitch.heightProperty().bind(effectsLayer.heightProperty());

        glitch.setOpacity(0);
        effectsLayer.getChildren().add(glitch);

        Timeline flicker = new Timeline(
                new KeyFrame(Duration.millis(100), new KeyValue(glitch.opacityProperty(), 0.5)),
                new KeyFrame(Duration.millis(200), new KeyValue(glitch.opacityProperty(), 0))
        );

        flicker.setCycleCount(4);
        flicker.setOnFinished(e -> effectsLayer.getChildren().remove(glitch));
        flicker.play();
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
            Circle p = new Circle(3, Color.web("#D6BCFA")); // Soft purple/gold
            p.setOpacity(0.2); p.setLayoutX(Math.random() * 1000); p.setLayoutY(Math.random() * 800);
            layer.getChildren().add(p);
            FadeTransition ft = new FadeTransition(Duration.seconds(3), p);
            ft.setFromValue(0.1); ft.setToValue(0.5); ft.setCycleCount(Animation.INDEFINITE); ft.setAutoReverse(true); ft.play();
        }
    }

    private void applySciFiEffects(Pane layer) {
        // TWEAK THESE TWO VARIABLES
        double particleSize = 9.0;   // Size of the dots
        double scanlineWidth = 5.0;  // Thickness of the scanning lines

        // 1. Horizontal Particles (Circles)
        for (int i = 0; i < 40; i++) {
            double randomRadius = 0.5 + (Math.random() * particleSize);
            Circle dataPoint = new Circle(randomRadius, Color.web("#38B2AC"));
            dataPoint.setOpacity(0.15);
            double width = effectsLayer.getWidth() > 0 ? effectsLayer.getWidth() : 1200;
            double height = effectsLayer.getHeight() > 0 ? effectsLayer.getHeight() : 800;

            dataPoint.setLayoutX(Math.random() * width);
            dataPoint.setLayoutY(Math.random() * height);

            layer.getChildren().add(dataPoint);

            TranslateTransition drift = new TranslateTransition(Duration.seconds(15 + Math.random() * 10), dataPoint);
            drift.setByX(200);
            drift.setCycleCount(Animation.INDEFINITE);
            drift.setAutoReverse(true);
            drift.play();
        }

        // 2. Vertical Scanning Bars (Rectangles)
        for (int i = 0; i < 5; i++) {
            Rectangle scanBar = new Rectangle(scanlineWidth, 400, Color.web("#38B2AC"));
            scanBar.setOpacity(0.05);
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
            indicator.setText("Page " + (currentPage + 1) + " of " + pages.size());
        }
    }

    public void applyGenreTheme(BorderPane root, String genre) {

        // remove old genre classes ALWAYS
        root.getStyleClass().removeIf(style -> style.startsWith("genre-"));

        //  if immersion OFF → DO NOTHING
        if (!Main.isImmersionModeEnabled()) {
            return;
        }

        // apply genre regardless of dark mode
        String cleanGenre = genre.toLowerCase().replaceAll("[^a-z]", "");
        root.getStyleClass().add("genre-" + cleanGenre);
    }

    public void refreshEffects(String genre) {

        // ALWAYS CLEAR EVERYTHING
        effectsLayer.getChildren().clear();

        // STOP if immersion OFF
        if (!Main.isImmersionModeEnabled()) return;

        // re-add if ON
        addBackgroundEffects(effectsLayer, genre);
    }


    private void triggerHorrorClickEffect() {
        if (!Main.isImmersionModeEnabled()) return;

        Rectangle dark = new Rectangle();
        dark.setFill(Color.BLACK);

        dark.widthProperty().bind(effectsLayer.widthProperty());
        dark.heightProperty().bind(effectsLayer.heightProperty());

        dark.setOpacity(0);
        effectsLayer.getChildren().add(dark);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), dark);
        fadeIn.setToValue(0.7);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), dark);
        fadeOut.setToValue(0);

        SequentialTransition seq = new SequentialTransition(fadeIn, fadeOut);
        seq.setOnFinished(e -> effectsLayer.getChildren().remove(dark));
        seq.play();

        // subtle shake
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), rootStack);
        shake.setByX(6);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.play();
    }

    private void triggerFantasyClickEffect() {
        if (!Main.isImmersionModeEnabled()) return;

        double width = effectsLayer.getWidth() > 0 ? effectsLayer.getWidth() : 1200;
        double height = effectsLayer.getHeight() > 0 ? effectsLayer.getHeight() : 800;

        for (int i = 0; i < 20; i++) {
            Circle spark = new Circle(3 + Math.random() * 4, Color.web("#d6bcfa", 0.6));

            spark.setLayoutX(Math.random() * width);
            spark.setLayoutY(Math.random() * height);

            effectsLayer.getChildren().add(spark);

            FadeTransition fade = new FadeTransition(Duration.seconds(2), spark);
            fade.setToValue(0);

            TranslateTransition floatUp = new TranslateTransition(Duration.seconds(2), spark);
            floatUp.setByY(-100);

            ParallelTransition pt = new ParallelTransition(fade, floatUp);
            pt.setOnFinished(e -> effectsLayer.getChildren().remove(spark));
            pt.play();
        }
    }

    private void triggerSciFiClickEffect() {
        if (!Main.isImmersionModeEnabled()) return;

        Rectangle flash = new Rectangle();
        flash.setFill(Color.web("#38B2AC"));

        flash.widthProperty().bind(effectsLayer.widthProperty());
        flash.heightProperty().bind(effectsLayer.heightProperty());

        flash.setOpacity(0);
        effectsLayer.getChildren().add(flash);

        FadeTransition ft = new FadeTransition(Duration.seconds(0.15), flash);
        ft.setToValue(0.5);

        FadeTransition out = new FadeTransition(Duration.seconds(0.3), flash);
        out.setToValue(0);

        SequentialTransition seq = new SequentialTransition(ft, out);
        seq.setOnFinished(e -> effectsLayer.getChildren().remove(flash));
        seq.play();
    }

    private void triggerActionClickEffect() {
        if (!Main.isImmersionModeEnabled()) return;

        Rectangle flash = new Rectangle();
        flash.setFill(Color.ORANGE);

        flash.widthProperty().bind(effectsLayer.widthProperty());
        flash.heightProperty().bind(effectsLayer.heightProperty());

        flash.setOpacity(0);
        effectsLayer.getChildren().add(flash);

        FadeTransition ft = new FadeTransition(Duration.seconds(0.1), flash);
        ft.setToValue(0.8);

        FadeTransition out = new FadeTransition(Duration.seconds(0.2), flash);
        out.setToValue(0);

        SequentialTransition seq = new SequentialTransition(ft, out);
        seq.setOnFinished(e -> effectsLayer.getChildren().remove(flash));
        seq.play();

        TranslateTransition shake = new TranslateTransition(Duration.millis(40), rootStack);
        shake.setByX(12);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.play();
    }


    public String getCurrentStoryGenre() { return (currentStory != null) ? currentStory.getGenre() : ""; }
}