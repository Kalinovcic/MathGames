package net.kalinovcic.mathgame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener
{
    public static boolean[] liveKeyDown = new boolean[65535];
    public static boolean[] currentKeyDown = new boolean[65535];
    public static boolean[] previousKeyDown = new boolean[65535];
    
    public static void updateInput()
    {
        System.arraycopy(currentKeyDown, 0, previousKeyDown, 0, currentKeyDown.length);
        System.arraycopy(liveKeyDown, 0, currentKeyDown, 0, liveKeyDown.length);
    }
    
    public static boolean isKeyDown(int key)
    {
        return currentKeyDown[key];
    }
    
    public static boolean isKeyPressed(int key)
    {
        return currentKeyDown[key] && !previousKeyDown[key];
    }
    
    public static boolean isKeyReleased(int key)
    {
        return !currentKeyDown[key] && previousKeyDown[key];
    }

    @Override public void keyPressed(KeyEvent e) { liveKeyDown[e.getKeyCode()] = true; }
    @Override public void keyReleased(KeyEvent e) { liveKeyDown[e.getKeyCode()] = false; }
    @Override public void keyTyped(KeyEvent e) {}
}
