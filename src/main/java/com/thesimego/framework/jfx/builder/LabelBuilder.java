package com.thesimego.framework.jfx.builder;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

/**
 *
 * @author drafaelli
 */
public class LabelBuilder {

    private final Label label;

    public LabelBuilder(String text) {
        this.label = new Label(text);
    }
    
    public static LabelBuilder $(String text) {
        LabelBuilder builder = new LabelBuilder(text);
        return builder;
    }
    
    public LabelBuilder pos(Pos pos) {
        label.setAlignment(pos);
        return this;
    }
    
    public LabelBuilder width(double width) {
        label.setPrefWidth(width);
        return this;
    }
    
    public Label get() {
       return label;
    }
    
}
