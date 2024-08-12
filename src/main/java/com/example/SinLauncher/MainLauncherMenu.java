package com.example.SinLauncher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.SinLauncher.config.Config;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import javafx.application.Application;
import javafx.geometry.Insets;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@SpringBootApplication
@EnableEncryptableProperties
public class MainLauncherMenu extends Application {

    private static String[] savedArgs;

    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox topSection = new HBox();

        topSection.getStyleClass().add("top-section");

        Label titleLabel = new Label("");

        titleLabel.getStyleClass().add("title-label");
        topSection.getChildren().add(titleLabel);

        VBox leftSection = new VBox();

        leftSection.getStyleClass().add("left-section");

        VBox centerContent = new VBox();

        centerContent.getStyleClass().add("center-content");

        for (int i = 0; i < 20; i++) {
            Label label = new Label("Content " + (i + 1));
            label.getStyleClass().add("content-label");
            centerContent.getChildren().add(label);
        }

        ScrollPane centerScrollPane = new ScrollPane(centerContent);

        centerScrollPane.getStyleClass().add("center-scroll-pane");

        HBox bottomSection = new HBox();

        bottomSection.getStyleClass().add("bottom-section");

        Label versionLabel = new Label("<Installation> 1.20.1");

        versionLabel.getStyleClass().add("version-label");
        bottomSection.getChildren().add(versionLabel);

        HBox.setMargin(versionLabel, new Insets(0, 0, 0, 10));

        TextField searchInput = new TextField();

        searchInput.getStyleClass().add("txtI");
        searchInput.setPromptText("Enter your username...");
        bottomSection.getChildren().add(searchInput);

        HBox.setMargin(searchInput, new Insets(0, 10, 0, 0));

        BorderPane mainPane = new BorderPane();

        mainPane.setTop(topSection);
        mainPane.setLeft(leftSection);
        mainPane.setCenter(centerScrollPane);
        mainPane.setBottom(bottomSection);

        Scene scene = new Scene(mainPane, 1200, 600);

        scene.getStylesheets().add(getClass().getResource("./styles/styles.css").toExternalForm());

        Image icon = new Image(getClass().getResourceAsStream("/Minecraft.png"));

        primaryStage.getIcons().add(icon);
        primaryStage.setScene(scene);
        primaryStage.setTitle("SinLauncher");
        primaryStage.show();
    }

    public static void init(Object arg) {

    }

    public static void main(String[] args) {
        // When we use mvn javafx:run Spring Boot should run as well due to it having a
        // separate thread
        // This must run before JavaFX

        savedArgs = args;

        init(App.DIR);
        init(Config.PATH);

        new Thread(() -> {
            SpringApplication.run(MainLauncherMenu.class, args);

            System.out.println("Spring boot fully started");
        }).start();

        launch(args);

        System.out.println("JavaFx Running");

    }
}
