package com.thesimego.fximguruploader;

import com.j256.ormlite.stmt.PreparedQuery;
import com.thesimego.framework.jfx.builder.TooltipBuilder;
import com.thesimego.framework.jfx.builder.TableBuilder;
import com.thesimego.framework.jfx.components.GroupBox;
import com.thesimego.framework.sqlite.ORMLite;
import com.thesimego.fximguruploader.entity.AccessTokenEN;
import com.thesimego.fximguruploader.entity.AlbumEN;
import com.thesimego.fximguruploader.entity.ImageEN;
import com.thesimego.fximguruploader.entity.PreferencesEN;
import com.thesimego.fximguruploader.entity.imgur.Basic;
import com.thesimego.fximguruploader.test.HttpCookieHandler;
import com.thesimego.fximguruploader.tools.Functions;
import com.thesimego.fximguruploader.tools.Locations;
import com.thesimego.fximguruploader.tools.ImgurClient.ImgurV3Image;
import com.thesimego.fximguruploader.tools.ImgurClient.ImgurV3ImageRequest;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import flexjson.JSONDeserializer;
import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author Simego
 */
public class Main extends Application {

    private Button startButton;
    private Button stopButton;
    private Button minimizeButton;
    private Button closeButton;
    private Button signInButton;
    private Button loggedButton;
    private Button uploadButton;
    private TextArea console;
    private GroupBox consoleBox;
    private GroupBox imageBox;
    private GroupBox albumBox;
    private HBox loginBox;
    private CheckBox checkBoxOpenLink;

    private TableView<ImageEN> imageTable;
    private TableView<AlbumEN> albumTable;

    private NativeKeyListener printScreenListener;

    private double xOffset = 0;
    private double yOffset = 0;

    private Stage primaryStage;
    private CookieManager manager;

    private AccessTokenEN loggedUser;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        System.setProperty("java.net.useSystemProxies", "true");
        
        /* ==== DATABASE ==== */
        try {
            ORMLite.openConnection();
        } catch (SQLException ex) {
            String msg = "Failed connecting to the Database.";
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, msg, ex);
            if (showErrorDialog(msg, "The program will now close.\nPlease contact the developers for help and more information.") == Dialog.ACTION_OK) {
                primaryStage.close();
            }
        }

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

            manager = new CookieManager();
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

        Platform.runLater(() -> {
            populate();
        });

        StackPane root = new StackPane();
        root.getStyleClass().add("main-borderpane");

        BorderPane borderPane = new BorderPane();
        root.getChildren().add(borderPane);
        borderPane.getStyleClass().add("main-borderpane");

        VBox center = new VBox();
        center.getStyleClass().add("center-pane");

        HBox upperCenter = new HBox(getConsoleBox(), getAlbumBox());
        upperCenter.getStyleClass().add("upper-center-pane");

        // ===
        BorderPane toolBarPane = new BorderPane();
        toolBarPane.getStyleClass().add("toolbar-custom-pane");

        ToolBar toolBarLeft = new ToolBar();
        toolBarLeft.getItems().addAll(getStartButton(), getStopButton(), getUploadButton());
//        ToolBar toolBarCenter = new ToolBar();
//        toolBarCenter.getItems().addAll(getLoginBox());
        ToolBar toolBarRight = new ToolBar();
        toolBarRight.getItems().addAll(getLoginBox(), getMinimizeButton(), getCloseButton());

//        toolBarLeft.setPrefHeight(60);
//        toolBarRight.setPrefHeight(60);
        toolBarLeft.setOnMousePressed((MouseEvent event) -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });
        toolBarLeft.setOnMouseDragged((MouseEvent event) -> {
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
        });
//        toolBarCenter.setOnMousePressed((MouseEvent event) -> {
//            xOffset = primaryStage.getX() - event.getScreenX();
//            yOffset = primaryStage.getY() - event.getScreenY();
//        });
//        toolBarCenter.setOnMouseDragged((MouseEvent event) -> {
//            primaryStage.setX(event.getScreenX() + xOffset);
//            primaryStage.setY(event.getScreenY() + yOffset);
//        });
        toolBarRight.setOnMousePressed((MouseEvent event) -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });
        toolBarRight.setOnMouseDragged((MouseEvent event) -> {
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
        });

        toolBarPane.setCenter(toolBarLeft);
//        toolBarPane.setCenter(toolBarCenter);
        toolBarPane.setRight(toolBarRight);

//        BorderPane.setAlignment(toolBarLeft, Pos.BASELINE_LEFT);
        borderPane.setTop(toolBarPane);

        center.getChildren().addAll(
                upperCenter,
                getImageBox()
        );
        borderPane.setCenter(center);

        /* ================= */
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add("css/styles.css");

        primaryStage.setScene(scene);
        primaryStage.setTitle("ImgurUploader");
        primaryStage.show();
        primaryStage.setHeight(root.getHeight());
        primaryStage.setWidth(850);
//        primaryStage.setHeight(637);
//        ResizeHelper.addResizeListener(primaryStage);
//        primaryStage.setResizable(false);
        /* ================= */

//        populate();
    }

    private void populate() {
        try {
            // Image
            PreparedQuery<ImageEN> qb = ImageEN.dao.queryBuilder().orderBy(ImageEN.DATE, false).prepare();
            getImageTable().getItems().addAll(ImageEN.dao.query(qb));

//            TableBuilder.buildTooltipsAfterPopulate(getImageTable());
            if (PreferencesEN.dao.countOf() == 0) {
                PreferencesEN preference = new PreferencesEN();
                PreferencesEN.dao.create(preference);
            } else {
                PreferencesEN preference = PreferencesEN.get();
                if (preference.getLoggedAccountId() != null) {
                    PreparedQuery query = AccessTokenEN.dao.queryBuilder().where().eq(AccessTokenEN.ACCOUNT_ID, preference.getLoggedAccountId()).prepare();
                    loggedUser = AccessTokenEN.dao.queryForFirst(query);

                    updateLoggedButton(loggedUser);
                }
            }

            // Album
        } catch (SQLException ex) {
            String msg = "An error ocurred loading data from database.";
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, msg, ex);
            showErrorDialog(msg, "Please if the error persist, try deleting the local database (imguruploader.db). (data will be lost)\nOr try contacting the developer.");
        }
    }

    public Button getStartButton() {
        if (startButton == null) {
            startButton = GlyphsDude.createIconButton(FontAwesomeIcon.PLAY, "", "28", "0", ContentDisplay.CENTER);
            startButton.getStyleClass().addAll("toolbar-button-transparent", "start", "dropshadow-1-5");
            TooltipBuilder.create("Start Capture", startButton);
            startButton.setOnAction((ActionEvent event) -> {
                getStartButton().setDisable(true);
                getStopButton().setDisable(false);

                try {
                    GlobalScreen.addNativeKeyListener(getPrintScreenListener());
                    GlobalScreen.registerNativeHook();
                } catch (NativeHookException ex) {
                    showErrorDialog("There was a problem registering the native hook.");
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "There was a problem registering the native hook.", ex);
                }

            });
        }
        return startButton;
    }

    public Button getStopButton() {
        if (stopButton == null) {
            stopButton = GlyphsDude.createIconButton(FontAwesomeIcon.STOP, "", "28", "0", ContentDisplay.CENTER);
            stopButton.getStyleClass().addAll("toolbar-button-transparent", "stop", "dropshadow-1-5");
            TooltipBuilder.create("Stop Capture", stopButton);
            stopButton.setDisable(true);
            stopButton.setOnAction((ActionEvent event) -> {
                getStopButton().setDisable(true);
                getStartButton().setDisable(false);

                try {
                    GlobalScreen.removeNativeKeyListener(getPrintScreenListener());
                    GlobalScreen.unregisterNativeHook();
                } catch (NativeHookException ex) {
                    showErrorDialog("There was a problem registering the native hook.");
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "There was a problem registering the native hook.", ex);
                }
            });
        }
        return stopButton;
    }

    public Button getMinimizeButton() {
        if (minimizeButton == null) {
            minimizeButton = GlyphsDude.createIconButton(FontAwesomeIcon.MINUS, "", "28", "0", ContentDisplay.CENTER);
            minimizeButton.getStyleClass().addAll("toolbar-button-transparent", "default-green-button");
            TooltipBuilder.create("Minimize", minimizeButton);
            minimizeButton.setOnAction((ActionEvent event) -> {
                primaryStage.setIconified(true);
            });
        }
        return minimizeButton;
    }

    public Button getCloseButton() {
        if (closeButton == null) {
            closeButton = GlyphsDude.createIconButton(FontAwesomeIcon.POWER_OFF, "", "28", "0", ContentDisplay.CENTER);
            closeButton.getStyleClass().addAll("toolbar-button-transparent", "default-green-button");
            TooltipBuilder.create("Close", closeButton);
            closeButton.setOnAction((ActionEvent event) -> {
                primaryStage.close();
            });
        }
        return closeButton;
    }

    public TableView<ImageEN> getImageTable() {
        if (imageTable == null) {
            imageTable = TableBuilder
                    .$()
                    .column(ImageEN.LINK, "Link", 200, "Image not uploaded yet")
                    .column(ImageEN.TITLE, "Title", 350, (Callback<ImageEN, Boolean>) entity -> {
                        return entity != null && entity.getLink() == null;
                    }, 128)
                    .dateColumn(ImageEN.DATE, "Date", 170)
                    .useTooltipEvents()
                    .onClick((EventHandler<MouseEvent>) event -> {
                        if (event.getClickCount() == 2) {
                            ImageEN entity = imageTable.getSelectionModel().getSelectedItem();
                            // Ignora se for nulo ou não tiver link
                            if (entity == null || entity.getLink() == null) {
                                return;
                            }
                            // Inicia no browser ou copia o link
                            if (getCheckBoxOpenLink().isSelected()) {
                                try {
                                    Desktop.getDesktop().browse(new URI(entity.getLink()));
                                } catch (URISyntaxException | IOException ex) {
                                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                final Clipboard clipboard = Clipboard.getSystemClipboard();
                                final ClipboardContent content = new ClipboardContent();
                                content.put(DataFormat.PLAIN_TEXT, entity.getLink());
                                clipboard.setContent(content);
                            }
                        }
                    })
                    .get();
            imageTable.setPrefHeight(300);
            imageTable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ImageEN> observable, ImageEN oldValue, ImageEN newValue) -> {
                updateUploadButton();
            });
        }
        return imageTable;
    }

    public TableView<AlbumEN> getAlbumTable() {
        if (albumTable == null) {
            albumTable = TableBuilder
                    .$()
                    .column("link", "Link", 200)
                    .column("title", "Title", 170)
                    .get();
            albumTable.setPrefHeight(200);
            albumTable.prefWidthProperty().bind(getImageTable().widthProperty().subtract(getConsole().widthProperty()));
        }
        return albumTable;
    }

    public TextArea getConsole() {
        if (console == null) {
            console = new TextArea();
            console.setEditable(false);
            console.setWrapText(true);
            console.prefWidthProperty().bind(getImageTable().widthProperty().subtract(getAlbumTable().widthProperty()));
        }
        return console;
    }

    public GroupBox getImageBox() {
        if (imageBox == null) {
            imageBox = new GroupBox("Images", getImageTable());
        }
        return imageBox;
    }

    public GroupBox getAlbumBox() {
        if (albumBox == null) {
            albumBox = new GroupBox("Albums", getAlbumTable());
        }
        return albumBox;
    }

    public GroupBox getConsoleBox() {
        if (consoleBox == null) {
            consoleBox = new GroupBox("Console", getConsole());
        }
        return consoleBox;
    }

    public HBox getLoginBox() {
        if (loginBox == null) {
            loginBox = new HBox(10, getSignInButton());
        }
        return loginBox;
    }

    public Button getSignInButton() {
        if (signInButton == null) {
            signInButton = new Button("sign in");
            signInButton.getStyleClass().addAll("toolbar-button-transparent", "default-green-button");
            signInButton.setOnAction((ActionEvent event) -> {
                new AuthenticationWindow(primaryStage, (AccessTokenEN accessToken) -> {
                    doLogin(accessToken);
                    return null;
                }).showAndWait();
            });
        }
        return signInButton;
    }

    public Button getLoggedButton() {
        if (loggedButton == null) {
            loggedButton = new Button();
            loggedButton.getStyleClass().addAll("toolbar-button-transparent", "default-green-button");
            final MenuItem logoutMenu = new MenuItem("Logout");
            logoutMenu.setOnAction((ActionEvent event) -> {
                try {
                    PreferencesEN preference = PreferencesEN.get();
                    preference.setLoggedAccountId(null);
                    PreferencesEN.dao.update(preference);

//                    Files.delete(Paths.get(Locations.COOKIE_FILE));
                    List<HttpCookie> toRemoveList = new ArrayList<>();
                    manager.getCookieStore().getCookies().stream().forEach((cookie) -> {
                        if (cookie.getDomain().contains("imgur") /*|| cookie.getName().contains("ACCOUNT_CHOOSER") || cookie.getName().contains("GALX")*/ || cookie.getName().contains("LSID") /*|| cookie.getName().contains("GAPS")*/) {
                            toRemoveList.add(cookie);
                        }
                    });
                    for (HttpCookie cookie : toRemoveList) {
                        manager.getCookieStore().remove(new URI(cookie.getDomain()), cookie);
                    }

                    // Cookie save
                    Functions.saveCookies();

                    loggedUser = null;
                    updateLoggedButton(null);
                } catch (SQLException | URISyntaxException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            final MenuItem browser = new MenuItem("Open in browser");
            browser.setOnAction((ActionEvent event) -> {
                try {
                    Desktop.getDesktop().browse(new URI(loggedUser.getAccountLink()));
                } catch (URISyntaxException | IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            CustomMenuItem cboxMenuItem = new CustomMenuItem(getCheckBoxOpenLink(), false);
            cboxMenuItem.setOnAction((ActionEvent event) -> {
                Arrays.toString(cboxMenuItem.getStyleClass().toArray());
            });
            final ContextMenu contextMenu = new ContextMenu(new CustomMenuItem(getCheckBoxOpenLink(), false), browser, logoutMenu);
            contextMenu.getStyleClass().add("context-menu-user");

            primaryStage.getScene().addEventFilter(MouseEvent.MOUSE_MOVED, (MouseEvent event) -> {
//                System.out.println(String.format("X: %f - Y: %f", event.getSceneX(), event.getSceneY()));
                Bounds buttonBounds = getLoggedButton().localToScene(getLoggedButton().getLayoutBounds());
//                Bounds popupBounds = contextMenu.getScene().getRoot().localToParent(contextMenu.getScene().getRoot().getLayoutBounds());
                if (buttonBounds.contains(event.getSceneX(), event.getSceneY()) /*|| popupBounds.contains(event.getSceneX(), event.getSceneY())*/) {
//                    System.out.println("contains");
                    if (!contextMenu.isShowing()) {
                        Point2D p2d = loggedButton.localToScreen(loggedButton.getLayoutBounds().getMaxX() - loggedButton.getWidth(), loggedButton.getLayoutBounds().getMaxY());
                        contextMenu.show(primaryStage, p2d.getX(), p2d.getY());
                    }
                } else if (contextMenu.isShowing()) {
                    contextMenu.hide();
                }
//                if(event.getTarget() instanceof Node) {
//                    System.out.println(Arrays.toString(((Node) event.getTarget()).getStyleClass().toArray()));
//                }
            });
        }
        return loggedButton;
    }

    public CheckBox getCheckBoxOpenLink() {
        if (checkBoxOpenLink == null) {
            checkBoxOpenLink = new CheckBox("On - Open in Browser\nOff - Copy to Clipboard");
            checkBoxOpenLink.setStyle("-fx-cursor: hand;");
        }
        return checkBoxOpenLink;
    }

    public Button getUploadButton() {
        if (uploadButton == null) {
            uploadButton = GlyphsDude.createIconButton(FontAwesomeIcon.CLOUD_UPLOAD, "upload image", "24", "15", ContentDisplay.LEFT);
            uploadButton.getStyleClass().add("upload-button");
            uploadButton.setDisable(true);
//            uploadButton.disableProperty().bind(getImageTable().getSelectionModel().selectedItemProperty().isNull());
            uploadButton.setOnAction((ActionEvent event) -> {
                ImageEN image = getImageTable().getSelectionModel().getSelectedItem();
                if (image == null) {
                    return;
                }

                new Thread(() -> {
                    try {
                        ImgurV3Image.execute(ImgurV3ImageRequest.UPLOAD, image, null, (Basic param) -> {
                            Platform.runLater(() -> {
                                // Passar info para console
                                if (param.getSuccess()) {
                                    Dialogs.create().lightweight().styleClass(Dialog.STYLE_CLASS_CROSS_PLATFORM).title("Upload").masthead("Your image has been uploaded.").showInformation();
                                } else {
                                    showErrorDialog("There was a problem uploading your image, please try again.");
                                }
                                getImageTable().getColumns().get(0).setVisible(false);
                                getImageTable().getColumns().get(0).setVisible(true);
                                updateUploadButton();
                            });
                            return null;
                        });
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        Platform.runLater(() -> {
                            showErrorDialog(ex.getMessage());
                        });
                    }
                }).start();
            });
        }
        return uploadButton;
    }

    private void doLogin(AccessTokenEN accessToken) {
        try {
            PreparedQuery query = AccessTokenEN.dao.queryBuilder().where().eq(AccessTokenEN.ACCOUNT_ID, accessToken.getAccountId()).prepare();
            AccessTokenEN foundToken = AccessTokenEN.dao.queryForFirst(query);

            if (foundToken == null) {
                accessToken.setLastUpdate(new Date());
                AccessTokenEN.dao.create(accessToken);
                loggedUser = accessToken;
            } else {
                foundToken.setAccessToken(accessToken.getAccessToken());
                foundToken.setExpiresIn(accessToken.getExpiresIn());
                foundToken.setRefreshToken(accessToken.getRefreshToken());
                foundToken.setTokenType(accessToken.getTokenType());
                foundToken.setAccountUsername(accessToken.getAccountUsername());
                foundToken.setLastUpdate(new Date());
                AccessTokenEN.dao.update(foundToken);
                loggedUser = foundToken;
            }

            PreferencesEN preference = PreferencesEN.get();
            preference.setLoggedAccountId(loggedUser.getAccountId());
            PreferencesEN.dao.update(preference);

            updateLoggedButton(loggedUser);
        } catch (SQLException ex) {
            String msg = "There was a problem validating your access token, please try again.";
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, msg, ex);
        }
    }

    private void updateLoggedButton(AccessTokenEN user) {
        if (user == null) {
            getLoggedButton().setText(null);
            getLoginBox().getChildren().remove(getLoggedButton());
            getLoginBox().getChildren().add(getSignInButton());
        } else {
            getLoggedButton().setText(user.getAccountUsername());
            getLoginBox().getChildren().remove(getSignInButton());
            getLoginBox().getChildren().add(getLoggedButton());
        }
        getLoginBox().requestLayout();
        updateUploadButton();
    }

    private void updateUploadButton() {
        ImageEN image = getImageTable().getSelectionModel().getSelectedItem();
        if (image == null || loggedUser == null) {
            getUploadButton().setDisable(true);
        } else {
            getUploadButton().setDisable(image.getLink() != null);
        }
    }

    public void showErrorDialog(String message) {
        Dialogs.create().lightweight().title("Error").message(message).owner(primaryStage).styleClass(Dialog.STYLE_CLASS_UNDECORATED).showError();
    }

    public Action showErrorDialog(String masthead, String message) {
        return Dialogs.create().title("Error").masthead(masthead).message(message).owner(primaryStage == null ? null : primaryStage).styleClass(primaryStage == null ? Dialog.STYLE_CLASS_CROSS_PLATFORM : Dialog.STYLE_CLASS_UNDECORATED).showError();
    }

    private NativeKeyListener getPrintScreenListener() {
        if (printScreenListener == null) {
            printScreenListener = new NativeKeyListener() {
                @Override
                public void nativeKeyReleased(NativeKeyEvent nke) {
//                    System.out.println("Key Pressed: " + nke.paramString());
                    if (nke.getKeyCode() == NativeKeyEvent.VC_PRINTSCREEN) {
//                        System.out.println("Screen Printed!");
                        try {
                            String filename = new SimpleDateFormat("yyyy-MM-ddHH-mm-ss").format(new Date()) + ".png";
                            BufferedImage bi = Functions.getScreenBufferedImage();
                            File outputfile = new File(Locations.image(filename));
                            ImageIO.write(bi, "png", outputfile);

                            File outputfileThumbnail = new File(Locations.thumbnail(filename));
                            Thumbnails
                                    .of(bi)
                                    .size(150, 150)
                                    .outputFormat("png")
                                    .toFile(outputfileThumbnail);
                            ImageEN image = new ImageEN();
                            image.setFilename(filename);
                            image.setDate(new Date());

                            ImageEN.dao.create(image);
                            getImageTable().getItems().add(0, image);
//                                    getImageTable().requestLayout();

                        } catch (AWTException | IOException | SQLException ex) {
                            String msg = "There was a problem saving the screenshot.";
                            showErrorDialog(msg, "Please try again, maybe restarting the application will solve the issue.");
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, msg, ex);
                        }
                    }
                }

                @Override
                public void nativeKeyPressed(NativeKeyEvent nke) {
//                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Locations | Templates.
                }

                @Override
                public void nativeKeyTyped(NativeKeyEvent nke) {
//                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Locations | Templates.
                }
            };
        }
        return printScreenListener;
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
                GlobalScreen.removeNativeKeyListener(getPrintScreenListener());
                GlobalScreen.unregisterNativeHook();
            }
        } catch (NativeHookException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "There was a problem unregistering the native hook.", ex);
        }
        super.stop();
    }

}
