package com.rafael;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

public class ProfileView {

    public ScrollPane getView() {

        String username = Main.getCurrentUsername() != null ? Main.getCurrentUsername() : "Guest";

        VBox page = new VBox(28);
        page.setAlignment(Pos.TOP_CENTER);
        page.setPadding(new Insets(36, 40, 48, 40));
        page.setMaxWidth(1100);
        page.getStyleClass().add("profile-page");

        /* ================= HEADER ================= */
        HBox header = new HBox(24);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setMaxWidth(980);

        VBox avatarBox = new VBox();
        avatarBox.setAlignment(Pos.CENTER);
        avatarBox.getStyleClass().add("profile-avatar");

        Label avatarLetter = new Label(username.substring(0, 1).toUpperCase());
        avatarLetter.getStyleClass().add("profile-avatar-letter");
        avatarBox.getChildren().add(avatarLetter);

        VBox headerText = new VBox(8);
        headerText.setAlignment(Pos.CENTER_LEFT);

        Label pageTitle = new Label("Profile");
        pageTitle.getStyleClass().add("section-title");

        Label welcome = new Label("Welcome back, " + username + "!");
        welcome.getStyleClass().add("profile-welcome");

        Label subtext = new Label("Manage your account, preferences, and archive activity.");
        subtext.getStyleClass().add("profile-subtext");

        headerText.getChildren().addAll(pageTitle, welcome, subtext);
        header.getChildren().addAll(avatarBox, headerText);

        /* ================= MAIN GRID ================= */
        HBox mainGrid = new HBox(20);
        mainGrid.setAlignment(Pos.TOP_CENTER);
        mainGrid.setMaxWidth(980);

        VBox leftColumn = new VBox(20);
        leftColumn.setPrefWidth(470);
        leftColumn.setMaxWidth(470);

        VBox rightColumn = new VBox(20);
        rightColumn.setPrefWidth(470);
        rightColumn.setMaxWidth(470);

        /* ================= ACCOUNT INFO ================= */
        VBox accountCard = new VBox(14);
        accountCard.getStyleClass().addAll("card", "profile-card");

        Label accountTitle = new Label("Account Information");
        accountTitle.getStyleClass().add("card-title");

        VBox infoList = new VBox(12);
        infoList.getChildren().addAll(
                createProfileRow("Username", username),
                createProfileRow("Account Type", "Registered User"),
                createProfileRow("Status", "Logged In"),
                createProfileRow("Email", username + "@example.com")
        );

        accountCard.getChildren().addAll(accountTitle, infoList);

        /* ================= PREFERENCES ================= */
        VBox preferencesCard = new VBox(14);
        preferencesCard.getStyleClass().addAll("card", "profile-card");

        Label prefTitle = new Label("Preferences");
        prefTitle.getStyleClass().add("card-title");

        VBox prefList = new VBox(12);
        prefList.getChildren().addAll(
                createProfileRow("Theme", Main.isThemeCustomizationEnabled() ? "Dark Mode" : "Light Mode"),
                createProfileRow("Immersion Mode", Main.isImmersionModeEnabled() ? "Enabled" : "Disabled")
        );

        preferencesCard.getChildren().addAll(prefTitle, prefList);

        /* ================= STATS ================= */
        VBox statsCard = new VBox(16);
        statsCard.getStyleClass().addAll("card", "profile-card");

        Label statsTitle = new Label("Archive Stats");
        statsTitle.getStyleClass().add("card-title");

        HBox statsGridTop = new HBox(14);
        statsGridTop.getChildren().addAll(
                createStatCard(String.valueOf(MockDatabase.getWorksCount()), "Works"),
                createStatCard(String.valueOf(MockDatabase.getBookmarkCount()), "Bookmarks")
        );

        HBox statsGridBottom = new HBox(14);
        statsGridBottom.getChildren().addAll(
                createStatCard(String.valueOf(MockDatabase.getFandomCount()), "Fandoms")
        );

        statsCard.getChildren().addAll(statsTitle, statsGridTop, statsGridBottom);

        /* ================= QUICK ACTIONS ================= */
        VBox actionsCard = new VBox(16);
        actionsCard.getStyleClass().addAll("card", "profile-card");

        Label actionsTitle = new Label("Quick Actions");
        actionsTitle.getStyleClass().add("card-title");

        VBox actionButtons = new VBox(12);

        Button myWorksBtn = new Button("Go to My Works");
        myWorksBtn.getStyleClass().add("filter-button");
        myWorksBtn.setMaxWidth(Double.MAX_VALUE);
        myWorksBtn.setOnAction(e -> Main.setCenterContent(new MyWorks().getView()));

        Button bookmarksBtn = new Button("Go to Bookmarks");
        bookmarksBtn.getStyleClass().add("filter-button");
        bookmarksBtn.setMaxWidth(Double.MAX_VALUE);
        bookmarksBtn.setOnAction(e -> Main.setCenterContent(new BookmarksView().getView()));

        actionButtons.getChildren().addAll(myWorksBtn, bookmarksBtn);
        actionsCard.getChildren().addAll(actionsTitle, actionButtons);

        leftColumn.getChildren().addAll(accountCard, preferencesCard);
        rightColumn.getChildren().addAll(statsCard, actionsCard);
        mainGrid.getChildren().addAll(leftColumn, rightColumn);

        /* ================= BOTTOM ACTIONS ================= */
        HBox bottomActions = new HBox(12);
        bottomActions.setAlignment(Pos.CENTER_LEFT);
        bottomActions.setMaxWidth(980);

        Button logoutBtn = new Button("Log Out");
        logoutBtn.getStyleClass().add("button");
        logoutBtn.setOnAction(e -> {
            Main.logout();
            Main.setCenterContent(new LoginView().getView());
        });
        page.getChildren().addAll(header, mainGrid, bottomActions);

        StackPane wrapper = new StackPane(page);
        wrapper.setAlignment(Pos.TOP_CENTER);

        ScrollPane scroll = new ScrollPane(wrapper);
        scroll.setFitToWidth(true);
        scroll.setPannable(true);
        scroll.setUserData("profile-view");

        return scroll;
    }

    private HBox createProfileRow(String labelText, String valueText) {
        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("profile-row");

        Label label = new Label(labelText);
        label.getStyleClass().add("profile-label");
        label.setMinWidth(120);

        Label value = new Label(valueText);
        value.getStyleClass().add("profile-value");
        value.setWrapText(true);

        row.getChildren().addAll(label, value);
        return row;
    }

    private VBox createStatCard(String number, String labelText) {
        VBox stat = new VBox(6);
        stat.setAlignment(Pos.CENTER_LEFT);
        stat.getStyleClass().add("profile-stat-card");
        stat.setPrefWidth(220);
        stat.setPadding(new Insets(18));

        Label numberLabel = new Label(number);
        numberLabel.getStyleClass().add("profile-stat-number");

        Label textLabel = new Label(labelText);
        textLabel.getStyleClass().add("profile-stat-label");

        stat.getChildren().addAll(numberLabel, textLabel);
        return stat;
    }
}