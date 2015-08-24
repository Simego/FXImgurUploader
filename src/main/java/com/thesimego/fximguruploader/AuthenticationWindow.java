package com.thesimego.fximguruploader;

import com.thesimego.fximguruploader.entity.AccessTokenEN;
import com.thesimego.fximguruploader.tools.Functions;
import com.thesimego.fximguruploader.tools.ImgurClient;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.MapTransformer;
import java.util.Map;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 *
 * @author Simego
 */
public class AuthenticationWindow extends Stage {

    public AuthenticationWindow(Stage owner, Callback<AccessTokenEN, Void> callback) {
        initOwner(owner);
        initStyle(StageStyle.UTILITY);
        initModality(Modality.APPLICATION_MODAL);
        centerOnScreen();
        
        final VBox vbox = new VBox(2);
        final Scene scene = new Scene(vbox);
        setScene(scene);
        
//        setMaximized(true);
        double dW = 850;
        double dH = 650;
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        double fW = screen.getWidth();
        double fH = screen.getHeight();
        setWidth(fW < dW ? fW * 0.8 : dW);
        setHeight(fH < dH ? fH * 0.8 : dH);

//        final TextField addressField = new TextField();
//        addressField.setEditable(false);
        
        final WebView webView = new WebView();
        webView.prefWidthProperty().bind(widthProperty());
        webView.prefHeightProperty().bind(heightProperty());
        webView.setContextMenuEnabled(true);
        webView.setCache(true);
        
        vbox.getChildren().addAll(/*addressField, */webView);

        String requestUrl = "https://api.imgur.com/oauth2/authorize?client_id={0}&response_type={1}&state={2}";
        requestUrl = requestUrl.replace("{0}", ImgurClient.CLIENT_ID);
        requestUrl = requestUrl.replace("{1}", "token");
        requestUrl = requestUrl.replace("{2}", "desktop_auth");
        final WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
//        addressField.textProperty().bind(webEngine.locationProperty());
        
        webEngine.getLoadWorker().stateProperty().addListener((ObservableValue<? extends State> observableValue, State state, State newState) -> {
            if (newState.equals(State.SUCCEEDED) && webEngine.getLocation().contains("thesimego.com")) {
                Map<String, String> params = Functions.splitQuery(webEngine.getLocation());
                JSONSerializer serializer = new JSONSerializer().transform(new MapTransformer(), AccessTokenEN.class);
                String json = serializer.serialize(params);
//                System.out.println(webEngine.getLocation());
                System.out.println(json);
                JSONDeserializer<AccessTokenEN> deserializer = new JSONDeserializer<>();
                AccessTokenEN accessToken = deserializer.deserialize(json, AccessTokenEN.class);
                callback.call(accessToken);
                
                // Cookie save
                Functions.saveCookies();
                
                close();
            } 
        });
        webEngine.load(requestUrl);
        webView.requestFocus();
    }
    
}
