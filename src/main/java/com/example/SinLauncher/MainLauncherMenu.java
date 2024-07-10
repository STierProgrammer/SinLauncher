package com.example.SinLauncher;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Primary;


@SpringBootApplication
@EnableEncryptableProperties
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

        scene.getStylesheets().add(getClass().getResource("./styles/styles.css").toExternalForm());

        Image icon = new Image(getClass().getResourceAsStream("/Minecraft.png"));


        primaryStage.getIcons().add(icon);
        primaryStage.setScene(scene);
        primaryStage.setTitle("SinLauncher");
        primaryStage.show();
    }

    public static void main(String[] args) {
        savedArgs = args;
        new Thread(() -> SpringApplication.run(MainLauncherMenu.class, args).start());
        Application.launch(args);
    }
}
