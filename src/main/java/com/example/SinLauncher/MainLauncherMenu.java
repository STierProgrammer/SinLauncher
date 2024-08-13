package com.example.SinLauncher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.SinLauncher.config.Config;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/furnace.png"))); // Placeholder for logo
        logo.setFitHeight(50);
        logo.setFitWidth(50);

        Label titleLabel = new Label("SinLauncher");
        titleLabel.getStyleClass().add("title-label");

        topSection.getChildren().addAll(logo, titleLabel);
        topSection.setSpacing(20);
        topSection.setAlignment(Pos.CENTER);

        VBox leftSection = new VBox();
        leftSection.getStyleClass().add("left-section");

        Button playButton = new Button("Play");
        playButton.getStyleClass().add("nav-button");

        Button settingsButton = new Button("Settings");
        settingsButton.getStyleClass().add("nav-button");

        Button modsButton = new Button("Mods");
        modsButton.getStyleClass().add("nav-button");

        leftSection.getChildren().addAll(playButton, settingsButton, modsButton);
        leftSection.setSpacing(20);

        VBox centerContent = new VBox();
        centerContent.getStyleClass().add("center-content");

        for (int i = 0; i < 10; i++) {
            Label label = new Label("News Item " + (i + 1));
            label.getStyleClass().add("content-label");
            centerContent.getChildren().add(label);
        }

        ScrollPane centerScrollPane = new ScrollPane(centerContent);
        centerScrollPane.getStyleClass().add("center-scroll-pane");

        HBox bottomSection = new HBox();
        bottomSection.getStyleClass().add("bottom-section");

        Label versionLabel = new Label("Version:");
        versionLabel.getStyleClass().add("version-label");

        ComboBox<String> versionComboBox = new ComboBox<>();
        versionComboBox.getItems().addAll("1.20.1", "1.19.4", "1.18.2");
        versionComboBox.getStyleClass().add("version-combo-box");
        versionComboBox.setValue("1.20.1");

        TextField usernameInput = new TextField();
        usernameInput.getStyleClass().add("txtI");
        usernameInput.setPromptText("Enter your username...");

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("login-button");

        bottomSection.getChildren().addAll(versionLabel, versionComboBox, usernameInput, loginButton);
        bottomSection.setSpacing(10);

        // Main pane layout
        BorderPane mainPane = new BorderPane();
        mainPane.setTop(topSection);
        mainPane.setLeft(leftSection);
        mainPane.setCenter(centerScrollPane);
        mainPane.setBottom(bottomSection);

        Scene scene = new Scene(mainPane, 1200, 700);
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
        savedArgs = args;
        
        init(App.DIR);
        init(Config.PATH);

        new Thread(() -> {
            SpringApplication.run(MainLauncherMenu.class, args);
            System.out.println("Spring boot fully started");
        }).start();

        launch(args);
        System.out.println("JavaFX Running");
    }
}