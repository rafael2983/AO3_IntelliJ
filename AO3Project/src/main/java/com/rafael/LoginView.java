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
        container.setMaxWidth(420);

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

        Label hint = new Label("Demo accounts: " + "sofia_batumbakal / Password123!   or   daniel_reyes / Password123!");
        hint.getStyleClass().add("card-desc");

        loginBtn.setOnAction(e -> {
            String enteredUsername = username.getText().trim();
            String enteredPassword = password.getText().trim();

            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                status.setText("Please enter username and password.");
                return;
            }

            if (MockDatabase.validateLogin(enteredUsername, enteredPassword)) {
                Main.setLoggedIn(true);
                Main.setCurrentUsername(enteredUsername);
                status.setText("Login successful!");
                Main.setCenterContent(new ProfileView().getView());
            } else {
                Main.setLoggedIn(false);
                Main.setCurrentUsername(null);
                status.setText("Invalid username or password.");
            }
        });

        container.getChildren().addAll(title, username, password, loginBtn, status, hint);

        StackPane wrapper = new StackPane(container);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPadding(new Insets(40));

        ScrollPane scroll = new ScrollPane(wrapper);
        scroll.setFitToWidth(true);
        scroll.setPannable(true);

        return scroll;
    }
}