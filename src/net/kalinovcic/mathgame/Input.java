package net.kalinovcic.mathgame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input implements KeyListener, MouseListener, MouseMotionListener
{
    public static boolean[] liveKeyDown = new boolean[65535];
    public static boolean[] currentKeyDown = new boolean[65535];
    public static boolean[] previousKeyDown = new boolean[65535];

    public static int mouseX;
    public static int mouseY;
    public static boolean liveLeftMouseDown;
    public static boolean currentLeftMouseDown;
    public static boolean previousLeftMouseDown;
    
    public static void updateInput()
    {
        System.arraycopy(currentKeyDown, 0, previousKeyDown, 0, currentKeyDown.length);
        System.arraycopy(liveKeyDown, 0, currentKeyDown, 0, liveKeyDown.length);
        
        previousLeftMouseDown = currentLeftMouseDown;
        currentLeftMouseDown = liveLeftMouseDown;
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
    
    public static boolean isLeftMouseDown()
    {
        return currentLeftMouseDown;
    }
    
    public static boolean isLeftMousePressed()
    {
        return currentLeftMouseDown && !previousLeftMouseDown;
    }
    
    public static boolean isLeftMouseReleased()
    {
        return !currentLeftMouseDown && previousLeftMouseDown;
    }

    @Override public void keyPressed(KeyEvent e) { liveKeyDown[e.getKeyCode()] = true; }
    @Override public void keyReleased(KeyEvent e) { liveKeyDown[e.getKeyCode()] = false; }
    @Override public void keyTyped(KeyEvent e) {}

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mousePressed(MouseEvent e)  { if (e.getButton() == MouseEvent.BUTTON1) liveLeftMouseDown = true; }
    @Override public void mouseReleased(MouseEvent e) { if (e.getButton() == MouseEvent.BUTTON1) liveLeftMouseDown = false; }

    @Override public void mouseDragged(MouseEvent e) { mouseX = e.getX(); mouseY = e.getY(); }
    @Override public void mouseMoved(MouseEvent e) { mouseX = e.getX(); mouseY = e.getY(); }
}
