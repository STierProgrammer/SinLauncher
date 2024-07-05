// MainLauncherMenu.java
package com.sinlauncher.app.GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainLauncherMenu extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        String text_1 = "Click Me";
        String text_2 = "Don't Click Me";

        Button playBtn = new Button();

        playBtn.getStyleClass().add("rounded-lg"); // Apply CSS class

        playBtn.setText(text_1);

        playBtn.setOnAction(event -> {
            if (playBtn.getText().equals(text_1))
                playBtn.setText(text_2);
            else
                playBtn.setText(text_1);
        });

        StackPane root = new StackPane();
        
        root.getChildren().add(playBtn);

        Scene scene = new Scene(root, 1200, 600);

        scene.getStylesheets().add(getClass().getResource("./css/styles.css").toExternalForm()); // Load CSS file

        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Test");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
