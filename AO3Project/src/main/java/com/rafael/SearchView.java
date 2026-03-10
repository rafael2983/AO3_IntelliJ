package com.rafael;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;

public class SearchView {

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

        // Section 1: Basic Info
        Label basicInfoLabel = new Label("Work Information");
        basicInfoLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: -app-accent-color;");
        
        GridPane basicGrid = new GridPane();
        basicGrid.setHgap(20);
        basicGrid.setVgap(15);
        
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);
        basicGrid.getColumnConstraints().addAll(col1, col2);

        addFormField(basicGrid, "Any Field:", "Title, author, summary...", 0);
        addFormField(basicGrid, "Title:", "", 1);
        addFormField(basicGrid, "Author/Artist:", "", 2);
        addFormField(basicGrid, "Date:", "YYYY-MM-DD", 3);

        // Section 2: Tags & Language
        Label tagsLabel = new Label("Tags & Language");
        tagsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: -app-accent-color; -fx-padding: 20 0 0 0;");

        GridPane tagsGrid = new GridPane();
        tagsGrid.setHgap(20);
        tagsGrid.setVgap(15);
        tagsGrid.getColumnConstraints().addAll(col1, col2);

        addFormField(tagsGrid, "Fandoms:", "Harry Potter, MCU...", 0);
        addFormField(tagsGrid, "Characters:", "Iron Man, Sherlock Holmes...", 1);
        addFormField(tagsGrid, "Relationships:", "Steve/Bucky, Destiel...", 2);
        addFormField(tagsGrid, "Additional Tags:", "Fluff, Angst...", 3);

        // Language Dropdown
        Label langLabel = new Label("Language:");
        langLabel.setStyle("-fx-font-weight: bold;");
        ComboBox<String> langBox = new ComboBox<>();
        langBox.getItems().addAll("English", "Spanish", "French", "German", "Chinese", "Japanese");
        langBox.setValue("English");
        langBox.setMaxWidth(Double.MAX_VALUE);
        tagsGrid.add(langLabel, 0, 4);
        tagsGrid.add(langBox, 1, 4);

        // Search Button
        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().add("filter-button");
        searchBtn.setStyle("-fx-background-color: -app-accent-color; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 30;");
        
        HBox btnBox = new HBox(searchBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        btnBox.setPadding(new Insets(20, 0, 0, 0));

        searchForm.getChildren().addAll(basicInfoLabel, basicGrid, tagsLabel, tagsGrid, btnBox);

        content.getChildren().addAll(header, searchForm);

        ScrollPane scroll = new ScrollPane();
        StackPane centerScroll = new StackPane(content);
        centerScroll.setAlignment(Pos.TOP_CENTER);
        scroll.setContent(centerScroll);
        scroll.setFitToWidth(true);
        scroll.setPannable(true); // Enable panning for smoother feel on touch/trackpads
        
        // Increase scroll speed for trackpads
        scroll.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                double delta = event.getDeltaY();
                double height = scroll.getContent().getBoundsInLocal().getHeight();
                double viewportHeight = scroll.getViewportBounds().getHeight();
                
                if (height > viewportHeight) {
                    double scrollRange = height - viewportHeight;
                    // Multiply delta by a factor (e.g., 4.0) to increase speed
                    double scrollOffset = -delta * 4.0; 
                    double newVValue = scroll.getVvalue() + (scrollOffset / scrollRange);
                    scroll.setVvalue(Math.max(0, Math.min(1, newVValue)));
                    event.consume(); // Consume event to override default behavior
                }
            }
        });
        
        return scroll;
    }

    private void addFormField(GridPane grid, String labelText, String promptText, int row) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-weight: bold;");
        
        TextField field = new TextField();
        field.setPromptText(promptText);
        field.setStyle("-fx-padding: 8; -fx-background-radius: 4;");
        
        grid.add(label, 0, row);
        grid.add(field, 1, row);
    }
}
