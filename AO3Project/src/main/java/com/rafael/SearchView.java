package com.rafael;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
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

        // Search Form Container
        VBox searchForm = new VBox(25);
        searchForm.getStyleClass().add("card");

        // Initialize fields here so they are ready for the search action
        titleField = new TextField();
        titleField.getStyleClass().add("search-field");
        authorField = new TextField();
        authorField.getStyleClass().add("search-field");

        GridPane basicGrid = new GridPane();
        basicGrid.setHgap(20);
        basicGrid.setVgap(15);

        // Add form fields
        addFormField(basicGrid, "Title:", titleField, 0);
        addFormField(basicGrid, "Author:", authorField, 1);

        // Search Button
        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().add("button");
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
        
        // Custom smooth scroll logic
        scroll.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                double delta = event.getDeltaY();
                double height = scroll.getContent().getBoundsInLocal().getHeight();
                double viewportHeight = scroll.getViewportBounds().getHeight();
                
                if (height > viewportHeight) {
                    double scrollRange = height - viewportHeight;
                    // Cap the maximum delta to prevent huge jumps from fast trackpad flings
                    double clampedDelta = Math.max(-40, Math.min(40, delta));
                    // Reduce the multiplier for a smoother feel
                    double scrollOffset = -clampedDelta * 1.5; 

                    double newVValue = scroll.getVvalue() + (scrollOffset / scrollRange);
                    scroll.setVvalue(Math.max(0, Math.min(1, newVValue)));
                    event.consume(); // Consume event to override default behavior
                }
            }
        });

        return scroll;
    }

    private void performSearch() {
        resultsContainer.getChildren().clear();
        String titleQuery = titleField.getText().toLowerCase();
        String authorQuery = authorField.getText().toLowerCase();

        // Accessing the database through the helper method
        var allStories = MockDatabase.getStoriesByFandom("K-pop");

        var filtered = allStories.stream()
                .filter(s -> s.getTitle().toLowerCase().contains(titleQuery))
                .filter(s -> s.getAuthor().toLowerCase().contains(authorQuery))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            Label noResults = new Label("No works matched your search.");
            noResults.setStyle("-fx-text-fill: -app-text-color; -fx-opacity: 0.6; -fx-font-style: italic;");
            resultsContainer.getChildren().add(noResults);
        } else {
            for (Story s : filtered) {
                resultsContainer.getChildren().add(createResultCard(s));
            }
        }
    }

    private VBox createResultCard(Story s) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");

        Label title = new Label(s.getTitle() + " by " + s.getAuthor());
        title.getStyleClass().add("card-title");

        Label genre = new Label("Genre: " + s.getGenre());
        genre.setStyle("-fx-font-weight: bold; -fx-text-fill: -app-text-color; -fx-font-size: 13px; -fx-opacity: 0.8;");

        Button readBtn = new Button("Read Story");
        readBtn.getStyleClass().add("filter-button");
        readBtn.setOnAction(e -> {
            ReaderView reader = new ReaderView();
            Main.setCenterContent(reader.getView(s, Main.getRoot()));
        });

        card.getChildren().addAll(title, genre, readBtn);
        return card;
    }

    private void addFormField(GridPane grid, String labelText, TextField field, int row) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-weight: 600; -fx-text-fill: -app-text-color;");
        field.setPromptText("Enter " + labelText.toLowerCase().replace(":", ""));
        grid.add(label, 0, row);
        grid.add(field, 1, row);
    }
}