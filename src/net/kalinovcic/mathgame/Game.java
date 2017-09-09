package net.kalinovcic.mathgame;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends Canvas
{
    private static final long serialVersionUID = 1L;
    
    public int width  = 1920;
    public int height = 1080;

    public Random random = new Random();
    public Stage stage = new TableShooterStage(this);
    public float secondsSinceStart = 0.0f;
    
    public Game()
    {
        Input input = new Input();
        addKeyListener(input);
    }
    
    public List<Particle> particles = new ArrayList<Particle>();

    private int particleSpawnCounter = 0;
    private float backgroundParticleHue = 180;
    
    private void updateParticles(double dt)
    {
        backgroundParticleHue += dt * 0.02;
        
        particleSpawnCounter--;
        if (particleSpawnCounter <= 0)
        {
            particleSpawnCounter = 5;

            Particle p = new Particle();
            p.x = random.nextInt(width);
            p.y = random.nextInt(height);
            p.vx = (random.nextFloat() - 0.5f) * 50f;
            p.vy = (random.nextFloat() - 0.5f) * 50f;
            p.size = 400;
            
            float hue = backgroundParticleHue;
            p.color = Color.getHSBColor(hue + (random.nextFloat() - 0.5f) * 0.1f, 1.0f, 1.0f);           
            particles.add(p);
        }
        
        for (int i = 0; i < particles.size(); i++)
        {
            Particle p = particles.get(i);
            p.update((float) dt);
            if (p.life > p.duration)
            {
                int last = particles.size() - 1;
                particles.set(i, particles.get(last));
                particles.remove(last);
                i--;
            }
        }
    }
    
    public void update(double dt)
    {
        Input.updateInput();
        secondsSinceStart += dt;
        
        stage.update((float) dt);
        updateParticles(dt);
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
        
        if (!stage.isOpaque)
        {
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, width, height);
            
            for (Particle p : particles)
                p.render(g);
        }

        g.setComposite(AlphaComposite.SrcOver.derive(1.0f));
        stage.render(g);
        
        g.dispose();
        bs.show();
    }
}
