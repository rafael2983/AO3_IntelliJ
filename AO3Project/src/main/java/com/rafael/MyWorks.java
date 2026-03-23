package com.rafael;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

public class MyWorks {

    public ScrollPane getView() {

        VBox page = new VBox(24);
        page.setAlignment(Pos.TOP_CENTER);
        page.setPadding(new Insets(36, 40, 48, 40));
        page.setMaxWidth(1100);

        VBox content = new VBox(20);
        content.setMaxWidth(900);
        content.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("My Works");
        title.getStyleClass().add("section-title");

        Label subtitle = new Label("Stories and works published under your account.");
        subtitle.getStyleClass().add("card-desc");

        content.getChildren().addAll(title, subtitle);

        if (!Main.isLoggedIn()) {
            VBox loginCard = new VBox(12);
            loginCard.getStyleClass().add("card");
            loginCard.setAlignment(Pos.CENTER_LEFT);

            Label loginTitle = new Label("Sign in required");
            loginTitle.getStyleClass().add("card-title");

            Label loginDesc = new Label("You need to sign in to view your works.");
            loginDesc.getStyleClass().add("card-desc");
            loginDesc.setWrapText(true);

            Button loginBtn = new Button("Go to Sign In");
            loginBtn.getStyleClass().add("button");
            loginBtn.setOnAction(e -> Main.setCenterContent(new LoginView().getView()));

            loginCard.getChildren().addAll(loginTitle, loginDesc, loginBtn);
            content.getChildren().add(loginCard);

        } else {
            String username = Main.getCurrentUsername() != null ? Main.getCurrentUsername() : "User";

            VBox profileCard = new VBox(12);
            profileCard.getStyleClass().add("card");

            Label welcome = new Label(username + "'s Works");
            welcome.getStyleClass().add("card-title");

            Label desc = new Label("This page shows the stories associated with your account.");
            desc.getStyleClass().add("card-desc");
            desc.setWrapText(true);

            profileCard.getChildren().addAll(welcome, desc);
            content.getChildren().add(profileCard);

            boolean hasWorks = false;

            for (Story story : MockDatabase.getAllStories()) {
                if (story.getAuthor().equalsIgnoreCase(username)) {
                    hasWorks = true;

                    VBox storyCard = new VBox(12);
                    storyCard.getStyleClass().add("card");
                    storyCard.setAlignment(Pos.TOP_LEFT);

                    Label storyTitle = new Label(story.getTitle());
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

                    actions.getChildren().add(readBtn);

                    storyCard.getChildren().addAll(storyTitle, fandom, genre, summary, actions);
                    content.getChildren().add(storyCard);
                }
            }

            if (!hasWorks) {
                VBox emptyCard = new VBox(12);
                emptyCard.getStyleClass().add("card");
                emptyCard.setAlignment(Pos.CENTER_LEFT);

                Label emptyTitle = new Label("No works yet");
                emptyTitle.getStyleClass().add("card-title");

                Label emptyDesc = new Label("You have not posted any works yet. Create your first work to start building your archive.");
                emptyDesc.getStyleClass().add("card-desc");
                emptyDesc.setWrapText(true);

                Button createWorkBtn = new Button("Create Work");
                createWorkBtn.getStyleClass().add("button");
                createWorkBtn.setOnAction(e -> {
                    Main.setCenterContent(new CreateWorkView().getView());
                });

                emptyCard.getChildren().addAll(emptyTitle, emptyDesc, createWorkBtn);
                content.getChildren().add(emptyCard);
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