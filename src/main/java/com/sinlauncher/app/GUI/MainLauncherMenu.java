// MainLauncherMenu.java
package com.sinlauncher.app.GUI;

import com.sinlauncher.app.GUI.components.StyledButton;
import com.sinlauncher.app.GUI.components.StyledTextInput;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


// Testing
public class MainLauncherMenu extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        StyledButton btn = new StyledButton("Button", "btn");
        StyledTextInput txtI = new StyledTextInput("Placeholder", "txtI");

        HBox _10H = new HBox(10);

        StackPane root = new StackPane();
        
        root.getChildren().add(txtI);

        Scene scene = new Scene(root, 1200, 600);

        scene.getStylesheets().add(getClass().getResource("./css/styles.css").toExternalForm()); // Load CSS file


        primaryStage.setScene(scene);
        primaryStage.setTitle("SinLauncher");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
