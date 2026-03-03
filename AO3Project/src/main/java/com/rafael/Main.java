package com.rafael;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        BorderPane root = new BorderPane();

        // =========================
        // TOP NAVBAR
        // =========================
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(15));
        topBar.getStyleClass().add("top-bar");
        topBar.setAlignment(Pos.CENTER_LEFT);

        ImageView logo = new ImageView(
                new Image(getClass().getResourceAsStream("/logo.png"))
        );
        logo.setFitHeight(30);
        logo.setPreserveRatio(true);

        Label title = new Label("Archive of Our Own");
        title.getStyleClass().add("logo-text");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Hyperlink fandoms = new Hyperlink("Fandoms");
        Hyperlink browse = new Hyperlink("Browse");
        Hyperlink search = new Hyperlink("Search");
        Hyperlink about = new Hyperlink("About");

        TextField searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setPrefWidth(180);

        topBar.getChildren().addAll(
                logo, title,
                spacer,
                fandoms, browse, search, about,
                searchField
        );

        root.setTop(topBar);

        // =========================
        // LEFT SIDEBAR
        // =========================
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(160);
        sidebar.getStyleClass().add("sidebar");

        Button myWorks = new Button("My Works");
        Button bookmarks = new Button("Bookmarks");
        Button history = new Button("History");
        Button profile = new Button("Profile");

        sidebar.getChildren().addAll(myWorks, bookmarks, history, profile);
        root.setLeft(sidebar);

        // =========================
        // CENTER CONTENT (HOME)
        // =========================
        VBox content = new VBox(30);
        content.setPadding(new Insets(30));

        Label exploreTitle = new Label("Explore Fandoms");
        exploreTitle.getStyleClass().add("section-title");

        Label subtitle = new Label(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
        );

        HBox filters = new HBox(10);
        Button popularBtn = new Button("Popular");
        Button upcomingBtn = new Button("Upcoming");
        Button allBtn = new Button("All");
        filters.getChildren().addAll(popularBtn, upcomingBtn, allBtn);

        VBox exploreSection = new VBox(10,
                exploreTitle,
                subtitle,
                filters
        );

        Label popularTitle = new Label("Popular Fandoms");
        popularTitle.getStyleClass().add("section-title");

        GridPane fandomGrid = new GridPane();
        fandomGrid.setHgap(20);
        fandomGrid.setVgap(20);

        for (int i = 0; i < 6; i++) {
            VBox card = createFandomCard("K-pop",
                    "Lorem ipsum dolor sit amet");
            fandomGrid.add(card, i % 3, i / 3);
        }

        Label allFandomsTitle = new Label("All Fandoms");
        allFandomsTitle.getStyleClass().add("section-title");

        GridPane table = new GridPane();
        table.setHgap(40);
        table.setVgap(15);

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                table.add(new Label("Lorem ipsum"), col, row);
            }
        }

        content.getChildren().addAll(
                exploreSection,
                popularTitle,
                fandomGrid,
                allFandomsTitle,
                table
        );

        ScrollPane homeScrollPane = new ScrollPane(content);
        homeScrollPane.setFitToWidth(true);

        root.setCenter(homeScrollPane);

        // =========================
        // NAVIGATION LOGIC
        // =========================
        about.setOnAction(e ->
                root.setCenter(new AboutView().getView())
        );

        fandoms.setOnAction(e ->
                root.setCenter(homeScrollPane)
        );

        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(
                getClass().getResource("/style.css").toExternalForm()
        );

        stage.setTitle("AO3 UI Mock");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createFandomCard(String title, String desc) {
        VBox box = new VBox(5);
        box.setPadding(new Insets(15));
        box.getStyleClass().add("card");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");

        Label descLabel = new Label(desc);

        box.getChildren().addAll(titleLabel, descLabel);
        return box;
    }

    public static void main(String[] args) {
        launch();
    }
}