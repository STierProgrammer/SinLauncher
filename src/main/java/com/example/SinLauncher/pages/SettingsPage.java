package com.example.SinLauncher.pages;

import javafx.scene.layout.VBox;

public class SettingsPage {

    public VBox getSettingsPageContent() {
        VBox settingsContent = new VBox();
        settingsContent.getStyleClass().add("settings-content");

        return settingsContent;
    }
}
