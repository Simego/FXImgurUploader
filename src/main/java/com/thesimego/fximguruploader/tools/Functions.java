package com.thesimego.fximguruploader.tools;

import flexjson.JSONSerializer;
import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 *
 * @author Simego
 */
public class Functions {

    private static final ClipboardData clipboardData = new ClipboardData();

    public static String getIdFromLink(String link) {
        return link.replaceAll("http(|s)://imgur.com/.*?/", "");
    }

    public static void saveClientID(String clientID) {
        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("clientid.txt", false))) {
                writer.write(clientID);
            }
        } catch (IOException ex) {
        }
    }

    public static String loadClientID() {
        try {
            String clientID;
            try (BufferedReader reader = new BufferedReader(new FileReader("clientid.txt"))) {
                clientID = reader.readLine();
            }
            return clientID;
        } catch (IOException ex) {
            return "";
        }
    }

    public static BufferedImage getPrintScreen(boolean activeWindowOnly) throws IOException, UnsupportedFlavorException, InterruptedException, AWTException {
        if (activeWindowOnly) {
            printActiveWindow();
            InputStream is = getImageInputStream(clipboardData.getImageData());
            return ImageIO.read(is);
        } else {
            BufferedImage im = getScreenBufferedImage();
            clipboardData.setImageData(im);
            return im;
        }
    }

    public static BufferedImage getScreenBufferedImage() throws AWTException {
        Robot robot = new Robot();
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage capture = robot.createScreenCapture(screenRect);
        return capture;
    }

    private static InputStream getImageInputStream(Image clipboardImage) throws IOException {
        BufferedImage bi = new BufferedImage(clipboardImage.getWidth(null), clipboardImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(clipboardImage, 0, 0, null);
        bg.dispose();

        byte[] imageInByte;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bi, "jpg", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
        }

        InputStream is = new ByteArrayInputStream(imageInByte);
        return is;
    }

    private static void printActiveWindow() {
        Robot robot;

        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new IllegalArgumentException("No robot");
        }

        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_PRINTSCREEN);
        robot.keyRelease(KeyEvent.VK_PRINTSCREEN);
        robot.keyRelease(KeyEvent.VK_ALT);
    }

    /**
     *
     * @param url
     * @return
     */
    public static Map<String, String> splitQuery(String url) {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        url = url.replace("#", "?");
        String[] split = url.split("\\?");
        String query = split[split.length - 1];
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            try {
                query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
            }
        }
        return query_pairs;
    }

    public static void saveCookies() {
        PrintWriter writer = null;
        try {
            CookieManager manager = (CookieManager) CookieHandler.getDefault();
            manager.getCookieStore().getCookies();
            JSONSerializer cookieSerializer = new JSONSerializer();
            String cookieJson = cookieSerializer.exclude("class").serialize(manager.getCookieStore().getCookies());
//                    System.out.println(cookieJson);
            writer = new PrintWriter(Locations.HOME + "/cookies.tmp", "UTF-8");
            writer.write(cookieJson);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
