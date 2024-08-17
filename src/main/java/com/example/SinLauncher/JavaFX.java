package com.example.SinLauncher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

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

        Button btnPlay = new Button("Play");
        Button btnMods = new Button("Mods");
        Button btnSettings = new Button("Settings");
        Button btnRegister = new Button("Register");
        Button btnLogin = new Button("Login");

        btnPlay.setOnAction(e -> loadPage("/Pages/Play.HTML"));
        btnMods.setOnAction(e -> loadPage("/Pages/Mods.HTML"));
        btnSettings.setOnAction(e -> loadPage("/Pages/Settings.HTML"));
        btnRegister.setOnAction(e -> loadPage("/Pages/Register.HTML"));
        btnLogin.setOnAction(e -> loadPage("/Pages/Login.HTML"));

        VBox sidebar = new VBox(10, btnPlay, btnMods, btnSettings, btnRegister, btnLogin);
        sidebar.setStyle("-fx-background-color: #20232a; -fx-padding: 20px; -fx-spacing: 10px; -fx-pref-width: 200px;");
        
        root.setLeft(sidebar);
        root.setCenter(webView);

        loadPage("/Pages/Play.HTML");

        scene = new Scene(root, 1920, 1080);
        primaryStage.setScene(scene);
        primaryStage.setTitle("SinLauncher");
        primaryStage.show();
    }

    private void loadPage(String pagePath) {
        webEngine.load(getClass().getResource(pagePath).toExternalForm());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
