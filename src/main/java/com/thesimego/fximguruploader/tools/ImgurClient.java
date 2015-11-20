package com.thesimego.fximguruploader.tools;

import com.thesimego.fximguruploader.views.Main;
import com.thesimego.fximguruploader.entity.AccessTokenEN;
import com.thesimego.fximguruploader.entity.AlbumEN;
import com.thesimego.fximguruploader.entity.ImageEN;
import com.thesimego.fximguruploader.entity.PreferencesEN;
import com.thesimego.fximguruploader.entity.imgur.Basic;
import com.thesimego.fximguruploader.entity.imgur.ImgurImage;
import flexjson.JSONDeserializer;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.util.Callback;
import javax.imageio.ImageIO;
import org.apache.commons.codec.binary.Base64;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author drafaelli
 */
public class ImgurClient {

    private final Main mainView;

    public static final String CLIENT_ID = "CLIENT_ID_FROM_YOUR_IMGUR_APPLICATION";
    public static final String CLIENT_SECRET = "CLIENT_SECRET_FROM_YOUR_IMGUR_APPLICATION";

    public ImgurClient(Main view) {
        mainView = view;
    }

//    public ImgurImageBasic doImageUpload(String clientID, boolean activeWindowOnly, String album) {
//        try {
//            BufferedImage image = Functions.getPrintScreen(activeWindowOnly);
//            String content = imageUpload(clientID, image, album);
//
//            JSONDeserializer<ImgurImage> streamDes = new JSONDeserializer<>();
//            ImgurImageBasic imgur = streamDes.deserialize(content, ImgurImageBasic.class);
//
//            return imgur;
//        } catch (UnsupportedFlavorException | IOException | InterruptedException | AWTException ex) {
//            if (mainView == null) {
//                System.err.println(ex.getMessage());
//            } else {
//                mainView.outputError(ex.getMessage());
//            }
//            return null;
//        }
//    }
    private String imageUpload(String token, BufferedImage image, String album) throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArray);
        byte[] byteImage = byteArray.toByteArray();
        String dataImage = Base64.encodeBase64String(byteImage);
        String data;

        data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(dataImage, "UTF-8");
        if (album != null) {
            data += "&" + URLEncoder.encode("album", "UTF-8") + "=" + URLEncoder.encode(album, "UTF-8");
        }
        //data += "&" + URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode("dasdasasdaadsdasdad", "UTF-8");

        URL url = new URL("https://api.imgur.com/3/image");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + token);

        conn.connect();
        StringBuilder stb = new StringBuilder();
        try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
            wr.write(data);
            wr.flush();
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = rd.readLine()) != null) {
                    stb.append(line).append("\n");
                }
            }
        }

        return stb.toString();
    }

//    public boolean imageDelete(String token, String deleteHash) {
//        try {
//            URL url;
//            url = new URL("https://api.imgur.com/3/image/" + deleteHash);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//            conn.setDoInput(true);
//            conn.setRequestMethod("DELETE");
//            conn.setRequestProperty("Authorization", "Bearer " + token);
//
//            conn.connect();
//            StringBuilder stb = new StringBuilder();
//            try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
//                String line;
//                while ((line = rd.readLine()) != null) {
//                    stb.append(line).append("\n");
//                }
//            }
//            return true;
//            //System.out.println(stb.toString());
//        } catch (IOException ex) {
//            if (ex instanceof FileNotFoundException) {
//                String message = "Image not found at Imgur.com, probably already deleted, local entry deleted.";
//                if (mainView != null) {
//                    mainView.outputError(message);
//                } else {
//                    System.err.println(message);
//                }
//                return true;
//            } else {
//                if (mainView == null) {
//                    System.err.println(ex.getMessage());
//                } else {
//                    mainView.outputError(ex.getMessage());
//                }
//                return true;
//            }
//        }
//    }
//
//    public Album doAlbumCreate(String clientID, String title, String layout, String privacy, String cover) {
//
//        if (cover != null && !cover.trim().isEmpty() && !cover.matches("http(|s)\\://(|i\\.)imgur\\.com/.*")) {
//            JOptionPane.showMessageDialog(null, "Cover must be an Imgur Image Link.", "Error", JOptionPane.ERROR_MESSAGE);
//            return null;
//        }
//
//        String content = albumCreate(clientID, title, layout, privacy, cover);
//
//        JSONDeserializer<Album> streamDes = new JSONDeserializer<>();
//        Album album = streamDes.deserialize(content, Album.class);
//        album.getData().setTitle(title);
//
//        if (album.getData().getDeletehash() == null) {
//            JOptionPane.showMessageDialog(null, "Something happened and couldn't create the album, try again.", "Error", JOptionPane.ERROR_MESSAGE);
//            return null;
//        }
//
//        return album;
//    }
//
//    private String albumCreate(String clientID, String title, String layout, String privacy, String cover) {
//        try {
//            String data;
//            data = URLEncoder.encode("layout", "UTF-8") + "=" + URLEncoder.encode(layout, "UTF-8");
//            data += "&" + URLEncoder.encode("privacy", "UTF-8") + "=" + URLEncoder.encode(privacy, "UTF-8");
//            data += "&" + URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8");
//            if (cover != null && !cover.trim().isEmpty()) {
//                data += "&" + URLEncoder.encode("cover", "UTF-8") + "=" + URLEncoder.encode(cover, "UTF-8");
//            }
//
//            URL url = new URL("https://api.imgur.com/3/album/");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Authorization", "Client-ID " + clientID);
//
//            conn.connect();
//            StringBuilder stb = new StringBuilder();
//
//            try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
//                wr.write(data);
//                wr.flush();
//                try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
//                    String line;
//                    while ((line = rd.readLine()) != null) {
//                        stb.append(line).append("\n");
//                    }
//                }
//            }
//
//            //System.out.println(stb.toString());
//            return stb.toString();
//        } catch (IOException ex) {
//            if (mainView == null) {
//                System.err.println(ex.getMessage());
//            } else {
//                mainView.outputError(ex.getMessage());
//            }
//            return null;
//        }
//    }
//
//    public boolean albumDelete(String clientID, String deleteHash) {
//        try {
//            URL url;
//            url = new URL("https://api.imgur.com/3/album/" + deleteHash);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//            conn.setDoInput(true);
//            conn.setRequestMethod("DELETE");
//            conn.setRequestProperty("Authorization", "Client-ID " + clientID);
//
//            conn.connect();
//            StringBuilder stb = new StringBuilder();
//            try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
//                String line;
//                while ((line = rd.readLine()) != null) {
//                    stb.append(line).append("\n");
//                }
//            }
//            return true;
//            //System.out.println(stb.toString());
//        } catch (IOException ex) {
//            if (ex instanceof FileNotFoundException) {
//                String message = "Album not found at Imgur.com, probably already deleted, local entry deleted.";
//                if (mainView != null) {
//                    mainView.outputError(message);
//                } else {
//                    System.err.println(message);
//                }
//                return true;
//            } else {
//                if (mainView != null) {
//                    mainView.outputError(ex.getMessage());
//                } else {
//                    System.err.println(ex.getMessage());
//                }
//                return true;
//            }
//        }
//    }
//
//    public Album albumInfo(String clientID, String link) {
//        String albumID = Functions.getIdFromLink(link);
//        try {
//            URL url = new URL(/*ImgurUrlV3.albumID(albumID)*/""); // FIXME
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//            conn.setDoInput(true);
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Authorization", "Client-ID " + clientID);
//
//            conn.connect();
//            StringBuilder stb = new StringBuilder();
//            try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
//                String line;
//                while ((line = rd.readLine()) != null) {
//                    stb.append(line).append("\n");
//                }
//            }
//            System.out.println(stb.toString());
//            JSONDeserializer<Album> streamDes = new JSONDeserializer<>();
//            Album album = streamDes.deserialize(stb.toString(), Album.class);
//
//            return album;
//            //System.out.println(stb.toString());
//        } catch (IOException ex) {
//            if (mainView == null) {
//                System.err.println(ex.getMessage());
//            } else {
//                mainView.outputError(ex.getMessage());
//            }
//            return null;
//        }
//    }
    private static AccessTokenEN refreshToken(AccessTokenEN accessToken) throws IOException {
        String data = "refresh_token={0}&client_id={1}&client_secret={2}&grant_type=refresh_token";
        data = data.replace("{0}", URLEncoder.encode(accessToken.getRefreshToken(), "UTF-8"));
        data = data.replace("{1}", URLEncoder.encode(CLIENT_ID, "UTF-8"));
        data = data.replace("{2}", URLEncoder.encode(CLIENT_SECRET, "UTF-8"));
//        data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(dataImage, "UTF-8");
//        if (album != null) {
//            data += "&" + URLEncoder.encode("album", "UTF-8") + "=" + URLEncoder.encode(album, "UTF-8");
//        }
        //data += "&" + URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode("dasdasasdaadsdasdad", "UTF-8");

        URL url = new URL("https://api.imgur.com/oauth2/token");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");

        conn.connect();
        StringBuilder stb = new StringBuilder();
        try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
            wr.write(data);
            wr.flush();
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = rd.readLine()) != null) {
                    stb.append(line).append("\n");
                }
            }
        }

        JSONDeserializer<AccessTokenEN> deserializer = new JSONDeserializer<>();
        AccessTokenEN newToken = deserializer.deserialize(stb.toString(), AccessTokenEN.class);
        accessToken.setAccessToken(newToken.getAccessToken());
        accessToken.setRefreshToken(newToken.getRefreshToken());
        accessToken.setExpiresIn(newToken.getExpiresIn());
        accessToken.setTokenType(newToken.getTokenType());
        accessToken.setAccountUsername(newToken.getAccountUsername());
        accessToken.setLastUpdate(new Date());
        try {
            AccessTokenEN.dao.update(accessToken);
        } catch (SQLException ex) {
            Platform.runLater(() -> {
                Dialogs.create().message("There was an error getting a new Access Token from Imgur, try relogging.").showError();
            });
        }
        return accessToken;
    }

    private static class ImgurBase {

        public String data;

        public String executeRequest(HttpURLConnection conn) throws IOException {
            if (data == null) {
                return executeWithoutData(conn);
            } else {
                return executeWithData(conn);
            }
        }

        private String executeWithData(HttpURLConnection conn) throws IOException {
            conn.connect();
            StringBuilder stb = new StringBuilder();
            try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
                wr.write(data);
                wr.flush();
                try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = rd.readLine()) != null) {
                        stb.append(line).append("\n");
                    }
                }
            }
            return stb.toString();
        }

        private String executeWithoutData(HttpURLConnection conn) throws IOException {
            conn.connect();
            StringBuilder stb = new StringBuilder();
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = rd.readLine()) != null) {
                    stb.append(line).append("\n");
                }
            }
            return stb.toString();
        }

        public HttpURLConnection createConnection(String urlString, ImgurMethod method, String dataToSend) throws IOException {
            data = dataToSend;
            PreferencesEN preference = PreferencesEN.get();
            AccessTokenEN accessToken = AccessTokenEN.getByAccountId(preference.getLoggedAccountId());

            Date lastDate = new Date(accessToken.getLastUpdate().getTime() + accessToken.getExpiresIn() * 1000);
            if (new Date().after(lastDate)) {
                accessToken = ImgurClient.refreshToken(accessToken);
            }

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            switch (method) {
//                case GET:
//                    break;
                case POST:
                    conn.setDoOutput(true);
                    break;
//                case DELETE:
//                    break;
                default:
                    conn.setDoInput(true);
                    break;
            }

            conn.setRequestMethod(method.name());
            conn.setRequestProperty("Authorization", "Bearer " + accessToken.getAccessToken());
            return conn;
        }

    }

    public static class ImgurV3Image extends ImgurBase {

        public static void execute(ImgurV3ImageRequest type, ImageEN image, AlbumEN album, Callback<Basic, Void> callback) throws IOException {

            PreferencesEN preference = PreferencesEN.get();
            AccessTokenEN accessToken = AccessTokenEN.getByAccountId(preference.getLoggedAccountId());
            if(accessToken == null) {
                Platform.runLater(() -> {
                    Dialogs.create()
                            .lightweight()
                            .styleClass(Dialog.STYLE_CLASS_CROSS_PLATFORM)
                            .title("Authentication Error")
                            .masthead("You are not logged.")
                            .message("To use the system please first log-in to Imgur via the \"sign in\" button.")
                            .showError();
                });
                return;
            }
            
            HttpURLConnection conn = null;
            ImgurV3Image v3Request = new ImgurV3Image();
            switch (type) {
                case UPLOAD:
                    conn = v3Request.doUpload(image, album);
                    break;
            }

            String response = v3Request.executeRequest(conn);
            Basic basic = new JSONDeserializer<Basic>().use("data", ImgurImage.class).deserialize(response, Basic.class);

            try {
                switch (type) {
                    case UPLOAD:
                        ImgurImage i = (ImgurImage) basic.getData();
                        image.setImageId(i.getId());
                        image.setLink(i.getLink());
                        ImageEN.dao.update(image);
                        break;
                }
                callback.call(basic);
            } catch (SQLException ex) {
                Logger.getLogger(ImgurClient.class.getName()).log(Level.SEVERE, "Failed to update Image entity.", ex);
            }

        }

        private String find(String id) {
            return ImgurV3ImageRequest.FIND.link.replace("{0}", id);
        }

        private String delete(String id) {
            return ImgurV3ImageRequest.DELETE.link.replace("{0}", id);
        }

        private String update(String id) {
            return ImgurV3ImageRequest.UPDATE.link.replace("{0}", id);
        }

        private String favorite(String id) {
            return ImgurV3ImageRequest.FAVORITE.link.replace("{0}", id);
        }
        
        private HttpURLConnection doUpload(ImageEN image, AlbumEN album) throws IOException {
            BufferedImage bi = ImageIO.read(new File(Locations.image(image.getFilename())));
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", byteArray);
            byte[] byteImage = byteArray.toByteArray();
            String dataImage = Base64.encodeBase64String(byteImage);
            data = "";

            data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("base64", "UTF-8");
            if (image.getTitle() != null) {
                data += "&" + URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(image.getTitle(), "UTF-8");
            }
            if (album != null) {
                data += "&" + URLEncoder.encode("album", "UTF-8") + "=" + URLEncoder.encode(album.getAlbumId(), "UTF-8");
            }
            data += "&" + URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(dataImage, "UTF-8");

            return createConnection(ImgurV3ImageRequest.UPLOAD.link, ImgurV3ImageRequest.UPLOAD.method, data);
        }
        
    }

    public enum ImgurV3ImageRequest {

        FIND("https://api.imgur.com/3/image/{0}", ImgurMethod.GET),
        UPLOAD("https://api.imgur.com/3/image", ImgurMethod.POST),
        DELETE("https://api.imgur.com/3/image/{0}", ImgurMethod.DELETE),
        UPDATE("https://api.imgur.com/3/image/{0}", ImgurMethod.POST),
        FAVORITE("https://api.imgur.com/3/image/{0}/favorite", ImgurMethod.POST);

        public String link;
        public ImgurMethod method;

        ImgurV3ImageRequest(String link, ImgurMethod method) {
            this.link = link;
            this.method = method;
        }

    }

    public enum ImgurMethod {

        GET, POST, DELETE
    }

}
