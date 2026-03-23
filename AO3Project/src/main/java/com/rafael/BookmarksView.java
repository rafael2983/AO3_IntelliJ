package com.rafael;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.collections.ObservableList;

public class BookmarksView {

    public ScrollPane getView() {

        VBox page = new VBox(24);
        page.setAlignment(Pos.TOP_CENTER);
        page.setPadding(new Insets(36, 40, 48, 40));
        page.setMaxWidth(1100);

        VBox content = new VBox(20);
        content.setMaxWidth(900);
        content.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Bookmarks");
        title.getStyleClass().add("section-title");

        Label subtitle = new Label("Your saved stories are listed here.");
        subtitle.getStyleClass().add("card-desc");

        content.getChildren().addAll(title, subtitle);

        ObservableList<Story> bookmarkedStories = MockDatabase.getBookmarkedStories();

        if (bookmarkedStories.isEmpty()) {
            VBox emptyCard = new VBox(12);
            emptyCard.getStyleClass().add("card");
            emptyCard.setAlignment(Pos.CENTER_LEFT);

            Label emptyTitle = new Label("No bookmarks yet");
            emptyTitle.getStyleClass().add("card-title");

            Label emptyDesc = new Label("Stories you bookmark from the reader page will appear here.");
            emptyDesc.getStyleClass().add("card-desc");
            emptyDesc.setWrapText(true);

            Button goHomeBtn = new Button("Browse Stories");
            goHomeBtn.getStyleClass().add("filter-button");
            goHomeBtn.setOnAction(e -> Main.setCenterContent(new HomeView().getView()));

            emptyCard.getChildren().addAll(emptyTitle, emptyDesc, goHomeBtn);
            content.getChildren().add(emptyCard);

        } else {
            for (Story story : bookmarkedStories) {
                VBox storyCard = new VBox(12);
                storyCard.getStyleClass().add("card");
                storyCard.setAlignment(Pos.TOP_LEFT);

                Label storyTitle = new Label(story.getTitle() + " by " + story.getAuthor());
                storyTitle.getStyleClass().add("card-title");

                Label fandom = new Label("Fandom: " + story.getFandom());
                fandom.getStyleClass().add("card-desc");

                Label genre = new Label("Genre: " + story.getGenre());
                genre.getStyleClass().add("card-desc");

                Label summary = new Label(story.getSummary());
                summary.getStyleClass().add("card-desc");
                summary.setWrapText(true);

                HBox actions = new HBox(12);
                actions.setAlignment(Pos.CENTER_LEFT);

                Button readBtn = new Button("Read Story");
                readBtn.getStyleClass().add("filter-button");
                readBtn.setOnAction(e -> {
                    ReaderView reader = new ReaderView();
                    Main.setCenterContent(reader.getView(story, Main.getRoot()));
                });

                Button removeBtn = new Button("Remove Bookmark");
                removeBtn.getStyleClass().add("button");
                removeBtn.setOnAction(e -> {
                    MockDatabase.removeBookmark(story);
                    Main.setCenterContent(new BookmarksView().getView());
                });

                actions.getChildren().addAll(readBtn, removeBtn);

                storyCard.getChildren().addAll(storyTitle, fandom, genre, summary, actions);
                content.getChildren().add(storyCard);
            }
        }

        page.getChildren().add(content);

        StackPane wrapper = new StackPane(page);
        wrapper.setAlignment(Pos.TOP_CENTER);

        ScrollPane scroll = new ScrollPane(wrapper);
        scroll.setFitToWidth(true);
        scroll.setPannable(true);

        return scroll;
    }
}