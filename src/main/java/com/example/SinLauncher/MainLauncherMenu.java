package com.example.SinLauncher;

import com.example.SinLauncher.pages.PlayPage;
import com.example.SinLauncher.pages.SettingsPage;

import org.springframework.boot.SpringApplication;

import com.example.SinLauncher.pages.ModsPage;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainLauncherMenu extends Application {

    private static String[] savedArgs;

    private PlayPage playPage = new PlayPage();
    private SettingsPage settingsPage = new SettingsPage();
    private ModsPage modsPage = new ModsPage();
    
    private BorderPane mainPane;

    @Override
    public void start(Stage primaryStage) throws Exception {

        HBox topSection = new HBox();
        topSection.getStyleClass().add("top-section");
        
        Label titleLabel = new Label("SinLauncher");
        titleLabel.getStyleClass().add("title-label");
        topSection.getChildren().addAll(titleLabel);
        topSection.setSpacing(20);
        topSection.setAlignment(Pos.CENTER);

        VBox leftSection = new VBox();
        leftSection.getStyleClass().add("left-section");

        Button playButton = new Button("Play");
        playButton.getStyleClass().add("nav-button");
        playButton.setOnAction(e -> mainPane.setCenter(playPage.getPlayPageContent()));

        Button settingsButton = new Button("Settings");
        settingsButton.getStyleClass().add("nav-button");
        settingsButton.setOnAction(e -> mainPane.setCenter(settingsPage.getSettingsPageContent()));

        Button modsButton = new Button("Mods");
        modsButton.getStyleClass().add("nav-button");
        modsButton.setOnAction(e -> mainPane.setCenter(modsPage.getModsPageContent()));

        leftSection.getChildren().addAll(playButton, settingsButton, modsButton);
        leftSection.setSpacing(20);

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

        mainPane = new BorderPane();
        mainPane.setTop(topSection);
        mainPane.setLeft(leftSection);
        mainPane.setBottom(bottomSection);  

        Scene scene = new Scene(mainPane, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("./styles/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("SinLauncher");
        primaryStage.show();
    }

    public static void main(String[] args) {
        savedArgs = args;

        new Thread(() -> {
            SpringApplication.run(MainLauncherMenu.class, args);
            System.out.println("Spring boot fully started");
        }).start();

        launch(args);
    }
}
