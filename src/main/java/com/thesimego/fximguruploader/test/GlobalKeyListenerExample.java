package com.thesimego.fximguruploader.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author drafaelli
 */
public class GlobalKeyListenerExample implements NativeKeyListener {
        @Override
        public void nativeKeyPressed(NativeKeyEvent e) {
            System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

            if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
                try {
                    GlobalScreen.unregisterNativeHook();
                } catch (NativeHookException ex) {
                    Logger.getLogger(GlobalKeyListenerExample.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent e) {
            System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        }

        @Override
        public void nativeKeyTyped(NativeKeyEvent e) {
            System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
        }

        public static void main(String[] args) {
            try {
                GlobalScreen.registerNativeHook();
            }
            catch (NativeHookException ex) {
                System.err.println("There was a problem registering the native hook.");
                System.err.println(ex.getMessage());

                System.exit(1);
            }

            GlobalScreen.addNativeKeyListener(new GlobalKeyListenerExample());
        }
    }