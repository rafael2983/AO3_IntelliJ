package com.rafael;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import java.util.HashMap;
import java.util.Map;

public class FandomPage {

    public ScrollPane getView() {
        ScrollPane scrollPane = new ScrollPane();
        VBox content = new VBox(35);
        content.setPadding(new Insets(40));
        content.setMaxWidth(1100);
        content.setAlignment(Pos.TOP_CENTER);

        // --- Explore Section ---
        VBox exploreSection = new VBox(15);
        exploreSection.setAlignment(Pos.CENTER);
        Label exploreTitle = new Label("Explore Fandoms");
        exploreTitle.getStyleClass().add("section-title");
        exploreSection.getChildren().addAll(exploreTitle, new Label("Find your next favorite story."));

        // --- Popular Fandoms Grid ---
        GridPane fandomGrid = new GridPane();
        fandomGrid.setHgap(20);
        fandomGrid.setVgap(20);
        fandomGrid.setAlignment(Pos.CENTER);

        String[] fandoms = {"K-pop", "Marvel Cinematic Universe", "Harry Potter", "Star Wars", "Supernatural", "Doctor Who"};

        // This Map will store our Story Sections so we can find them later
        Map<String, VBox> sectionMap = new HashMap<>();

        // 1. First, build the grid
        for (int i = 0; i < fandoms.length; i++) {
            String name = fandoms[i];
            VBox card = createFandomCard(name, "Explore works in this fandom.");
            card.setCursor(javafx.scene.Cursor.HAND);

            // The Click Logic
            card.setOnMouseClicked(e -> {
                VBox target = sectionMap.get(name);
                if (target != null) {
                    double vValue = target.getBoundsInParent().getMinY() / (content.getHeight() - scrollPane.getViewportBounds().getHeight());
                    scrollPane.setVvalue(vValue);

                    // Brief highlight effect
                    target.setStyle("-fx-background-color: rgba(229, 62, 62, 0.05); -fx-padding: 10; -fx-background-radius: 5;");
                }
            });

            fandomGrid.add(card, i % 3, i / 3);
        }

        content.getChildren().addAll(exploreSection, fandomGrid);

        // 2. Now, create a story list for EACH fandom automatically
        for (String name : fandoms) {
            VBox storySection = createStoryList(name + " Works", name);
            sectionMap.put(name, storySection);
            content.getChildren().add(storySection);
        }

        // Final ScrollPane Setup
        StackPane centeredContent = new StackPane(content);
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

    // UPDATED: Now takes fandom name to fetch specific stories
    private VBox createStoryList(String sectionTitle, String fandomName) {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20, 0, 20, 0));

        Label title = new Label(sectionTitle);
        title.getStyleClass().add("section-title");
        section.getChildren().add(title);



        var stories = MockDatabase.getStoriesByFandom(fandomName);

        if (stories.isEmpty()) {
            Label noStories = new Label("No stories found for this fandom yet.");
            noStories.setStyle("-fx-text-fill: -app-text-color; -fx-opacity: 0.6; -fx-font-style: italic;");
            section.getChildren().add(noStories);
        } else {
            for (Story s : stories) {
                VBox storyCard = new VBox(8);
                storyCard.getStyleClass().add("card");

                Label stitle = new Label(s.getTitle() + " by " + s.getAuthor());
                stitle.getStyleClass().add("card-title");

                Label sGenre = new Label("Genre: " + s.getGenre());
                sGenre.setStyle("-fx-font-weight: bold; -fx-text-fill: -app-text-color; -fx-font-size: 13px; -fx-opacity: 0.8;");

                Label sSummary = new Label(s.getSummary());
                sSummary.setWrapText(true);
                sSummary.getStyleClass().add("card-desc");

                Button readBtn = new Button("Read Story");
                readBtn.getStyleClass().add("filter-button");
                readBtn.setOnAction(e -> {
                    ReaderView reader = new ReaderView();
                    Main.setCenterContent(reader.getView(s, Main.getRoot()));
                });

                // ADD EVERYTHING ONCE:
                storyCard.getChildren().addAll(stitle, sGenre, sSummary, readBtn);

                // Add the card to the section
                section.getChildren().add(storyCard);
            }
        }
        return section;
    }

    private VBox createFandomCard(String title, String desc) {
        VBox box = new VBox(8);
        box.getStyleClass().add("card");
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");
        
        Label descLabel = new Label(desc);
        descLabel.getStyleClass().add("card-desc");
        
        box.getChildren().addAll(titleLabel, descLabel);
        return box;
    }
}