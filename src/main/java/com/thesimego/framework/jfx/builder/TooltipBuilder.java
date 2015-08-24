package com.thesimego.framework.jfx.builder;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author drafaelli
 */
public class TooltipBuilder {

    /**
     * 
     * @param text
     * @param node 
     * @return  
     */
    public static Tooltip create(String text, Node node) {
        Tooltip tooltip = new Tooltip(text);
        node.setOnMouseEntered((MouseEvent event) -> {
            Point2D p = node.localToScreen(node.getLayoutBounds().getMaxX(), node.getLayoutBounds().getMaxY()); //I position the tooltip at bottom right of the node (see below for explanation)
            tooltip.show(node, p.getX(), p.getY());
        });
        node.setOnMouseExited((MouseEvent event) -> {
            tooltip.hide();
        });
        return tooltip;
    }
    
    /**
     * 
     * @param graphic
     * @param node 
     * @return  
     */
    public static Tooltip create(Node graphic, Node node) {
        Tooltip tooltip = new Tooltip();
        tooltip.setGraphic(graphic);
        node.setOnMouseEntered((MouseEvent event) -> {
            Point2D p = node.localToScreen(node.getLayoutBounds().getMaxX(), node.getLayoutBounds().getMaxY()); //I position the tooltip at bottom right of the node (see below for explanation)
            tooltip.show(node, p.getX(), p.getY());
        });
        node.setOnMouseExited((MouseEvent event) -> {
            tooltip.hide();
        });
        return tooltip;
    }
    
    /**
     * 
     * @param node
     * @return 
     */
    public static Tooltip create(Node node) {
        Tooltip tooltip = new Tooltip();
        node.setOnMouseEntered((MouseEvent event) -> {
            Point2D p = node.localToScreen(node.getLayoutBounds().getMaxX()/2, node.getLayoutBounds().getMaxY()+5); //I position the tooltip at bottom right of the node (see below for explanation)
            tooltip.show(node, p.getX(), p.getY());
        });
        node.setOnMouseExited((MouseEvent event) -> {
            tooltip.hide();
        });
        return tooltip;
    }

}
