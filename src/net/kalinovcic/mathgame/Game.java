package net.kalinovcic.mathgame;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.util.Random;

public class Game extends Canvas
{
    private static final long serialVersionUID = 1L;
    
    public int width  = 1920;
    public int height = 1080;

    public Random random = new Random();
    public Stage stage = new TableShooterStage(this); // new SumStage(this, 5813, 8346, true);
    public float secondsSinceStart = 0.0f;
    
    public Game()
    {
        Input input = new Input();
        addKeyListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);
    }
    
    public void update(double dt)
    {
        Input.updateInput();
        secondsSinceStart += dt;
        
        stage.update((float) dt);
    }
    
    public void render()
    {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null)
        {
            createBufferStrategy(2);
            return;
        }

        width = getWidth();
        height = getHeight();
        
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.GRAY);
        g.fillRect(0, 0, width, height);

        g.setComposite(AlphaComposite.SrcOver.derive(1.0f));
        stage.render(g);
        
        g.dispose();
        bs.show();
    }
}
