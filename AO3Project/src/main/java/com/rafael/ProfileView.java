package com.rafael;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ProfileView {

    public ScrollPane getView() {

        VBox content = new VBox(20);
        content.setAlignment(Pos.TOP_LEFT);
        content.setPadding(new Insets(40));
        content.setMaxWidth(1000);

        Label title = new Label("Profile");
        title.getStyleClass().add("section-title");

        Label info = new Label("Welcome to your profile page.");
        info.getStyleClass().add("card-desc");

        Label status = new Label("You are currently logged in.");
        status.getStyleClass().add("card-desc");

        content.getChildren().addAll(title, info, status);

        StackPane wrapper = new StackPane(content);
        wrapper.setAlignment(Pos.TOP_CENTER);

        ScrollPane scroll = new ScrollPane(wrapper);
        scroll.setFitToWidth(true);
        scroll.setPannable(true);

        return scroll;
    }
}