/*
 * Copyright 2015 TheSimego.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thesimego.fximguruploader;

import javafx.application.Preloader;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Simple Preloader Using the ProgressBar Control
 *
 * @author drafaelli
 */
public class SplashPreloader extends Preloader {
    
    private Stage stage;
    private VBox splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private ImageView splash;
    private static double SPLASH_WIDTH = 400;
    private static final int SPLASH_HEIGHT = 230;
    public static final String SPLASH_IMAGE = "img/logo-dark.png";
    
    private Scene createPreloaderScene() {
        splash = new ImageView(new Image(SPLASH_IMAGE));
        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(SPLASH_WIDTH);
        progressText = new Label();
        progressText.setAlignment(Pos.CENTER);
        progressText.setStyle("-fx-font-weight: bold; -fx-fill: rgb(43,43,43);");
        splashLayout = new VBox(10);
        splashLayout.getChildren().addAll(splash, loadProgress, progressText);
        splashLayout.setAlignment(Pos.CENTER);
        splashLayout.setPadding(new Insets(10, 5, 5, 5));
        splash.setEffect(new DropShadow());
        progressText.setEffect(new DropShadow(4, Color.rgb(133, 191, 37)));
        Scene scene = new Scene(splashLayout, SPLASH_WIDTH, SPLASH_HEIGHT, Color.TRANSPARENT);
        scene.getStylesheets().add("css/styles.css");
        return scene;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.initStyle(StageStyle.TRANSPARENT);
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        System.setProperty("java.net.useSystemProxies", "true");
//        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(createPreloaderScene());     
        stage.show();
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        super.handleApplicationNotification(info);
        if(info instanceof SplashNotification) {
            SplashNotification sn = (SplashNotification)info;
            loadProgress.setProgress(sn.getProgress());
            progressText.setText(sn.getMessage());
            
            if(sn.getProgress() >= 1) {
                stage.hide();
            }
        }
    }
    
}
