package com.thesimego.fximguruploader;

import com.thesimego.framework.jfx.tools.FxDialogs;
import com.thesimego.framework.sqlite.ORMLite;
import com.thesimego.fximguruploader.entity.ImageEN;
import com.thesimego.fximguruploader.test.HttpCookieHandler;
import com.thesimego.fximguruploader.tools.Functions;
import com.thesimego.fximguruploader.tools.Locations;
import com.thesimego.fximguruploader.views.MainWindow;
import flexjson.JSONDeserializer;
import java.awt.AWTException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author Simego
 */
public class Main extends Application {

    private MainWindow mainWindow;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        System.setProperty("java.net.useSystemProxies", "true");

        /* ==== DATABASE ==== */
        ResourceBundle bundle = ResourceBundle.getBundle("messages", new Locale("en", "US"));
//        System.out.println(bundle.getBaseBundleName());
//        System.out.println(bundle.getString("test"));

        /*
         String e = "{\"data\":{\"error\":\"Fake Error\",\"request\":\"\\/3\\/account\\/imgur\\/images.json\",\"method\":\"GET\"},\"success\":true,\"status\":\"200\"}";
         String t = "{\"data\":{\"id\":\"T4njaMC\",\"title\":\"teste dev 2\",\"description\":null,\"datetime\":1438627005,\"type\":\"image/jpeg\",\"animated\":false,\"width\":1920,\"height\":1080,\"size\":145241,\"views\":0,\"bandwidth\":0,\"vote\":null,\"favorite\":false,\"nsfw\":null,\"section\":null,\"account_url\":null,\"account_id\":9042594,\"comment_preview\":null,\"deletehash\":\"R8e8ZGwev52phEx\",\"name\":\"\",\"link\":\"http://i.imgur.com/T4njaMC.jpg\"},\"success\":true,\"status\":200}";
         String d = "{\"id\":\"T4njaMC\",\"title\":\"teste dev 2\",\"description\":null,\"datetime\":1438627005,\"type\":\"image/jpeg\",\"animated\":false,\"width\":1920,\"height\":1080,\"size\":145241,\"views\":0,\"bandwidth\":0,\"vote\":null,\"favorite\":false,\"nsfw\":null,\"section\":null,\"account_url\":null,\"account_id\":9042594,\"comment_preview\":null,\"deletehash\":\"R8e8ZGwev52phEx\",\"name\":\"\",\"link\":\"http://i.imgur.com/T4njaMC.jpg\"}";
         Basic basic = new JSONDeserializer<Basic>().use("data", ImgurImage.class).deserialize(t, Basic.class);
         System.out.println(basic);*/
//        AccessTokenEN e = AccessTokenEN.getByAccountId(9042594L);
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DAY_OF_MONTH, -3);
//        e.setLastUpdate(cal.getTime());
//        
//        try {
//            AccessTokenEN.dao.update(e);
//        } catch (SQLException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
        // Clear previous logging configurations.
        LogManager.getLogManager().reset();

        // Get the logger for "org.jnativehook" and set the level to off.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        try {
            if (!Files.exists(Paths.get(Locations.HOME))) {
                Files.createDirectory(Paths.get(Locations.HOME));
            }
            if (!Files.exists(Paths.get(Locations.IMAGE_FOLDER))) {
                Files.createDirectory(Paths.get(Locations.IMAGE_FOLDER));
                if (!Files.exists(Paths.get(Locations.THUMBNAIL_FOLDER))) {
                    Files.createDirectory(Paths.get(Locations.THUMBNAIL_FOLDER));
                }
            }
            
            CookieManager manager = new CookieManager();
            CookieHandler.setDefault(manager);

            if (Files.exists(Paths.get(Locations.COOKIE_FILE))) {
                JSONDeserializer<List<HttpCookieHandler>> deserializer = new JSONDeserializer<>();
                List<HttpCookieHandler> cookies = deserializer.use("values", HttpCookieHandler.class).deserialize(new FileReader(new File(Locations.COOKIE_FILE)));
                cookies.stream().forEach((ch) -> {
                    HttpCookie cookie = ch.getCookie();
                    if (cookie != null) {
                        try {
                            manager.getCookieStore().add(new URI(ch.getDomain()), cookie);
                        } catch (URISyntaxException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Unable to create image folder, please try again.", ex);
        }
        
        try {
            ORMLite.openConnection();
        } catch (SQLException ex) {
            String msg = "Failed connecting to the Database.";
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, msg, ex);
            if (FxDialogs.showConfirmDialog(null, "Error", msg + "\nThe program will now close.\nPlease contact the developers for help and more information.").equals(FxDialogs.OK)) {
                primaryStage.close();
            }
        }
        
        primaryStage.close();
        mainWindow = new MainWindow();
        mainWindow.show();
    }
    
    @Override
    public void stop() throws Exception {
        try {
            ORMLite.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Failed disconnecting the Database.", ex);
        }
        try {
            if (GlobalScreen.isNativeHookRegistered()) {
                GlobalScreen.removeNativeKeyListener(mainWindow.getPrintScreenListener());
                GlobalScreen.unregisterNativeHook();
            }
        } catch (NativeHookException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "There was a problem unregistering the native hook.", ex);
        }
        super.stop();
    }
    
}
