package com.example.SinLauncher.pages;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlayPage {

    public ScrollPane getPlayPageContent() {
        VBox centerContent = new VBox();
        centerContent.getStyleClass().add("center-content");
        centerContent.setSpacing(30);
        centerContent.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("SinLauncher");
        titleLabel.getStyleClass().add("title-label");

        Button playButton = new Button("Play");
        playButton.getStyleClass().add("play-button");

        HBox versionBox = new HBox();
        versionBox.setAlignment(Pos.CENTER);
        versionBox.setSpacing(10);

        Label versionLabel = new Label("Version:");
        versionLabel.getStyleClass().add("version-label");

        Button versionButton = new Button("Select Version");
        versionButton.getStyleClass().add("version-button");

        versionBox.getChildren().addAll(versionLabel, versionButton);

        centerContent.getChildren().addAll(titleLabel, playButton, versionBox);

        ScrollPane centerScrollPane = new ScrollPane(centerContent);
        centerScrollPane.getStyleClass().add("center-scroll-pane");
        centerScrollPane.setFitToWidth(true);

        return centerScrollPane;
    }
}
