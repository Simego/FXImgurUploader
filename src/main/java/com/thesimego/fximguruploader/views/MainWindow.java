package com.thesimego.fximguruploader.views;

import com.thesimego.fximguruploader.Main;
import com.j256.ormlite.stmt.PreparedQuery;
import com.thesimego.framework.jfx.BaseWindow;
import com.thesimego.framework.jfx.builder.TableBuilder;
import com.thesimego.framework.jfx.builder.TooltipBuilder;
import com.thesimego.framework.jfx.components.GroupBox;
import com.thesimego.framework.jfx.tools.FxDialogs;
import com.thesimego.fximguruploader.entity.AccessTokenEN;
import com.thesimego.fximguruploader.entity.AlbumEN;
import com.thesimego.fximguruploader.entity.ImageEN;
import com.thesimego.fximguruploader.entity.PreferencesEN;
import com.thesimego.fximguruploader.entity.imgur.Basic;
import com.thesimego.fximguruploader.tools.Functions;
import com.thesimego.fximguruploader.tools.ImgurClient;
import com.thesimego.fximguruploader.tools.Locations;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javax.imageio.ImageIO;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author drafaelli
 */
public final class MainWindow extends BaseWindow {

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
    private StackPane lockPane;

    private TableView<ImageEN> imageTable;
    private TableView<AlbumEN> albumTable;

    private double xOffset = 0;
    private double yOffset = 0;

    private CookieManager manager;

    private AccessTokenEN loggedUser;

    public MainWindow() {
        initStyle(StageStyle.TRANSPARENT);
        
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

        BorderPane toolBarPane = new BorderPane();
        toolBarPane.getStyleClass().add("toolbar-custom-pane");

        ToolBar toolBarLeft = new ToolBar();
        toolBarLeft.getItems().addAll(getStartButton(), getStopButton(), getUploadButton());

        ToolBar toolBarRight = new ToolBar();
        toolBarRight.getItems().addAll(getLoginBox(), getMinimizeButton(), getCloseButton());

        toolBarLeft.setOnMousePressed((MouseEvent event) -> {
            xOffset = this.getX() - event.getScreenX();
            yOffset = this.getY() - event.getScreenY();
        });
        toolBarLeft.setOnMouseDragged((MouseEvent event) -> {
            this.setX(event.getScreenX() + xOffset);
            this.setY(event.getScreenY() + yOffset);
        });

        toolBarRight.setOnMousePressed((MouseEvent event) -> {
            xOffset = this.getX() - event.getScreenX();
            yOffset = this.getY() - event.getScreenY();
        });
        toolBarRight.setOnMouseDragged((MouseEvent event) -> {
            this.setX(event.getScreenX() + xOffset);
            this.setY(event.getScreenY() + yOffset);
        });

        toolBarPane.setCenter(toolBarLeft);
        toolBarPane.setRight(toolBarRight);

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

        this.setScene(scene);
        this.setTitle("ImgurUploader");
        this.getIcons().add(new Image("img/icon.png"));
        this.show();
        this.setHeight(root.getHeight());
        this.setWidth(850);

//        this.setHeight(637);
//        ResizeHelper.addResizeListener(this);
//        this.setResizable(false);
        
        getLockPane(); // load 'loading' gif

        // Enable Drag & Drop
        scene.setOnDragOver((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles() || db.hasUrl() || db.hasImage()) {
                event.acceptTransferModes(TransferMode.ANY);
            } else {
                event.consume();
            }
        });

        // Dropping over surface
        scene.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                for (File file : db.getFiles()) {
                    String filePath = file.getAbsolutePath();
                    if (file.getName().matches(".*\\.(jpg|jpeg|png|bmp|)")) {
                        Image image = new Image(file.toURI().toString());
                        try {
                            String split[] = filePath.split("\\.");
                            String extension = split[split.length - 1];
                            if (image.getException() != null) {
                                FxDialogs.showErrorDialog(image.getException().getMessage());
                            }
                            ImageEN imageEN = ImageEN.createImage(SwingFXUtils.fromFXImage(image, null), extension);
                            getImageTable().getItems().add(0, imageEN);
                        } catch (SQLException | IOException ex) {
                            String msg = "There was a problem saving the image.";
                            FxDialogs.showErrorDialog(msg, "Please try again, maybe restarting the application will solve the issue.");
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, msg, ex);
                        }
                    }
                }
                success = true;
            } else if (db.hasUrl()) {
                BufferedImage image;
                try {
                    URL url = new URL(db.getUrl());
                    URLConnection conn = url.openConnection();
                    String extension = conn.getContentType().split("/")[1];
                    image = ImageIO.read(url);
                    ImageEN imageEN = ImageEN.createImage(image, extension);
                    getImageTable().getItems().add(0, imageEN);
                } catch (SQLException | IOException ex) {
                    String msg = "There was a problem saving the image from the URL.";
                    FxDialogs.showErrorDialog(msg, "Please try again, maybe restarting the application will solve the issue.");
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, msg, ex);
                }
                success = true;
            } else if (db.hasImage()) {
                Image image = db.getImage();
                try {
                    ImageEN imageEN = ImageEN.createImage(SwingFXUtils.fromFXImage(image, null), "png");
                    getImageTable().getItems().add(0, imageEN);
                } catch (SQLException | IOException ex) {
                    String msg = "There was a problem saving the image.";
                    FxDialogs.showErrorDialog(msg, "Please try again, maybe restarting the application will solve the issue.");
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, msg, ex);
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }
    
    /**
     * 
     */
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
            FxDialogs.showErrorDialog(msg, "Please if the error persist, try deleting the local database (imguruploader.db). (data will be lost)\nOr try contacting the developer.");
        }
    }

    /**
     * 
     * @return 
     */
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
                    FxDialogs.showErrorDialog("There was a problem registering the native hook.");
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "There was a problem registering the native hook.", ex);
                }

            });
        }
        return startButton;
    }

    /**
     * 
     * @return 
     */
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
                    FxDialogs.showErrorDialog("There was a problem registering the native hook.");
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "There was a problem registering the native hook.", ex);
                }
            });
        }
        return stopButton;
    }

    /**
     * 
     * @return 
     */
    public Button getMinimizeButton() {
        if (minimizeButton == null) {
            minimizeButton = GlyphsDude.createIconButton(FontAwesomeIcon.MINUS, "", "28", "0", ContentDisplay.CENTER);
            minimizeButton.getStyleClass().addAll("toolbar-button-transparent", "default-green-button");
            TooltipBuilder.create("Minimize", minimizeButton);
            minimizeButton.setOnAction((ActionEvent event) -> {
                this.setIconified(true);
            });
        }
        return minimizeButton;
    }

    /**
     * 
     * @return 
     */
    public Button getCloseButton() {
        if (closeButton == null) {
            closeButton = GlyphsDude.createIconButton(FontAwesomeIcon.POWER_OFF, "", "28", "0", ContentDisplay.CENTER);
            closeButton.getStyleClass().addAll("toolbar-button-transparent", "default-green-button");
            TooltipBuilder.create("Close", closeButton);
            closeButton.setOnAction((ActionEvent event) -> {
                this.close();
            });
        }
        return closeButton;
    }

    /**
     * 
     * @return 
     */
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
                            // Ignora se for nulo ou nÃƒÂ£o tiver link
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
            // Delete listener
            imageTable.setOnKeyPressed((KeyEvent event) -> {
                ImageEN image = imageTable.getSelectionModel().getSelectedItem();
                if (event.getCode() == KeyCode.DELETE && FxDialogs.showConfirmDialog(this, "Delete Image", "Do you want to delete this image?").equals(FxDialogs.YES)) {
                    Callback<Basic, Void> callback = (Basic param) -> {
                        Platform.runLater(() -> {
                            try {
                                Files.delete(Paths.get(Locations.image(image.getFilename())));
                                Files.delete(Paths.get(Locations.thumbnail(image.getFilename())));
                                ImageEN.dao.delete(image);
                                imageTable.getItems().remove(image);
                            } catch (SQLException | IOException ex) {
                                // TODO log to console
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        return null;
                    };

                    if (image.getLink() == null) { // uploaded image
                        callback.call(null);
                    } else {
                        lockScreen();
                        new Thread(() -> {
                            try {
                                ImgurClient.ImgurV3Image.execute(ImgurClient.ImgurV3ImageRequest.DELETE, image, null, callback);
                            } catch (IOException ex) {
                                // TODO log to console
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            } finally {
                                unlockScreen();
                            }
                        }).start();
                    }

                }
            });
        }
        return imageTable;
    }

    /**
     * 
     * @return 
     */
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

    /**
     * 
     * @return 
     */
    public TextArea getConsole() {
        if (console == null) {
            console = new TextArea();
            console.setEditable(false);
            console.setWrapText(true);
            console.prefWidthProperty().bind(getImageTable().widthProperty().subtract(getAlbumTable().widthProperty()));
        }
        return console;
    }

    /**
     * 
     * @return 
     */
    public GroupBox getImageBox() {
        if (imageBox == null) {
            imageBox = new GroupBox("Images", getImageTable());
        }
        return imageBox;
    }

    /**
     * 
     * @return 
     */
    public GroupBox getAlbumBox() {
        if (albumBox == null) {
            albumBox = new GroupBox("Albums", getAlbumTable());
        }
        return albumBox;
    }

    /**
     * 
     * @return 
     */
    public GroupBox getConsoleBox() {
        if (consoleBox == null) {
            consoleBox = new GroupBox("Console", getConsole());
        }
        return consoleBox;
    }

    /**
     * 
     * @return 
     */
    public HBox getLoginBox() {
        if (loginBox == null) {
            loginBox = new HBox(10, getSignInButton());
        }
        return loginBox;
    }

    /**
     * 
     * @return 
     */
    public Button getSignInButton() {
        if (signInButton == null) {
            signInButton = new Button("sign in");
            signInButton.getStyleClass().addAll("toolbar-button-transparent", "default-green-button");
            signInButton.setOnAction((ActionEvent event) -> {
                new AuthenticationWindow(this, (AccessTokenEN accessToken) -> {
                    doLogin(accessToken);
                    return null;
                }).showAndWait();
            });
        }
        return signInButton;
    }

    /**
     * 
     * @return 
     */
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

            this.getScene().addEventFilter(MouseEvent.MOUSE_MOVED, (MouseEvent event) -> {
                Bounds buttonBounds = getLoggedButton().localToScene(getLoggedButton().getLayoutBounds());
                if (buttonBounds.contains(event.getSceneX(), event.getSceneY()) /*|| popupBounds.contains(event.getSceneX(), event.getSceneY())*/) {
                    if (!contextMenu.isShowing() && !getLockPane().isVisible()) { // only if the lockpane is not visible
                        Point2D p2d = loggedButton.localToScreen(loggedButton.getLayoutBounds().getMaxX() - loggedButton.getWidth(), loggedButton.getLayoutBounds().getMaxY());
                        contextMenu.show(this, p2d.getX(), p2d.getY());
                    }
                } else if (contextMenu.isShowing()) {
                    contextMenu.hide();
                }
            });
            /*loggedButton.setOnMouseEntered((MouseEvent event) -> {
             Point2D p2d = loggedButton.localToScreen(loggedButton.getLayoutBounds().getMaxX() - loggedButton.getWidth(), loggedButton.getLayoutBounds().getMaxY());
             contextMenu.show(this, p2d.getX(), p2d.getY());
             });
             loggedButton.setOnMouseClicked((MouseEvent event) -> {
             Point2D p2d = loggedButton.localToScreen(loggedButton.getLayoutBounds().getMaxX() - loggedButton.getWidth(), loggedButton.getLayoutBounds().getMaxY());
             contextMenu.show(this, p2d.getX(), p2d.getY());
             });*/
        }
        return loggedButton;
    }

    /**
     * 
     * @return 
     */
    public CheckBox getCheckBoxOpenLink() {
        if (checkBoxOpenLink == null) {
            checkBoxOpenLink = new CheckBox("On - Open in Browser\nOff - Copy to Clipboard");
            checkBoxOpenLink.setStyle("-fx-cursor: hand;");
        }
        return checkBoxOpenLink;
    }

    /**
     * 
     * @return 
     */
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

                lockScreen();

                new Thread(() -> {
                    try {
                        ImgurClient.ImgurV3Image.execute(ImgurClient.ImgurV3ImageRequest.UPLOAD, image, null, (Basic param) -> {
                            Platform.runLater(() -> {
                                if (param.getSuccess()) {
                                    // Print info to console
                                    //Dialogs.create().lightweight().styleClass(Dialog.STYLE_CLASS_CROSS_PLATFORM).title("Upload").masthead("Your image has been uploaded.").showInformation();
                                } else {
                                    FxDialogs.showErrorDialog("There was a problem uploading your image, please try again.");
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
                            FxDialogs.showErrorDialog(ex.getMessage());
                        });
                    } finally {
                        unlockScreen();
                    }
                }).start();
            });
        }
        return uploadButton;
    }

    /**
     * 
     */
    public void lockScreen() {
        getLockPane().setVisible(true);
    }

    /**
     * 
     */
    public void unlockScreen() {
        getLockPane().setVisible(false);
    }

    /**
     * 
     * @return 
     */
    public StackPane getLockPane() {
        if (lockPane == null) {
            lockPane = new StackPane();
            lockPane.setVisible(false);
            lockPane.setStyle("-fx-background-color: rgba(50,50,50,0.6); -fx-background-radius: 10px;");
            ((StackPane) this.getScene().getRoot()).getChildren().add(lockPane);
            lockPane.getChildren().add(new ImageView(new Image("img/loading-gallery.gif", 100, 100, true, true)));
        }
        return lockPane;
    }

    /**
     * 
     * @param accessToken 
     */
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

    /**
     * 
     * @param user 
     */
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

    /**
     * 
     */
    private void updateUploadButton() {
        ImageEN image = getImageTable().getSelectionModel().getSelectedItem();
        if (image == null || loggedUser == null) {
            getUploadButton().setDisable(true);
        } else {
            getUploadButton().setDisable(image.getLink() != null);
        }
    }

    private NativeKeyListener printScreenListener;
    public NativeKeyListener getPrintScreenListener() {
        if (printScreenListener == null) {
            printScreenListener = new NativeKeyListener() {
                @Override
                public void nativeKeyReleased(NativeKeyEvent nke) {
                    //System.out.println("Key Pressed: " + nke.paramString());
                    if (nke.getKeyCode() == NativeKeyEvent.VC_PRINTSCREEN) {
                        //System.out.println("Screen Printed!");
                        try {
                            ImageEN image = ImageEN.createImage(Functions.getScreenBufferedImage(), "png");
                            getImageTable().getItems().add(0, image);
                        } catch (AWTException | IOException | SQLException ex) {
                            String msg = "There was a problem saving the screenshot.";
                            FxDialogs.showErrorDialog(msg, "Please try again, maybe restarting the application will solve the issue.");
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, msg, ex);
                        }
                    }
                }

                @Override
                public void nativeKeyPressed(NativeKeyEvent nke) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Locations | Templates.
                }

                @Override
                public void nativeKeyTyped(NativeKeyEvent nke) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Locations | Templates.
                }
            };
        }
        return printScreenListener;
    }
    
}
