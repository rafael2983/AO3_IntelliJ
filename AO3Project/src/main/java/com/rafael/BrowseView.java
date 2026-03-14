package com.rafael;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class BrowseView {

    public ScrollPane getView() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(40));
        content.setFillWidth(true);
        content.setAlignment(Pos.TOP_CENTER); // Center content vertically
        content.setMaxWidth(1100); // Limit width for better readability

        Label header = new Label("Browse Fandoms");
        header.getStyleClass().add("section-title");

        // Categories
        String[] categories = {"Anime & Manga", "Books & Literature", "Cartoons & Comics", "Celebrities & Real People", "Movies", "Music & Bands", "Other Media", "Theater", "TV Shows", "Video Games", "Uncategorized"};

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(25);
        grid.setAlignment(Pos.CENTER); // Center the grid

        int columns = 3;
        for (int i = 0; i < categories.length; i++) {
            VBox categoryCard = new VBox();
            categoryCard.getStyleClass().add("card");
            categoryCard.setAlignment(Pos.CENTER);
            categoryCard.setPrefSize(250, 100);
            categoryCard.setCursor(javafx.scene.Cursor.HAND);

            Label categoryLabel = new Label(categories[i]);
            categoryLabel.getStyleClass().add("card-title");
            categoryLabel.setStyle("-fx-font-size: 16px; -fx-text-alignment: center;");
            categoryLabel.setWrapText(true);

            categoryCard.getChildren().add(categoryLabel);

            // Click effect
            categoryCard.setOnMouseClicked(e -> {
                categoryCard.setStyle("-fx-background-color: rgba(229, 62, 62, 0.05);");
                // Reset after brief moment
                new Thread(() -> {
                    try {
                        Thread.sleep(150);
                        javafx.application.Platform.runLater(() -> categoryCard.setStyle(""));
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
                System.out.println("Clicked: " + categoryLabel.getText());
            });

            grid.add(categoryCard, i % columns, i / columns);
        }

        content.getChildren().addAll(header, grid);

        ScrollPane scroll = new ScrollPane();
        StackPane centeredContent = new StackPane(content);
        centeredContent.setAlignment(Pos.TOP_CENTER);
        scroll.setContent(centeredContent);
        scroll.setFitToWidth(true);
        scroll.setPannable(true); // Enable panning for smoother feel on touch/trackpads
        
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
}