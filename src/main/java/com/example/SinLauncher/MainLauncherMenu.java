package com.example.SinLauncher;// com.MainLauncherMenu.java

import org.springframework.boot.SpringApplication;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


// Testing
public class MainLauncherMenu extends Application {

    private static String[] savedArgs;

    @Override
    public void start(Stage primaryStage) throws Exception {
        TextField searchInput = new TextField();
        searchInput.getStyleClass().add("txtI");
        searchInput.setPromptText("Search");

        StackPane root = new StackPane();
        
        root.getChildren().add(searchInput);

        Scene scene = new Scene(root, 1200, 600);

        scene.getStylesheets().add(getClass().getResource("./styles/styles.css").toExternalForm()); // Load CSS file


        primaryStage.setScene(scene);
        primaryStage.setTitle("com/example/SinLauncher");
        primaryStage.show();
    }

    public static void main(String[] args) {
        savedArgs = args;
        new Thread(() -> SpringApplication.run(MainLauncherMenu.class, args).start());
        Application.launch(args);
    }
}
