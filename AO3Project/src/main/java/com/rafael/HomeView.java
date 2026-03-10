package com.rafael;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class HomeView {

    public ScrollPane getView() {
        VBox content = new VBox(35);
        content.setPadding(new Insets(40));
        content.setMaxWidth(1100);
        content.setAlignment(Pos.TOP_CENTER);

        // Explore Section
        VBox exploreSection = new VBox(15);
        exploreSection.setAlignment(Pos.CENTER); // Center the content of this section
        Label exploreTitle = new Label("Explore Fandoms");
        exploreTitle.getStyleClass().add("section-title");
        Label subtitle = new Label("Find your next favorite story among thousands of communities.");
        subtitle.getStyleClass().add("card-desc");

        HBox filters = new HBox(10);
        filters.setAlignment(Pos.CENTER); // Center the filter buttons
        Button popularBtn = new Button("Popular");
        Button upcomingBtn = new Button("Upcoming");
        Button allBtn = new Button("All");
        popularBtn.getStyleClass().addAll("filter-button", "active");
        upcomingBtn.getStyleClass().add("filter-button");
        allBtn.getStyleClass().add("filter-button");
        filters.getChildren().addAll(popularBtn, upcomingBtn, allBtn);
        exploreSection.getChildren().addAll(exploreTitle, subtitle, filters);

        // Popular Fandoms Grid
        GridPane fandomGrid = new GridPane();
        fandomGrid.setHgap(20);
        fandomGrid.setVgap(20);
        fandomGrid.setAlignment(Pos.CENTER); // Center the grid content
        String[] fandoms = {"K-pop", "Marvel Cinematic Universe", "Harry Potter", "Star Wars", "Supernatural", "Doctor Who"};
        for (int i = 0; i < fandoms.length; i++) {
            VBox card = createFandomCard(fandoms[i], "Explore works in this fandom.");
            fandomGrid.add(card, i % 3, i / 3);
        }

        // All Fandoms Table
        Label allFandomsTitle = new Label("All Fandoms");
        allFandomsTitle.getStyleClass().add("section-title");
        GridPane table = new GridPane();
        table.setHgap(40);
        table.setVgap(15);
        table.setAlignment(Pos.CENTER); // Center the table content
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                table.add(new Label("Fandom Name " + (row * 4 + col + 1)), col, row);
            }
        }

        content.getChildren().addAll(exploreSection, fandomGrid, allFandomsTitle, table);

        ScrollPane scrollPane = new ScrollPane();
        StackPane centeredContent = new StackPane(content);
        centeredContent.setAlignment(Pos.TOP_CENTER);
        scrollPane.setContent(centeredContent);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("edge-to-edge"); // Optional: for better appearance

        return scrollPane;
    }

    private VBox createFandomCard(String title, String desc) {
        VBox box = new VBox(8);
        box.getStyleClass().add("card");
        box.setAlignment(Pos.CENTER_LEFT); // Keep card content left-aligned
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");
        Label descLabel = new Label(desc);
        descLabel.getStyleClass().add("card-desc");
        box.getChildren().addAll(titleLabel, descLabel);
        return box;
    }
}
