package com.example.SinLauncher.pages;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

public class PlayPage {
    
    public ScrollPane getPlayPageContent() {
        VBox centerContent = new VBox();
        centerContent.getStyleClass().add("center-content");

        for (int i = 0; i < 10; i++) {
            Label label = new Label("News Item " + (i + 1));
            label.getStyleClass().add("content-label");
            centerContent.getChildren().add(label);
        }

        ScrollPane centerScrollPane = new ScrollPane(centerContent);
        centerScrollPane.getStyleClass().add("center-scroll-pane");

        return centerScrollPane;
    }
}
