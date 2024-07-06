package com.sinlauncher.app.GUI.components;

import javafx.scene.control.TextField;

// Testing

public class StyledTextInput extends TextField {
    public StyledTextInput(String text, String styleClass) {
        super(text);
        if (styleClass != null && !styleClass.isEmpty()) {
            this.getStyleClass().add(styleClass);
        }
    }
}
