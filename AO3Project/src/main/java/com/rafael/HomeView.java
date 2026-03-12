package com.rafael;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HomeView {

    public ScrollPane getView() {
        VBox mainContainer = new VBox(40);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setMaxWidth(1100);

        // --- HERO SECTION ---
        HBox heroSection = new HBox(30);
        heroSection.setPadding(new Insets(20));
        heroSection.setAlignment(Pos.CENTER_LEFT);

        VBox heroTextContainer = new VBox(15);
        heroTextContainer.setPrefWidth(600);

        Label welcomeTitle = new Label("Welcome to AO3");
        welcomeTitle.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;");

        Label loremLabel = new Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        loremLabel.setWrapText(true);
        loremLabel.getStyleClass().add("card-desc");

        TextField heroSearch = new TextField();
        heroSearch.setPromptText("Lorem ipsum");
        heroSearch.setPrefHeight(40);

        HBox heroButtons = new HBox(15);
        Button searchBtn = new Button("Search");
        Button getStartedBtn = new Button("Get Started");
        searchBtn.getStyleClass().add("filter-button"); // Using existing theme button style
        getStartedBtn.getStyleClass().add("filter-button");
        searchBtn.setPrefWidth(120);
        getStartedBtn.setPrefWidth(120);
        heroButtons.getChildren().addAll(searchBtn, getStartedBtn);

        heroTextContainer.getChildren().addAll(welcomeTitle, loremLabel, heroSearch, heroButtons);

        // Placeholder for the large image/box on the right of hero
        Region imagePlaceholder = new Region();
        imagePlaceholder.setPrefSize(300, 250);
        imagePlaceholder.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 5;");

        heroSection.getChildren().addAll(heroTextContainer, imagePlaceholder);

        // --- RECOMMENDED WORKS SECTION ---
        //VBox recommendedSection = createContentSection("Recommended Works", 2);
        VBox recommendedSection = createStoryList("Recommended Works");

        // --- POPULAR TAGS SECTION ---
        VBox tagsSection = new VBox(15);
        Label tagsTitle = new Label("Popular Tags");
        tagsTitle.getStyleClass().add("section-title");

        HBox tagsContainer = new HBox(10);
        String[] tags = {"Tag One", "Tag Two", "Tag Three", "Tag Four", "Tag Five", "Tag Six"};
        for (String tag : tags) {
            Label tagChip = new Label(tag);
            tagChip.setPadding(new Insets(8, 15, 8, 15));
            // Greenish tint from your prototype
            tagChip.setStyle("-fx-background-color: #A8C69F; -fx-background-radius: 5; -fx-text-fill: white;");
            tagsContainer.getChildren().add(tagChip);
        }
        tagsSection.getChildren().addAll(tagsTitle, tagsContainer);

        // --- UPDATES & ANNOUNCEMENTS ---
        VBox updatesSection = createContentSection("Updates & Announcements", 2);

        // Add all sections to main container
        mainContainer.getChildren().addAll(heroSection, recommendedSection, tagsSection, updatesSection);


        // ScrollPane Setup
        ScrollPane scrollPane = new ScrollPane();
        StackPane centeredContent = new StackPane(mainContainer);
        centeredContent.setAlignment(Pos.TOP_CENTER);
        scrollPane.setContent(centeredContent);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("edge-to-edge");

        return scrollPane;
    }

    private VBox createContentSection(String title, int count) {
        VBox section = new VBox(15);
        Label sectionTitle = new Label(title);
        sectionTitle.getStyleClass().add("section-title");
        section.getChildren().add(sectionTitle);

        for (int i = 0; i < count; i++) {
            VBox workPlaceholder = new VBox(10);
            workPlaceholder.setPadding(new Insets(15));
            workPlaceholder.setStyle("-fx-border-color: #EEEEEE; -fx-border-width: 0 0 1 0;");

            // Simulating the "skeleton" lines from prototype
            Region line1 = new Region(); line1.setPrefHeight(15); line1.setMaxWidth(800);
            line1.setStyle("-fx-background-color: #D3D3D3;");

            Region line2 = new Region(); line2.setPrefHeight(12); line2.setMaxWidth(600);
            line2.setStyle("-fx-background-color: #E0E0E0;");

            workPlaceholder.getChildren().addAll(line1, line2);
            section.getChildren().add(workPlaceholder);
        }
        return section;
    }

    private VBox createStoryList(String sectionTitle) {
        VBox section = new VBox(15);
        Label title = new Label(sectionTitle);
        title.getStyleClass().add("section-title");
        section.getChildren().add(title);

        for (Story s : MockDatabase.getStoriesByFandom("K-pop")) {
            VBox storyCard = new VBox(5);
            storyCard.setPadding(new Insets(10));
            storyCard.setStyle("-fx-border-color: #D3D3D3; -fx-border-width: 0 0 1 0;");

            Label stitle = new Label(s.getTitle() + " by " + s.getAuthor());
            stitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #990000;");

            Label sGenre = new Label("Genre: " + s.getGenre());
            sGenre.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333; -fx-font-size: 12px;");

            Label sSummary = new Label(s.getSummary());
            sSummary.setWrapText(true);

            Label sTags = new Label("Tags: " + s.getTags());
            sTags.setStyle("-fx-font-size: 11px; -fx-text-fill: #666666;");

            Button readBtn = new Button("Read Story");
            readBtn.getStyleClass().add("filter-button");
            readBtn.setOnAction(e -> {
                ReaderView reader = new ReaderView();
                Main.setCenterContent(reader.getView(s));
            });

            // ADD EVERYTHING ONCE:
            storyCard.getChildren().addAll(stitle, sGenre, sSummary, sTags, readBtn);

            // Add the completed card to the section
            section.getChildren().add(storyCard);
        }
        return section;
    }
}