package com.thesimego.framework.jfx.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 *
 * @author drafaelli
 */
public class GroupBox extends StackPane {

    public GroupBox(String titleString, Node content) {
        getStylesheets().add("css/groupbox.css");
        
        Label title = new Label(" " + titleString + " ");
        title.getStyleClass().add("groupbox-title");
        StackPane.setAlignment(title, Pos.TOP_LEFT);

        StackPane contentPane = new StackPane();
        content.getStyleClass().add("groupbox-content");
        contentPane.getChildren().add(content);

        getStyleClass().add("groupbox-border");
        getChildren().addAll(title, contentPane);
    }
    
}
