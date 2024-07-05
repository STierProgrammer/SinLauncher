package com.sinlauncher.app.GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainLauncherMenu extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Button btn = new Button();
        
        btn.setText("Hello JavaFX");
        btn.setOnAction(event -> System.out.println("Hello JavaFX"));

        Scene scene = new Scene(btn, 300, 250);

        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
