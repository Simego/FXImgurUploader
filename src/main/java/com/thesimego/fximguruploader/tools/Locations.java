package com.thesimego.fximguruploader.tools;

/**
 *
 * @author drafaelli
 */
public class Locations {

    private static final String HOME_FOLDER = System.getProperty("user.home");
    public static final String HOME = HOME_FOLDER.concat( HOME_FOLDER.endsWith("\\") || HOME_FOLDER.endsWith("/") ? "ImgurUploader" : "\\ImgurUploader" );
    public static final String TMP_LOCATION = System.getProperty("java.io.tmpdir")+"\\ImgurUploader";
    public static final String COOKIE_FILE = HOME + "\\cookies.tmp";
    public static final String IMAGE_FOLDER = HOME + "\\images";
    public static final String THUMBNAIL_FOLDER = HOME + "\\images\\thumbnails";
    
    
    public static String image(String filename) {
        return IMAGE_FOLDER + "\\" + filename;
    }

    public static String thumbnail(String filename) {
        return THUMBNAIL_FOLDER + "\\" +filename;
    }

}