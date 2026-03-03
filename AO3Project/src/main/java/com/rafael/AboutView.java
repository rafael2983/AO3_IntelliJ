package com.rafael;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

public class AboutView {

    public ScrollPane getView() {

        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        content.setFillWidth(true);

        Label header = new Label("About the OTW");
        header.getStyleClass().add("section-title");

        Label subtitle = new Label(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut"
        );
        subtitle.setWrapText(true);

        VBox infoBox = new VBox(15);
        infoBox.setPadding(new Insets(25));
        infoBox.getStyleClass().add("info-box");
        infoBox.setMaxWidth(Double.MAX_VALUE);

        Label paragraph1 = new Label(
                "The Organization for Transformative Works (OTW) is a nonprofit founded by fans in 2007 to protect, preserve, and celebrate fanworks and fan culture. It believes fanworks are transformative and legitimate forms of creative expression. OTW actively defends fan creations from legal and commercial threats. It also works to safeguard fan communities, values, and history while ensuring broad access for all fans."
        );
        paragraph1.setWrapText(true);

        Label paragraph2 = new Label(
                "The Archive of Our Own offers a noncommercial and nonprofit central hosting place for fanworks using open-source archiving software. We welcome contributions to our GitHub repository, and a list of open tasks is available on our Jira project."
        );
        paragraph2.setWrapText(true);

        infoBox.getChildren().addAll(paragraph1, paragraph2);

        Label projectsTitle = new Label("Other major projects include:");

        // ===== PROJECT GRID (2 x 2 Balanced Layout) =====
        GridPane projectsGrid = new GridPane();
        projectsGrid.setHgap(40);
        projectsGrid.setVgap(30);
        projectsGrid.setMaxWidth(Double.MAX_VALUE);

        // Create 2 equal columns
        for (int i = 0; i < 2; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(50);
            column.setHgrow(Priority.ALWAYS);
            projectsGrid.getColumnConstraints().add(column);
        }

        // Add cards in 2x2 format
        projectsGrid.add(createProjectCard("Fanlore",
                "Preserving the history of transformative fanworks and the fandoms from which they have arisen."), 0, 0);

        projectsGrid.add(createProjectCard("Legal Advocacy",
                "Protecting fanworks from commercial exploitation and legal challenge."), 1, 0);

        projectsGrid.add(createProjectCard("Open Doors",
                "Offers shelter to at-risk fannish projects."), 0, 1);

        projectsGrid.add(createProjectCard("Works/Cultures",
                "Promote scholarship on fanworks and practices."), 1, 1);

        content.getChildren().addAll(
                header,
                subtitle,
                infoBox,
                projectsTitle,
                projectsGrid
        );

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);

        return scroll;
    }

    private VBox createProjectCard(String title, String desc) {

        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.getStyleClass().add("project-card");
        box.setMaxWidth(Double.MAX_VALUE);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");

        Label descLabel = new Label(desc);
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(Double.MAX_VALUE);

        box.getChildren().addAll(titleLabel, descLabel);

        return box;
    }
}