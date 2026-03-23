package com.rafael;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CreateWorkView {

    public ScrollPane getView() {

        VBox page = new VBox(24);
        page.setAlignment(Pos.TOP_CENTER);
        page.setPadding(new Insets(36, 40, 48, 40));
        page.setMaxWidth(1100);

        VBox content = new VBox(20);
        content.setMaxWidth(900);
        content.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Create Work");
        title.getStyleClass().add("section-title");

        Label subtitle = new Label("Draft and publish a new work to the archive.");
        subtitle.getStyleClass().add("card-desc");

        content.getChildren().addAll(title, subtitle);

        if (!Main.isLoggedIn()) {
            VBox loginCard = new VBox(12);
            loginCard.getStyleClass().add("card");
            loginCard.setAlignment(Pos.CENTER_LEFT);

            Label loginTitle = new Label("Sign in required");
            loginTitle.getStyleClass().add("card-title");

            Label loginDesc = new Label("You need to sign in before creating a work.");
            loginDesc.getStyleClass().add("card-desc");
            loginDesc.setWrapText(true);

            Button loginBtn = new Button("Go to Sign In");
            loginBtn.getStyleClass().add("button");
            loginBtn.setOnAction(e -> Main.setCenterContent(new LoginView().getView()));

            loginCard.getChildren().addAll(loginTitle, loginDesc, loginBtn);
            content.getChildren().add(loginCard);
        } else {
            VBox formCard = new VBox(18);
            formCard.getStyleClass().add("card");
            formCard.setAlignment(Pos.TOP_LEFT);

            TextField titleField = new TextField();
            titleField.setPromptText("Work Title");
            titleField.getStyleClass().add("search-field");

            TextField fandomField = new TextField();
            fandomField.setPromptText("Fandom");
            fandomField.getStyleClass().add("search-field");

            TextField genreField = new TextField();
            genreField.setPromptText("Genre");
            genreField.getStyleClass().add("search-field");

            TextField tagsField = new TextField();
            tagsField.setPromptText("Tags (comma separated)");
            tagsField.getStyleClass().add("search-field");

            TextArea summaryArea = new TextArea();
            summaryArea.setPromptText("Summary");
            summaryArea.setWrapText(true);
            summaryArea.setPrefRowCount(4);
            summaryArea.getStyleClass().add("search-field");

            TextArea contentArea = new TextArea();
            contentArea.setPromptText("Write your story here...");
            contentArea.setWrapText(true);
            contentArea.setPrefRowCount(16);
            contentArea.getStyleClass().add("search-field");

            Label statusLabel = new Label();
            statusLabel.getStyleClass().add("card-desc");

            HBox actions = new HBox(12);
            actions.setAlignment(Pos.CENTER_LEFT);

            Button publishBtn = new Button("Publish Work");
            publishBtn.getStyleClass().add("button");

            Button cancelBtn = new Button("Cancel");
            cancelBtn.getStyleClass().add("filter-button");
            cancelBtn.setOnAction(e -> Main.setCenterContent(new MyWorks().getView()));

            publishBtn.setOnAction(e -> {
                String titleText = titleField.getText().trim();
                String fandomText = fandomField.getText().trim();
                String genreText = genreField.getText().trim();
                String tagsText = tagsField.getText().trim();
                String summaryText = summaryArea.getText().trim();
                String bodyText = contentArea.getText().trim();
                String author = Main.getCurrentUsername() != null ? Main.getCurrentUsername() : "User";

                if (titleText.isEmpty() || fandomText.isEmpty() || genreText.isEmpty()
                        || summaryText.isEmpty() || bodyText.isEmpty()) {
                    statusLabel.setText("Please fill in all required fields.");
                    return;
                }

                Story newStory = new Story(
                        titleText,
                        author,
                        fandomText,
                        summaryText,
                        tagsText.isEmpty() ? "No tags" : tagsText,
                        genreText,
                        bodyText
                );

                MockDatabase.addUserWork(newStory);
                statusLabel.setText("Work published successfully!");
                Main.setCenterContent(new MyWorks().getView());
            });

            actions.getChildren().addAll(publishBtn, cancelBtn);

            formCard.getChildren().addAll(
                    new Label("Title"),
                    titleField,
                    new Label("Fandom"),
                    fandomField,
                    new Label("Genre"),
                    genreField,
                    new Label("Tags"),
                    tagsField,
                    new Label("Summary"),
                    summaryArea,
                    new Label("Content"),
                    contentArea,
                    actions,
                    statusLabel
            );

            content.getChildren().add(formCard);
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