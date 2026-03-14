package com.rafael;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.List;

public class HomeView {

    public ScrollPane getView() {
        VBox mainContainer = new VBox(50);
        mainContainer.setPadding(new Insets(40, 30, 60, 30));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setMaxWidth(1150);

        // --- HERO SECTION ---
        HBox heroSection = new HBox(40);
        heroSection.setPadding(new Insets(20));
        heroSection.setAlignment(Pos.CENTER_LEFT);

        VBox heroTextContainer = new VBox(25);
        heroTextContainer.setPrefWidth(650);
        heroTextContainer.setAlignment(Pos.CENTER_LEFT);

        Label welcomeTitle = new Label("Welcome to AO3");
        // Apply inline gradient for WOW factor on the huge title
        welcomeTitle.setStyle("-fx-font-size: 48px; -fx-font-weight: 900; -fx-text-fill: linear-gradient(to bottom right, #FF416C, #FF4B2B); -fx-effect: dropshadow(gaussian, rgba(255, 65, 108, 0.2), 10, 0, 0, 5);");

        Label loremLabel = new Label("A fan-created, fan-run, nonprofit, noncommercial archive for transformative fanworks, like fanfiction, fanart, fan videos, and podfic.");
        loremLabel.setWrapText(true);
        loremLabel.setStyle("-fx-font-size: 18px; -fx-line-spacing: 8px; -fx-opacity: 0.8;");
        loremLabel.getStyleClass().add("card-desc");

        // SEARCH FIELD SETUP
        HBox searchWrapper = new HBox(15);
        searchWrapper.setAlignment(Pos.CENTER_LEFT);
        
        TextField heroSearch = new TextField();
        heroSearch.setPromptText("Search works, tags, or authors...");
        heroSearch.setPrefHeight(48);
        heroSearch.setPrefWidth(350);
        heroSearch.getStyleClass().add("search-field");

        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().add("button");
        searchBtn.setPrefHeight(48);
        searchBtn.setPrefWidth(120);

        searchWrapper.getChildren().addAll(heroSearch, searchBtn);

        // ACTION: Perform search on button click
        searchBtn.setOnAction(e -> {
            SearchView searchView = new SearchView();
            Main.setCenterContent(searchView.getView());
        });

        // ACTION: Support "Enter" key on the text field
        heroSearch.setOnAction(e -> searchBtn.fire());

        Button getStartedBtn = new Button("Explore Fandoms");
        getStartedBtn.getStyleClass().add("filter-button");
        getStartedBtn.setPrefHeight(48);
        getStartedBtn.setOnAction(e -> {
            Main.setCenterContent(new FandomPage().getView());
        });

        HBox heroButtons = new HBox(15);
        heroButtons.getChildren().addAll(getStartedBtn);

        heroTextContainer.getChildren().addAll(welcomeTitle, loremLabel, searchWrapper, heroButtons);

        // --- UPDATED IMAGE SECTION WITH FLOAT ANIMATION ---
        Image heroImage = new Image(getClass().getResourceAsStream("/filename.jpg"));
        ImageView imageView = new ImageView(heroImage);
        imageView.setFitWidth(350);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        StackPane imageContainer = new StackPane(imageView);
        imageContainer.setPrefSize(350, 300);
        imageContainer.setStyle("-fx-background-radius: 20; -fx-background-color: transparent;");
        
        // Add a beautiful glowing shadow to the hero image
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#FF416C", 0.3));
        glow.setRadius(30);
        glow.setSpread(0.1);
        imageContainer.setEffect(glow);

        // WOW Factor: Continuous Floating Animation
        TranslateTransition floatAnim = new TranslateTransition(Duration.seconds(3.5), imageContainer);
        floatAnim.setByY(-20); // float up by 20 pixels
        floatAnim.setAutoReverse(true);
        floatAnim.setCycleCount(Animation.INDEFINITE);
        floatAnim.setInterpolator(Interpolator.SPLINE(0.4, 0.1, 0.4, 0.9)); // Smooth ease-in-out
        floatAnim.play();

        heroSection.getChildren().addAll(heroTextContainer, imageContainer);

        // --- RECOMMENDED WORKS SECTION ---
        VBox recommendedSection = createStoryList("Recommended Works");

        // --- POPULAR TAGS SECTION ---
        VBox tagsSection = new VBox(20);
        Label tagsTitle = new Label("Trending Tags");
        tagsTitle.getStyleClass().add("section-title");

        FlowPane tagsContainer = new FlowPane();
        tagsContainer.setHgap(12);
        tagsContainer.setVgap(12);
        
        String[] tags = {"Angst", "Slow Burn", "Alternate Universe", "Fluff", "Enemies to Lovers", "Magic", "Coffee Shop AU"};
        for (String tag : tags) {
            Label tagChip = new Label(tag);
            tagChip.setPadding(new Insets(10, 20, 10, 20));
            // Gradient colorful tags
            tagChip.setStyle("-fx-background-color: linear-gradient(to right, #4facfe, #00f2fe); -fx-background-radius: 30; -fx-text-fill: white; -fx-font-weight: 800; -fx-font-size: 14px; -fx-effect: dropshadow(gaussian, rgba(79, 172, 254, 0.4), 10, 0, 0, 4);");
            tagsContainer.getChildren().add(tagChip);
            
            // Hover animation for tags
            tagChip.setOnMouseEntered(e -> {
                tagChip.setTranslateY(-3);
            });
            tagChip.setOnMouseExited(e -> {
                tagChip.setTranslateY(0);
            });
            tagChip.setCursor(javafx.scene.Cursor.HAND);
        }
        tagsSection.getChildren().addAll(tagsTitle, tagsContainer);

        // --- UPDATES & ANNOUNCEMENTS ---
        List<String> news = List.of(
                "Are you interested in helping keep OTW news post spaces a welcoming and safe space for engagement? Are you fluent in Brazilian Portuguese, Mandarin Chinese, Russian, or Spanish, and want to help us better reply to users all around the world? Are you a skilled organizer who enjoys working in a team? The Organization for Transformative Works is recruiting!"
        );
        VBox updatesSection = createContentSection("Updates & Announcements", news);

        mainContainer.getChildren().addAll(heroSection, recommendedSection, tagsSection, updatesSection);

        ScrollPane scrollPane = new ScrollPane();
        StackPane centeredContent = new StackPane(mainContainer);
        centeredContent.setAlignment(Pos.TOP_CENTER);
        scrollPane.setContent(centeredContent);
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

    private VBox createContentSection(String title, List<String> announcements) {
        VBox section = new VBox(20);
        Label sectionTitle = new Label(title);
        sectionTitle.getStyleClass().add("section-title");
        section.getChildren().add(sectionTitle);

        for (String announcement : announcements) {
            VBox updateCard = new VBox(5);
            updateCard.getStyleClass().add("card");

            Label textLabel = new Label(announcement);
            textLabel.setWrapText(true);
            textLabel.setMaxWidth(800);
            textLabel.getStyleClass().add("card-desc");

            updateCard.getChildren().add(textLabel);
            section.getChildren().add(updateCard);
        }
        return section;
    }

    private VBox createStoryList(String sectionTitle) {
        VBox section = new VBox(20);
        Label title = new Label(sectionTitle);
        title.getStyleClass().add("section-title");
        section.getChildren().add(title);

        for (Story s : MockDatabase.getStoriesByFandom("K-pop")) {
            VBox storyCard = new VBox(12);
            storyCard.getStyleClass().add("card");

            Label stitle = new Label(s.getTitle() + " by " + s.getAuthor());
            stitle.getStyleClass().add("card-title");

            Label sGenre = new Label("Genre: " + s.getGenre());
            sGenre.setStyle("-fx-font-weight: 800; -fx-text-fill: -app-text-color; -fx-font-size: 14px; -fx-opacity: 0.7;");

            Label sSummary = new Label(s.getSummary());
            sSummary.setWrapText(true);
            sSummary.getStyleClass().add("card-desc");

            Label sTags = new Label("Tags: " + s.getTags());
            sTags.setStyle("-fx-font-size: 13px; -fx-text-fill: -app-text-color; -fx-opacity: 0.5; -fx-font-style: italic;");

            Button readBtn = new Button("Read Story");
            readBtn.getStyleClass().add("filter-button");
            readBtn.setOnAction(e -> {
                ReaderView reader = new ReaderView();
                Main.setCenterContent(reader.getView(s, Main.getRoot()));
            });

            storyCard.getChildren().addAll(stitle, sGenre, sSummary, sTags, readBtn);
            section.getChildren().add(storyCard);
        }
        return section;
    }
}