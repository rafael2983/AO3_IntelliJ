package com.rafael;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;

public class AboutView {

    public ScrollPane getView() {
        VBox content = new VBox(40);
        content.setPadding(new Insets(40));
        content.setAlignment(Pos.TOP_CENTER);
        content.setMaxWidth(1000); // Limit width for readability

        // Hero Section
        VBox hero = new VBox(15);
        hero.setAlignment(Pos.CENTER);
        
        Label title = new Label("About the OTW");
        title.getStyleClass().add("section-title");
        title.setStyle("-fx-font-size: 36px; -fx-border-width: 0 0 3px 0;");

        Label subtitle = new Label("Preserving the history of fanworks and fan culture.");
        subtitle.setStyle("-fx-font-size: 18px; -fx-text-fill: -app-text-color; -fx-opacity: 0.8;");
        
        hero.getChildren().addAll(title, subtitle);

        // Main Info Card
        VBox infoCard = new VBox(20);
        infoCard.getStyleClass().add("card");

        Label infoText1 = new Label(
                "The Organization for Transformative Works (OTW) is a nonprofit organization established by fans to serve the interests of fans by providing access to and preserving the history of fanworks and fan culture in its myriad forms."
        );
        infoText1.setWrapText(true);
        infoText1.getStyleClass().add("card-desc");
        infoText1.setStyle("-fx-font-size: 16px; -fx-line-spacing: 6px;");

        Label infoText2 = new Label(
                "We believe that fanworks are transformative and that transformative works are legitimate. The OTW represents a practice of transformative fanwork historically rooted in a primarily female culture. The OTW will preserve the record of that history as we pursue our mission while encouraging new and non-mainstream expressions of cultural identity within fandom."
        );
        infoText2.setWrapText(true);
        infoText2.getStyleClass().add("card-desc");
        infoText2.setStyle("-fx-font-size: 16px; -fx-line-spacing: 6px;");

        infoCard.getChildren().addAll(infoText1, infoText2);

        // Projects Section
        VBox projectsSection = new VBox(20);
        projectsSection.setAlignment(Pos.CENTER_LEFT);
        
        Label projectsTitle = new Label("Our Projects");
        projectsTitle.getStyleClass().add("section-title");

        GridPane projectsGrid = new GridPane();
        projectsGrid.setHgap(30);
        projectsGrid.setVgap(30);
        
        projectsGrid.add(createProjectCard("Archive of Our Own", "A noncommercial and nonprofit central hosting place for fanworks."), 0, 0);
        projectsGrid.add(createProjectCard("Fanlore", "A wiki for fans to document their history and traditions."), 2, 0);
        projectsGrid.add(createProjectCard("Open Doors", "Preserving at-risk fannish content."), 0, 2);
        projectsGrid.add(createProjectCard("Legal Advocacy", "Protecting the rights of fans to create transformative works."), 2, 2);

        // Add visible dividers
        Region vDivider = new Region();
        vDivider.getStyleClass().add("divider-vertical");
        projectsGrid.add(vDivider, 1, 0, 1, 3); // Span across rows

        Region hDivider1 = new Region();
        hDivider1.getStyleClass().add("divider-horizontal");
        projectsGrid.add(hDivider1, 0, 1, 3, 1); // Span across columns

        projectsSection.getChildren().addAll(projectsTitle, projectsGrid);

        content.getChildren().addAll(hero, infoCard, projectsSection);

        ScrollPane scroll = new ScrollPane();
        StackPane centerScroll = new StackPane(content);
        centerScroll.setAlignment(Pos.TOP_CENTER);
        scroll.setContent(centerScroll);
        scroll.setFitToWidth(true);
        scroll.setPannable(true); // Enable panning for smoother feel on touch/trackpads
        
        // Custom smooth scroll logic
        scroll.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                double delta = event.getDeltaY();
                double height = scroll.getContent().getBoundsInLocal().getHeight();
                double viewportHeight = scroll.getViewportBounds().getHeight();
                
                if (height > viewportHeight) {
                    double scrollRange = height - viewportHeight;
                    
                    // Cap the maximum delta to prevent huge jumps from fast trackpad flings
                    double clampedDelta = Math.max(-40, Math.min(40, delta));
                    
                    // Reduce the multiplier for a smoother feel
                    double scrollOffset = -clampedDelta * 1.5; 

                    double newVValue = scroll.getVvalue() + (scrollOffset / scrollRange);
                    scroll.setVvalue(Math.max(0, Math.min(1, newVValue)));
                    event.consume(); // Consume event to override default behavior
                }
            }
        });
        
        return scroll;
    }

    private VBox createProjectCard(String title, String desc) {
        VBox card = new VBox(10);
        card.getStyleClass().add("project-card");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(10));
        card.setMinHeight(120);

        Label cardTitle = new Label(title);
        cardTitle.getStyleClass().add("card-title");
        
        Label cardDesc = new Label(desc);
        cardDesc.setWrapText(true);
        cardDesc.getStyleClass().add("card-desc");

        card.getChildren().addAll(cardTitle, cardDesc);
        return card;
    }
}