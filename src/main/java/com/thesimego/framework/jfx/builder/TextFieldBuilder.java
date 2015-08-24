package com.thesimego.framework.jfx.builder;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 *
 * @author drafaelli
 */
public class TextFieldBuilder {

    private final TextField textField;

    public TextFieldBuilder() {
        this.textField = new TextField();
    }
    
    public static TextFieldBuilder $() {
        TextFieldBuilder builder = new TextFieldBuilder();
        return builder;
    }
    
    public TextField get() {
        return textField;
    }
    
    public TextFieldBuilder maxlength(Integer maxlength) {
        if(maxlength != null) {
            textField.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                if (newValue.intValue() > oldValue.intValue()) {
                    if (textField.getText().length() >= maxlength) {
                        textField.setText(textField.getText().substring(0, maxlength));
                    }
                }
            });
        }
        return this;
    }
    
}
