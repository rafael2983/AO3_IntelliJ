package com.rafael;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;

public class ReaderView {
    private int currentPage = 0;
    private List<String> pages;
    private Label textLabel = new Label();

    public VBox getView(Story story) {
        // Initialize pagination
        pages = StoryPageService.paginate(story.getFullText());

        VBox readerContainer = new VBox(20);
        readerContainer.setPadding(new Insets(30));
        readerContainer.setAlignment(Pos.TOP_CENTER);
        readerContainer.setStyle("-fx-background-color: #ffffff;");

        // Reader Title
        Label title = new Label(story.getTitle());
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Text display
        textLabel.setText(pages.get(0));
        textLabel.setWrapText(true);
        textLabel.setMaxWidth(800); // AO3-style readability width
        textLabel.setStyle("-fx-font-size: 16px; -fx-line-spacing: 5;");

        // Navigation Controls
        HBox navControls = new HBox(20);
        navControls.setAlignment(Pos.CENTER);

        Button prevBtn = new Button("Previous");
        Button nextBtn = new Button("Next");
        Label pageIndicator = new Label("Page 1 of " + pages.size());

        prevBtn.setOnAction(e -> changePage(-1, pageIndicator));
        nextBtn.setOnAction(e -> changePage(1, pageIndicator));

        navControls.getChildren().addAll(prevBtn, pageIndicator, nextBtn);

        readerContainer.getChildren().addAll(title, textLabel, navControls);
        return readerContainer;
    }

    private void changePage(int direction, Label indicator) {
        int newPage = currentPage + direction;
        if (newPage >= 0 && newPage < pages.size()) {
            currentPage = newPage;
            textLabel.setText(pages.get(currentPage));
            indicator.setText("Page " + (currentPage + 1) + " of " + pages.size());
        }
    }
}