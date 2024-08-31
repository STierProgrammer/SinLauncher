// JavaFX.java

package com.example.SinLauncher;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class JavaFX extends Application {

    private WebView webView;
    private WebEngine webEngine;
    private BorderPane root;
    private Scene scene;

    public static String username = App.currentUser;

    @Override
    public void start(Stage primaryStage) throws Exception {
        webView = new WebView();
        webEngine = webView.getEngine();

        root = new BorderPane();

        Button btnPlay = createStyledButton("Play");
        Button btnSettings = createStyledButton("Settings");
        Button btnAbout = createStyledButton("About");
        Button btnMods = createStyledButton("Mods");
        Button btnProfile = createStyledButton("Profile");
        Button btnServers = createStyledButton("Servers");

        btnPlay.setOnAction(e -> loadPage("/Pages/play.html", "/css/play.css"));
        btnSettings.setOnAction(e -> loadPage("/Pages/settings.html", "/css/settings.css"));
        btnAbout.setOnAction(e -> loadPage("/Pages/about.html", "/css/about.css"));
        btnMods.setOnAction(e -> loadPage("/Pages/mods.html", "/css/mods.css"));
        btnProfile.setOnAction(e -> loadPage("/Pages/profile.html", "/css/profile.css"));
        btnServers.setOnAction(e -> loadPage("/Pages/servers.html", "/css/servers.css"));

        VBox sidebar = new VBox(15, btnPlay, btnSettings, btnAbout, btnMods, btnProfile, btnServers);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #20232a; -fx-pref-width: 220px;");

        root.setLeft(sidebar);
        root.setCenter(webView);

        loadPage("/Pages/play.html", "/css/play.css");

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
                        "-fx-background-radius: 6px;");
        return button;
    }

    private void loadPage(String page, String css) {
        webEngine.load(getClass().getResource(page).toExternalForm());
        webEngine.setUserStyleSheetLocation(getClass().getResource(css).toExternalForm());
    
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                webEngine.executeScript("window.currentUser = '" + username + "';");
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
