package com.rafael;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import java.util.List;

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

        Label loremLabel = new Label("A fan-created, fan-run, nonprofit, noncommercial archive for transformative fanworks, like fanfiction, fanart, fan videos, and podfic.");
        loremLabel.setWrapText(true);
        loremLabel.getStyleClass().add("card-desc");

        // SEARCH FIELD SETUP
        TextField heroSearch = new TextField();
        heroSearch.setPromptText("Search...");
        heroSearch.setPrefHeight(40);

        HBox heroButtons = new HBox(15);
        Button searchBtn = new Button("Search");
        Button getStartedBtn = new Button("Get Started");
        searchBtn.getStyleClass().add("filter-button");
        getStartedBtn.getStyleClass().add("filter-button");
        searchBtn.setPrefWidth(120);
        getStartedBtn.setPrefWidth(120);

        // ACTION: Perform search on button click
        searchBtn.setOnAction(e -> {
            SearchView searchView = new SearchView();
            Main.setCenterContent(searchView.getView());
        });

        // ACTION: Support "Enter" key on the text field
        heroSearch.setOnAction(e -> searchBtn.fire());

        // ACTION: Get Started navigation
        getStartedBtn.setOnAction(e -> {
            // Logic to navigate to a "Start" or "Help" page, or simply scroll
            System.out.println("Get Started clicked!");
        });

        heroButtons.getChildren().addAll(searchBtn, getStartedBtn);

        heroTextContainer.getChildren().addAll(welcomeTitle, loremLabel, heroSearch, heroButtons);

        // --- UPDATED IMAGE SECTION ---
        Image heroImage = new Image(getClass().getResourceAsStream("/filename.jpg"));
        ImageView imageView = new ImageView(heroImage);
        imageView.setFitWidth(300);
        imageView.setFitHeight(250);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        StackPane imageContainer = new StackPane(imageView);
        imageContainer.setPrefSize(300, 250);
        imageContainer.setStyle("-fx-background-radius: 5; -fx-background-color: transparent;");

        heroSection.getChildren().addAll(heroTextContainer, imageContainer);

        // --- RECOMMENDED WORKS SECTION ---
        VBox recommendedSection = createStoryList("Recommended Works");

        // --- POPULAR TAGS SECTION ---
        VBox tagsSection = new VBox(15);
        Label tagsTitle = new Label("Popular Tags");
        tagsTitle.getStyleClass().add("section-title");

        HBox tagsContainer = new HBox(10);
        String[] tags = {"Love", "Kpop", "Scary", "Kpop", "Fluff", "Star Wars"};
        for (String tag : tags) {
            Label tagChip = new Label(tag);
            tagChip.setPadding(new Insets(8, 15, 8, 15));
            tagChip.setStyle("-fx-background-color: #A8C69F; -fx-background-radius: 5; -fx-text-fill: white;");
            tagsContainer.getChildren().add(tagChip);
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

        return scrollPane;
    }

    private VBox createContentSection(String title, List<String> announcements) {
        VBox section = new VBox(15);
        Label sectionTitle = new Label(title);
        sectionTitle.getStyleClass().add("section-title");
        section.getChildren().add(sectionTitle);

        for (String announcement : announcements) {
            VBox updateCard = new VBox(5);
            updateCard.setPadding(new Insets(10));
            updateCard.setStyle("-fx-border-color: #EEEEEE; -fx-border-width: 0 0 1 0;");

            Label textLabel = new Label(announcement);
            textLabel.setWrapText(true);
            textLabel.setMaxWidth(750);
            textLabel.setLineSpacing(5);
            textLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333; -fx-padding: 0 0 10 0;");

            updateCard.getChildren().add(textLabel);
            section.getChildren().add(updateCard);
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
                Main.setCenterContent(reader.getView(s, Main.getRoot()));
            });

            storyCard.getChildren().addAll(stitle, sGenre, sSummary, sTags, readBtn);
            section.getChildren().add(storyCard);
        }
        return section;
    }
}