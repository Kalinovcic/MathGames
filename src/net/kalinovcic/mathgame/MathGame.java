package net.kalinovcic.mathgame;

import javax.swing.JFrame;

public class MathGame
{
    public static void main(String[] args) throws InterruptedException
    {
        Game game = new Game();
        
        JFrame frame = new JFrame("Igra");
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        //frame.setSize(800, 600);
        frame.setVisible(true);

        frame.toFront();
        frame.requestFocus();
        frame.setAlwaysOnTop(true); 
        frame.setAlwaysOnTop(false);

        game.setVisible(true);
        game.requestFocus();
        
        long lastTime = System.nanoTime();
        while (true)
        {
            long currentTime = System.nanoTime();
            long deltaTime = currentTime - lastTime;
            double delta = deltaTime / 1000000000.0;
            lastTime = currentTime;

            game.update(delta);
            game.render();
            Thread.sleep(10);
        }
    }
}
