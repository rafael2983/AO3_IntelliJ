package com.rafael;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class LoginView {

    public ScrollPane getView() {

        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(40));
        container.setMaxWidth(400);

        Label title = new Label("Sign In");
        title.getStyleClass().add("section-title");

        TextField username = new TextField();
        username.setPromptText("Username");
        username.getStyleClass().add("search-field");

        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.getStyleClass().add("search-field");

        Button loginBtn = new Button("Sign In");
        loginBtn.getStyleClass().add("button");

        Label status = new Label();
        status.getStyleClass().add("card-desc");

        loginBtn.setOnAction(e -> {
            if (!username.getText().isEmpty() && !password.getText().isEmpty()) {

                // simulate login
                Main.setLoggedIn(true);

                status.setText("Login successful!");

                // redirect to profile
                Main.setCenterContent(new ProfileView().getView());

            } else {
                status.setText("Please enter username and password.");
            }
        });

        container.getChildren().addAll(title, username, password, loginBtn, status);

        StackPane wrapper = new StackPane(container);
        wrapper.setAlignment(Pos.CENTER);

        ScrollPane scroll = new ScrollPane(wrapper);
        scroll.setFitToWidth(true);

        return scroll;
    }
}