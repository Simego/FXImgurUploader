package com.thesimego.fximguruploader.tools;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ClipboardData implements ClipboardOwner {

    /**
     * Empty implementation of the ClipboardOwner interface.
     *
     * @param aClipboard
     * @param aContents
     */
    @Override
    public void lostOwnership(Clipboard aClipboard, Transferable aContents) {
        //do nothing
    }

    /**
     * Place a String on the clipboard, and make this class the owner of the Clipboard's contents.
     *
     * @param aString
     */
    public void setStringData(String aString) {
        StringSelection stringSelection = new StringSelection(aString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this);
    }

    /**
     * Place an Image on the clipboard, and make this class the owner of the Clipboard's contents.
     *
     * @param aImage
     */
    public void setImageData(Image aImage) {
        TransferableImage transferable = new TransferableImage(aImage);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(transferable, this);
    }

    /**
     * Get the String residing on the clipboard.
     *
     * @return any text found on the Clipboard; if none found, return an empty String.
     * @throws java.awt.datatransfer.UnsupportedFlavorException
     * @throws java.io.IOException
     */
    public String getStringData() throws UnsupportedFlavorException, IOException {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        //odd: the Object param of getContents is not currently used
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            result = (String) contents.getTransferData(DataFlavor.stringFlavor);
        }
        return result;
    }

    /**
     * Get the Image residing on the clipboard.
     *
     * @return any text found on the Clipboard; if none found, return an empty Image.
     * @throws java.awt.datatransfer.UnsupportedFlavorException
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public Image getImageData() throws UnsupportedFlavorException, IOException, InterruptedException {
        Thread.sleep(200);
        Image result = null;
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        //odd: the Object param of getContents is not currently used
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.imageFlavor);
        if (hasTransferableText) {
            result = (Image) contents.getTransferData(DataFlavor.imageFlavor);
        }
        return result;
    }

}
