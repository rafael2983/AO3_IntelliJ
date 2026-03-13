package com.rafael;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.stream.Collectors;

public class SearchView {

    // Class-level fields to hold references to inputs
    private TextField titleField;
    private TextField authorField;
    private VBox resultsContainer;

    public ScrollPane getView() {
        VBox content = new VBox(30);
        content.setPadding(new Insets(40));
        content.setAlignment(Pos.TOP_CENTER);
        content.setMaxWidth(900);

        Label header = new Label("Search Works");
        header.getStyleClass().add("section-title");
        header.setStyle("-fx-font-size: 32px; -fx-border-width: 0 0 3px 0;");

        // Search Form Container
        VBox searchForm = new VBox(25);
        searchForm.getStyleClass().add("card");
        searchForm.setPadding(new Insets(30));

        // Initialize fields here so they are ready for the search action
        titleField = new TextField();
        authorField = new TextField();

        GridPane basicGrid = new GridPane();
        basicGrid.setHgap(20);
        basicGrid.setVgap(15);

        // Add form fields
        addFormField(basicGrid, "Title:", titleField, 0);
        addFormField(basicGrid, "Author:", authorField, 1);

        // Search Button
        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().add("filter-button");
        searchBtn.setStyle("-fx-background-color: -app-accent-color; -fx-text-fill: white;");
        searchBtn.setOnAction(e -> performSearch());

        HBox btnBox = new HBox(searchBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        searchForm.getChildren().addAll(basicGrid, btnBox);

        // Results Container
        resultsContainer = new VBox(15);

        content.getChildren().addAll(header, searchForm, resultsContainer);

        ScrollPane scroll = new ScrollPane(new StackPane(content));
        scroll.setFitToWidth(true);
        scroll.setPannable(true);

        return scroll;
    }

    private void performSearch() {
        resultsContainer.getChildren().clear();
        String titleQuery = titleField.getText().toLowerCase();
        String authorQuery = authorField.getText().toLowerCase();

        // Accessing the database through the helper method
        // Note: You can expand MockDatabase to include a getAllStories() method
        // if you want to search across all fandoms at once.
        var allStories = MockDatabase.getStoriesByFandom("K-pop");

        var filtered = allStories.stream()
                .filter(s -> s.getTitle().toLowerCase().contains(titleQuery))
                .filter(s -> s.getAuthor().toLowerCase().contains(authorQuery))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            resultsContainer.getChildren().add(new Label("No works matched your search."));
        } else {
            for (Story s : filtered) {
                resultsContainer.getChildren().add(createResultCard(s));
            }
        }
    }

    private VBox createResultCard(Story s) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: #D3D3D3; -fx-border-width: 0 0 1 0;");

        Label title = new Label(s.getTitle() + " by " + s.getAuthor());
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: #990000;");

        Button readBtn = new Button("Read Story");
        readBtn.getStyleClass().add("filter-button");
        readBtn.setOnAction(e -> {
            ReaderView reader = new ReaderView();
            Main.setCenterContent(reader.getView(s, Main.getRoot()));
        });

        card.getChildren().addAll(title, new Label("Genre: " + s.getGenre()), readBtn);
        return card;
    }

    private void addFormField(GridPane grid, String labelText, TextField field, int row) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-weight: bold;");
        field.setPromptText("Enter " + labelText.toLowerCase().replace(":", ""));
        grid.add(label, 0, row);
        grid.add(field, 1, row);
    }
}