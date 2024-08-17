package com.example.SinLauncher;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class JavaFX extends Application {

    private WebView webView;
    private WebEngine webEngine;
    private BorderPane root;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        webView = new WebView();
        webEngine = webView.getEngine();
        root = new BorderPane();

        Button btnPlay = createStyledButton("Play");
        Button btnMods = createStyledButton("Mods");
        Button btnSettings = createStyledButton("Settings");
        Button btnRegister = createStyledButton("Register");
        Button btnLogin = createStyledButton("Login");

        btnPlay.setOnAction(e -> loadPage("/Pages/Play.HTML"));
        btnMods.setOnAction(e -> loadPage("/Pages/Mods.HTML"));
        btnSettings.setOnAction(e -> loadPage("/Pages/Settings.HTML"));
        btnRegister.setOnAction(e -> loadPage("/Pages/Register.HTML"));
        btnLogin.setOnAction(e -> loadPage("/Pages/Login.HTML"));

        VBox sidebar = new VBox(15, btnPlay, btnMods, btnSettings, btnRegister, btnLogin);
        sidebar.setPadding(new Insets(20)); 
        sidebar.setStyle("-fx-background-color: #20232a; -fx-pref-width: 220px;"); 

        root.setLeft(sidebar);
        root.setCenter(webView);

        loadPage("/Pages/Play.HTML");

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        scene = new Scene(root, screenWidth * 0.8, screenHeight * 0.8); 

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/assets/images/SinLauncher.png")));

        primaryStage.setScene(scene);
        primaryStage.setTitle("SinLauncher");
        primaryStage.show();
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);

        button.setStyle(
            "-fx-background-color: #61dafb;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12px 24px;" +  
            "-fx-background-radius: 8px;" +  
            "-fx-border-radius: 8px;" +
            "-fx-cursor: hand;" +
            "-fx-pref-width: 180px;"  
        );

        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #4fa3d6;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12px 24px;" +
            "-fx-background-radius: 8px;" +
            "-fx-border-radius: 8px;" +
            "-fx-cursor: hand;" +
            "-fx-pref-width: 180px;"
        ));

        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: #61dafb;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12px 24px;" +
            "-fx-background-radius: 8px;" +
            "-fx-border-radius: 8px;" +
            "-fx-cursor: hand;" +
            "-fx-pref-width: 180px;"
        ));

        return button;
    }

    private void loadPage(String pagePath) {
        webEngine.load(getClass().getResource(pagePath).toExternalForm());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
