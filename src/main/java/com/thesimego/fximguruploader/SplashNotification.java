package com.thesimego.fximguruploader;

import javafx.application.Preloader;

/**
 *
 * @author drafaelli
 */
public class SplashNotification implements Preloader.PreloaderNotification {

    private final double progress;
    private final String message;
    
    public SplashNotification(double progress, String message) {
        this.progress = progress;
        this.message = message;
    }

    public double getProgress() {
        return progress;
    }

    public String getMessage() {
        return message;
    }
    
}
