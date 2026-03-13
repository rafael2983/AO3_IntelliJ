package com.rafael;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class BrowseView {

    public ScrollPane getView() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        content.setFillWidth(true);
        content.setAlignment(Pos.TOP_CENTER); // Center content vertically
        content.setMaxWidth(1100); // Limit width for better readability

        Label header = new Label("Browse Fandoms");
        header.getStyleClass().add("section-title");

        // Categories
        String[] categories = {"Anime & Manga", "Books & Literature", "Cartoons & Comics", "Celebrities & Real People", "Movies", "Music & Bands", "Other Media", "Theater", "TV Shows", "Video Games", "Uncategorized"};

        GridPane grid = new GridPane();
        grid.setHgap(40);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER); // Center the grid

        int columns = 3;
        for (int i = 0; i < categories.length; i++) {
            Label categoryLabel = new Label(categories[i]);
            categoryLabel.getStyleClass().add("card-title");
            categoryLabel.setStyle("-fx-font-size: 15px;");
            grid.add(categoryLabel, i % columns, i / columns);
        }

        content.getChildren().addAll(header, grid);

        ScrollPane scroll = new ScrollPane();
        StackPane centeredContent = new StackPane(content);
        centeredContent.setAlignment(Pos.TOP_CENTER);
        scroll.setContent(centeredContent);
        scroll.setFitToWidth(true);
        scroll.setPannable(true); // Enable panning for smoother feel on touch/trackpads

        return scroll;
    }
}
