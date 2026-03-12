package com.rafael;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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
                    target.setStyle("-fx-background-color: #fff9e6; -fx-padding: 10; -fx-background-radius: 5;");
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
            noStories.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
            section.getChildren().add(noStories);
        } else {
            for (Story s : stories) {
                VBox storyCard = new VBox(5);
                storyCard.setPadding(new Insets(15));
                storyCard.setStyle("-fx-border-color: #D3D3D3; -fx-border-width: 0 0 1 0; -fx-background-color: white;");

                Label stitle = new Label(s.getTitle() + " by " + s.getAuthor());
                stitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #990000;");

                Label sGenre = new Label("Genre: " + s.getGenre());
                sGenre.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333; -fx-font-size: 12px;");

                Button readBtn = new Button("Read Story");
                readBtn.getStyleClass().add("filter-button");

                readBtn.setOnAction(e -> {
                    ReaderView reader = new ReaderView();
                    // This now works because setCenterContent is static!
                    Main.setCenterContent(reader.getView(s));
                });

                storyCard.getChildren().addAll(stitle, new Label(s.getSummary()));
                section.getChildren().add(storyCard);
                storyCard.getChildren().add(readBtn);
                storyCard.getChildren().addAll(stitle, sGenre, new Label(s.getSummary()));
            }
        }
        return section;
    }

    private VBox createFandomCard(String title, String desc) {
        VBox box = new VBox(8);
        box.getStyleClass().add("card");
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");
        box.getChildren().addAll(titleLabel, new Label(desc));
        return box;
    }
}