package com.thesimego.framework.jfx.components;

import eu.hansolo.enzo.onoffswitch.OnOffSwitch;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 *
 * @author drafaelli
 */
public class TSwitch extends VBox {

    private final OnOffSwitch onOffSwitch;
    private final Text text;

    public TSwitch(String text) {
        super(5);
        setPadding(new Insets(5, 5, 5, 5));
        this.text = new Text(text);
        this.text.setFill(Color.WHITE);
        this.onOffSwitch = new OnOffSwitch();
        this.onOffSwitch.setPadding(new Insets(0, 2, 2, 5));
        getStyleClass().add("toolbar-button-custom");
        getChildren().addAll(this.text, this.onOffSwitch);
        setOnMouseEntered((MouseEvent event) -> {
            setStyle("-fx-background-color: -imgur-color-hover;");
        });
        setOnMouseExited((MouseEvent event) -> {
            setStyle("-fx-background-color: transparent;");
        });
        setOnMouseClicked((MouseEvent event) -> {
            this.onOffSwitch.setSelected(!this.onOffSwitch.isSelected());
        });
    }
    
    public OnOffSwitch getSwitch() {
        return onOffSwitch;
    }

    public Text getText() {
        return text;
    }
    
}
