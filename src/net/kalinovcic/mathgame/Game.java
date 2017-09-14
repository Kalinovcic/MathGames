package net.kalinovcic.mathgame;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.util.Random;

import net.kalinovcic.mathgame.SumStage.Task;

public class Game extends Canvas
{
    private static final long serialVersionUID = 1L;
    
    public int width  = 1920;
    public int height = 1080;

    public Random random = new Random();

    public Stage stage = new SumStage(this, new Task[]
    {
        new Task(new int[] { 498765, 37182 }, false),
        new Task(new int[] { 459876, 51682 }, false),
        new Task(new int[] { 97368, 4988, 7379 }, false),
        new Task(new int[] { 80316, 9583, 4979 }, false),
        new Task(new int[] { 873594, 60873, 57942, 8698 }, false),
        new Task(new int[] { 45976, 3879, 2046, 5124 }, false),
        new Task(new int[] { 73876, 4935, 2161 }, false),
        new Task(new int[] { 389764, 87969 }, false),
        new Task(new int[] { 987632, 281201 }, true),
        new Task(new int[] { 863201, 49583 }, true),
        new Task(new int[] { 654127, 45068  }, true),
        new Task(new int[] { 38614, 9378 }, true),
        new Task(new int[] { 720653, 13745 }, true),
        new Task(new int[] { 307624, 118954 }, true),
        new Task(new int[] { 38264, 24888  }, true),
        new Task(new int[] { 59641, 14903 }, true)
    });
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
