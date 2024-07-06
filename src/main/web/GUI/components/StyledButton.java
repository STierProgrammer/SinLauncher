package com.sinlauncher.app.GUI.components;

import javafx.scene.control.Button;

public class StyledButton extends Button {

    public StyledButton(String text, String styleClass) {
        super(text);
        if (styleClass != null && !styleClass.isEmpty()) {
            this.getStyleClass().add(styleClass);
        }
    }
}